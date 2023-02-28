package com.plug.auth;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;


import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SecurityFilter implements Filter {


    private Map<String,List<AuthorizationPattern>> patterns = new HashMap<>();

    private static final String[] DEFAULT_METHODS = {"GET","POST","PUT","DELETE"};


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("FILTER - INITIALIZE");
        this.loadJson();

    }

    private void loadJson() {
        InputStream in = SecurityFilter.class.getResourceAsStream("/authorization.json");
        if(in!=null) {
            try {

                ObjectMapper mapper = new ObjectMapper();
                List<AuthorizationPattern> patterns = mapper.readValue(in, new TypeReference<List<AuthorizationPattern>>(){});
                this.loadPatterns(patterns);

                in.close();

            } catch (IOException e) {
                e.printStackTrace();
            } finally {

            }

        }
    }

    private void loadPatterns(List<AuthorizationPattern> patterns) {

        for(AuthorizationPattern ap : patterns) {

            String[] methods = ap.getMethod()==null || ap.getMethod().length==0 ? DEFAULT_METHODS : ap.getMethod();
            for(String m : methods) {
                m=m.toUpperCase();
                List<AuthorizationPattern> p = this.patterns.get(m);
                if(p==null) {
                    p = new ArrayList<>();
                    this.patterns.put(m,p);
                }

                p.add(ap);

            }
        }
    }



    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest)request;


        System.out.println("--->" + req.getServletPath());
        List<AuthorizationPattern> list = this.patterns.get(req.getMethod());
        if(list!=null) {
            for(AuthorizationPattern p : list) {
                if(p.getPattern().matcher(req.getServletPath()).matches()) {
                    System.out.println(p);
                }
            }
        }


        try {
            chain.doFilter(request, response);


        } catch (Exception e) {
            if(this.findCause(e, UnauhorizedException.class)) {
                System.out.println("NON AUTORIZZATO");
            } else if(this.findCause(e, UnauthenticatedException.class)) {
                System.out.println("NON AUTENTICATO");
            } else {
                throw e;
            }
        }

    }

    private boolean findCause(Throwable ex,  Class cause) {

        while(ex!=null && !cause.isAssignableFrom(ex.getClass())) {
            ex = ex.getCause();
        }
        
        return ex!=null;
    }




    @Override
    public void destroy() {

    }



}
