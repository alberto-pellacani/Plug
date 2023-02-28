package com.plug.auth;


import com.plug.dao.SQLAuthenticationProvider;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
public class AuthorizeAspect {


    @Autowired
    private SQLAuthenticationProvider provider;

    @Around("@annotation(Authorize)")
    public Object authorizeRequest(ProceedingJoinPoint joinPoint) throws Throwable {


        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Class<?> clazz = methodSignature.getDeclaringType();
        Method method = clazz.getDeclaredMethod(methodSignature.getName(),methodSignature.getParameterTypes());
        Authorize auth = method.getAnnotation(Authorize.class);


        Authorization a = new Authorization();
        a.setAuth(auth.auth());
        a.setRole(auth.role());
        a.setUser(auth.user());

        //this.provider.assertAuthorized(a);



        return joinPoint.proceed();

    }


}
