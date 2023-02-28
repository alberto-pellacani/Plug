package com.plug.auth;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Enumeration;

public class Tenant {

    private  String name;

    @Autowired
    private HttpServletRequest request;

    public Tenant(String name) {
        this.name = name;
    }

    public Tenant() {

    }

    public String getName() {

        if(this.name==null) {

            String host = this.request.getHeader("host");
            System.out.println("=>" + host);

            if(host==null) {
                host = "default";
            } else {

                int i = host.lastIndexOf(':');
                host = i>0 ? host.substring(0,i).trim() : host;


            }


            System.out.println("->" + this.request.getRemoteHost());

            Enumeration<String> names = this.request.getHeaderNames();
            while(names.hasMoreElements()) {
                String n= names.nextElement();
                System.out.println(n + ":" + this.request.getHeader(n));
            }



        }

        return this.name;




    }


    @Override
    public String toString() {
        return this.getName();
    }
}
