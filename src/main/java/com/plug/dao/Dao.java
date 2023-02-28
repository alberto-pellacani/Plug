package com.plug.dao;

import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.HandleConsumer;
import org.jdbi.v3.core.Jdbi;

import javax.sql.DataSource;

public abstract class Dao {

    private final Handle handle;



    protected Dao(Handle handle) {
        this.handle = handle;
    }

    protected Handle handle() {
        return this.handle;
    }



}
