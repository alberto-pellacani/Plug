package com.plug.dao;

import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.HandleConsumer;
import org.jdbi.v3.core.transaction.TransactionIsolationLevel;

public class TransactionDao extends Dao {



    public TransactionDao(Dao dao) {
        super(dao.handle());
    }

    public TransactionDao(Handle handle) {
        super(handle);

    }

    public static TransactionDao create(Handle handle) {
        return new TransactionDao(handle);
    }

    public static TransactionDao create(Dao dao) {
        return new TransactionDao(dao);
    }


    public <X extends Exception> void useTransaction(final HandleConsumer<X> callback) throws X {
        if(this.handle().isInTransaction()) {
            callback.useHandle(this.handle());
        } else {
            this.handle().useTransaction(callback);
        }
    }

    public  <R, X extends Exception> R inTransaction(HandleCallback<R, X> callback) throws X {
        if(this.handle().isInTransaction()) {
            return callback.withHandle(this.handle());
        } else {
            return this.handle().inTransaction(callback);
        }


    }

    public <R, X extends Exception> R inTransaction(TransactionIsolationLevel level, HandleCallback<R, X> callback) throws X {
        if(this.handle().isInTransaction() && this.handle().getTransactionIsolationLevel().equals(level)) {
            return callback.withHandle(this.handle());
        } else {
            return this.handle().inTransaction(level,callback);
        }
    }

}
