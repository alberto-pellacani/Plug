package com.plug.dto;

import java.io.Serializable;

public class UserIdentity implements Serializable {

    private final String name;
    private final String id;

    private final int[] tenants ;

    public UserIdentity( String id,String name, int[] tenants) {
        this.name = name;
        this.id = id;
        this.tenants = tenants;
    }

    public int[] getTenants() {
        return tenants;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }
}
