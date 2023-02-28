package com.plug.dao;

import com.plug.dto.Numerator;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.result.ResultSetScanner;
import org.jdbi.v3.core.statement.Query;
import org.jdbi.v3.core.statement.StatementContext;
import org.jdbi.v3.core.statement.Update;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.function.Supplier;

public class NumeratorDao extends Dao {

    public NumeratorDao(Handle handle) {
        super(handle);
    }

    public int nextInteger(int tenant, String id) {
        return next(tenant, id).getIndex();
    }

    public String nextNumber(int tenant, String id) {
        return next(tenant, id).getNumber();
    }

    public Numerator next(int tenant, Class clazz) {
        return next(tenant, clazz.getName());
    }

    public Numerator next(int tenant, String id) {


        Date now = Calendar.getInstance().getTime();

        Query q = this.handle().createQuery("select next_idx, offset_idx, format, start_at, end_at from number_md where tenant=? and id=? and start_at <= ? and (end_at is null or end_at >= ?) order by start_at desc");
        q.setMaxRows(1);
        q.bind(0, tenant).bind(1, id ).bind(2, now).bind(3, now);
        Numerator n = q.scanResultSet(new ResultSetScanner<Numerator>() {

            @Override
            public Numerator scanResultSet(Supplier<ResultSet> resultSetSupplier, StatementContext ctx) throws SQLException {

                Numerator n = new Numerator();
                n.setTenant(tenant);
                n.setId(id);
                ResultSet rs = resultSetSupplier.get();
                if(rs.next()) {
                    n.setIndex(rs.getInt(1));
                    n.setOffset(rs.getInt(2));
                    n.setFormat(rs.getString(3));
                    n.setStartAt(rs.getDate(4));
                    n.setEndAt(rs.getDate(5));
                }

                return n;
            }
        });

        if(n.getIndex()==0) {


            n.setOffset(1);
            n.setIndex(1);
            n.setStartAt(now);
            n.setFormat("%15d");
            n.setNumber( String.format(n.getFormat(),n.getIndex()));

            Update u = this.handle().createUpdate("insert into number_md (tenant, id, start_at, format, next_idx, offset_idx, last_number, created_at, updated_at) values (?, ?, ?, ?, ?, ?, ?, ?, ?)");
            u.bind(0, tenant).bind(1, id).bind(2, n.getStartAt()).bind(3, n.getFormat())
                    .bind(4, n.getIndex() + n.getOffset())
                    .bind(5, n.getOffset())
                    .bind(6, n.getNumber()).bind(7, now).bind(8, now);
            u.execute();

        } else {

            n.setNumber( String.format(n.getFormat(),n.getIndex()));

            Update u = this.handle().createUpdate("update number_md set next_idx=?, updated_at=?, last_number=? where tenant=? and id=? and start_at=?");
            u.bind(0, tenant).bind(1, n.getIndex() + Math.max(n.getOffset(),1))
                    .bind(2, now).bind(3, n.getNumber())
                    .bind(4,id).bind(5, n.getStartAt());
            u.execute();
        }


        return n;


    }

}
