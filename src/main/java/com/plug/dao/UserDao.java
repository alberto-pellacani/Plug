package com.plug.dao;

import com.plug.dto.Tenant;
import com.plug.dto.User;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.HandleConsumer;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.result.ResultIterable;
import org.jdbi.v3.core.statement.Query;
import org.jdbi.v3.core.statement.StatementContext;
import org.jdbi.v3.core.statement.Update;

import java.security.MessageDigest;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

public class UserDao extends Dao {

    public static final String NUMERATOR_ID="user_md";
    private static final Pattern HEX_MATCHER = Pattern.compile("^[0-9a-f]+$");
    private static final char[] HEX_ARRAY = "0123456789abcdef".toCharArray();

    public UserDao(Handle handle) {
        super(handle);
    }


    public static String passwordToMD5(String password) {

        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(password.getBytes());
            byte[] digest = md.digest();
            return bytesToHex(digest);
        } catch (Exception e) {

        }
        return null;
    }



    private static boolean isHex(String value) {
        return value!=null && value.length() % 2 == 0 && HEX_MATCHER.matcher(value).matches();
    }




    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }

    public void updateTenant(int user_id, Tenant... tenants) throws SQLException {
        this.updateTenant(user_id, Arrays.asList(tenants));
    }

    public void updateTenant(int user_id, Iterable<Tenant> tenants) throws SQLException {

        TenantDao tdao = new TenantDao(this.handle());
        Tenant t = tdao.exists(tenants);
        if(t!=null) {
            throw new SQLException("Il Tenant " + t + " non esiste !");
        }


        TransactionDao.create(this).useTransaction(new HandleConsumer<SQLException>() {
            @Override
            public void useHandle(Handle handle) throws SQLException {

                removeTenant(handle,user_id);



                Update update = handle.createUpdate("INSERT INTO user_tenant (user_id, tenant_id) VALUES (?, ?)");

                for(Tenant t : tenants) {



                    update.bind(0, user_id).bind(1, t.getId()).execute();
                }
            }
        });
    }



    public void removeTenant(int user_id) throws SQLException {
        this.removeTenant(this.handle(),user_id);
    }

    public void removeTenant(Handle handle, int user_id) throws SQLException {
        handle.createUpdate("DELETE FROM user_tenant WHERE user_id=?").bind(0, user_id).execute();
    }

    public User insert(User user) {

        user.setId( new NumeratorDao(this.handle()).nextInteger(0, NUMERATOR_ID));

        this.ensureMD5(user);

        Date now = Calendar.getInstance().getTime();

        Update update = this.handle().createUpdate("INSERT INTO user_md (id, name, pwd, description, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?)");
        update.bind(0, user.getId())
                .bind(1,user.getName())
                .bind(2, user.getPassword())
                .bind(3, user.getDescription())
                .bind(4, now)
                .bind(5, now).execute();



        return user;
    }

    private void ensureMD5(User user) {
        // la password deve essere in md5
        if( !isHex( user.getPassword() )) {
            user.setPassword(  passwordToMD5( user.getPassword() )   );
        }
    }

    public User update(User user) {

        this.ensureMD5(user);

        Date now = Calendar.getInstance().getTime();
        Update update = this.handle().createUpdate("UPDATE user_md SET name=?, pwd=?, description=?, updated_at=? WHERE id=?");
        update.bind(0, user.getName())
                .bind(1, user.getPassword())
                .bind(2, user.getDescription())
                .bind(3, now)
                .bind(4, user.getId())
                .execute();

        return user;
    }



    public User getByName(String name) {
        Query query = handle().createQuery("select id, name, description, pwd from user_md where name=?");
        query.bind(0, name);
        query.setMaxRows(1);
        return this.map(query).findOnly();
    }


    public User getByName(int id) {
        Query query = handle().createQuery("select id, name, description, pwd from user_md where id=?");
        query.bind(0, id);
        query.setMaxRows(1);
        return this.map(query).findOnly();

    }

    private ResultIterable<User> map(Query query) {



        return query.map(new RowMapper<User>(){
            @Override
            public User map(ResultSet rs, StatementContext ctx) throws SQLException {
                User user = new User();
                user.setId(rs.getInt(1));
                user.setName(rs.getString(2));
                user.setDescription(rs.getString(3));
                user.setPassword(rs.getString(4));
                return user;
            }


        });
    }

}
