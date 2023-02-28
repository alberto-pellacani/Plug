package com.plug.dao;

import com.plug.dto.Tenant;
import com.plug.dto.User;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.HandleConsumer;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.result.ResultIterable;
import org.jdbi.v3.core.statement.Query;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class TenantDao extends Dao {

    public static final String NUMERATOR_ID="tenant_md";

    public TenantDao(Handle handle) {
        super(handle);
    }


    public Tenant getById(int id) {

        Query query = this.handle().createQuery("select id, name from tenantmd where id=?");
        return this.map(query).findOnly();
    }

    public List<Tenant> getByUser(User user) {
        return user==null ? null : getByUser(user.getId());
    }

    public List<Tenant> getByUser(int user_id) {
        Query query = this.handle().createQuery("select tm.tenant_id , tm.name from user_tenant ut left join tenant_md tm on tm.tenant_id = ut.tenant_id where ut.user_id = ?");
        query.bind(0,user_id);

        return this.map(query).list();
    }

    public boolean exists(Tenant tenant) {
        return exists(new Tenant[] { tenant})==null;
    }

    public Tenant exists(Tenant... tenants) {
        return exists(Arrays.asList(tenants));
    }

    public Tenant exists(Iterable<Tenant> tenants) {
        Query query = this.handle().createQuery("SELECT tenant_id FROM tenant_md where tenant_id=?");
        for (Tenant tenant : tenants) {
            Integer r = query.bind(0,tenant.getId()).mapTo(Integer.class).findOnly();
            if(r==null || r == 0) return  tenant;
        }
        return null;
    }

    public void insert(Tenant tenant) throws SQLException {

        TransactionDao.create(this).useTransaction(new HandleConsumer<SQLException>() {
            @Override
            public void useHandle(Handle handle) throws SQLException {

                int id = new NumeratorDao(handle).nextInteger(0,  NUMERATOR_ID);

                tenant.setId(id);

                handle.createUpdate("INSERT INTO tenant_md (tenant_id, name) VALUES (?, ?)")
                        .bind(0, id).bind(1, tenant.getName()).execute();


            }
        });

    }

    private ResultIterable<Tenant> map(Query query) {
        return query.map(new RowMapper<Tenant>(){
            @Override
            public Tenant map(ResultSet rs, StatementContext ctx) throws SQLException {

                Tenant tenant = new Tenant();
                tenant.setId(rs.getInt(1));
                tenant.setName(rs.getString(2));
                return tenant;
            }


        });
    }

}
