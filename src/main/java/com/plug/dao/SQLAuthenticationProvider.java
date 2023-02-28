package com.plug.dao;


import antlr.preprocessor.PreprocessorTokenTypes;
import com.plug.auth.*;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.Jdbi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.sql.DataSource;

public class SQLAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private DataSource dataSource;

    @Override
    public AuthenticaionIdentity login(String username, String password) {

        Jdbi jdbi = Jdbi.create(dataSource);
        try {
            jdbi.withHandle(new HandleCallback<UserDao, Exception>() {

                @Override
                public UserDao withHandle(Handle handle) throws Exception {
                    UserDao user = new UserDao(handle);


                    return null;
                }
            });
        } catch (Exception e) {

        }


        return null;
    }

    /*
    public void assertAuthorized(Authorization auth) throws UnauthenticatedException, UnauhorizedException {

    }

     */

    /*
    public UserIdentity login(int tenant, String userName, String password) {
        UserDao dao = new UserDao(this.dataSource);
        User user = dao.getUser(userName, password);
        UserIdentity uid = null;
        if(user!=null) {
            uid = new UserIdentity(dao.getUserTenants(user.getId()), user.getId(), user.getName());
            uid.setTenant(tenant);
        }
        return uid;
    }



    public void assertAuthorized(com.goweb.auth.Authorization auth) throws UnauthenticatedException, com.goweb.auth.UnauhorizedException {

        UserIdentity uid = this.getUserIdentity();
        if(uid==null) {
            throw new UnauthenticatedException();
        }

        boolean hasUser = !(auth.getUser()!=null && auth.getUser().length>0);
        if(!hasUser) {
            for(String user : auth.getUser()) {
                if(user.equalsIgnoreCase(uid.getName())) {
                    hasUser = true;
                }
            }
            if(!hasUser) {
                throw new com.goweb.auth.UnauhorizedException();
            }
        }


        UserDao dao = new UserDao(this.dataSource);

        boolean checkRole = !(auth.getRole()!=null && auth.getRole().length>0);
        if(!checkRole) {
            checkRole = dao.hasRoles(uid.getTenant(), uid.getId(), auth.getRole());
            if(!checkRole) {
                throw new com.goweb.auth.UnauhorizedException();
            }
        }

        boolean checkAuth = !(auth.getAuth()!=null && auth.getAuth().length>0);
        if(!checkAuth) {
            checkAuth = dao.hasAuthorization(uid.getTenant(), uid.getId(), auth.getAuth());
            if(!checkAuth) {
                throw new com.goweb.auth.UnauhorizedException();
            }
        }


    }

    public void sessionLogin(UserIdentity uid) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        HttpSession session = request.getSession(true);
        session.setAttribute(UserIdentity.class.getName(), uid);
        request.setAttribute(UserIdentity.class.getName(), uid);
    }

    public void sessionLogout() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        HttpSession session = request.getSession(false);
        if(session!=null) {
            session.removeAttribute(UserIdentity.class.getName());
        }
        request.removeAttribute(UserIdentity.class.getName());
    }

    public UserIdentity getUserIdentity() {

        UserIdentity uid;

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        // prima cerco nella richiesta
        uid = (UserIdentity)request.getAttribute(UserIdentity.class.getName());
        if(uid!=null) return uid;

        // poi cerco nella sessione
        HttpSession session = request.getSession(false);
        if(session!=null) {
            uid = (UserIdentity)session.getAttribute(UserIdentity.class.getName());
        }

        // poi cerco il token
        if(uid==null) {
            uid = com.goweb.auth.Auth0.parseToken(request);
        }

        request.setAttribute(UserIdentity.class.getName(), uid);

        return uid;


    }
    */


}
