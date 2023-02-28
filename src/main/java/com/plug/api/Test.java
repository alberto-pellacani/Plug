package com.plug.api;

import com.plug.dao.*;
import com.plug.dto.Tenant;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.HandleConsumer;
import org.jdbi.v3.core.Jdbi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;

@RestController
public class Test {

    @Autowired
    private DataSource dataSource;

    @GetMapping("/test")
    public String test() {

        try {

            Jdbi jdbi = Jdbi.create(this.dataSource);
            jdbi.useTransaction(new HandleConsumer<Exception>() {
                @Override
                public void useHandle(Handle handle) throws Exception {

                    Tenant t1 = new Tenant();
                    t1.setName("OT Cosulting");

                    Tenant t2 = new Tenant();
                    t2.setName("OT Group");

                    TenantDao dao = new TenantDao(handle);
                    dao.insert(t1);
                    dao.insert(t2);

                }
            });




        } catch (Exception e) {
            e.printStackTrace();
        }


        return "Hello !";
    }

}
