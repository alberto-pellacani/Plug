package com.plug.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.plug.dto.UserIdentity;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;


import java.util.Random;

public abstract class Auth0 {

    private static byte[] SECRET_KEY;
    private static int SECRET_KEY_LENGTH = 128;

    protected static final String BEARER_AUTH_HEADER_PREFIX = "Bearer ";

    public static byte[] getSecretKey() {
        if(SECRET_KEY==null) {
            SECRET_KEY = new byte[SECRET_KEY_LENGTH];
            new Random().nextBytes(SECRET_KEY);
        }

        return SECRET_KEY;
    }

    /*
    public static UserIdentity parseToken(HttpServletRequest request)  {
        UserIdentity identity = null;
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith(BEARER_AUTH_HEADER_PREFIX)) {
            String token = authorizationHeader.substring(BEARER_AUTH_HEADER_PREFIX.length());
            identity = parseToken(token);
        }
        return identity;
    }
    */

    /*
    public static UserIdentity parseToken(String token)  {
        UserIdentity identity = null;
        try {
            Algorithm algorithm = Algorithm.HMAC256(getSecretKey());
            JWTVerifier verifier = JWT.require(algorithm).build(); //Reusable verifier instance
            DecodedJWT jwt = verifier.verify(token);

            Integer[] tenants = jwt.getClaim("tns").asArray(Integer.class);
            int[] tns = new int[tenants.length];
            for(int i=0; i<tns.length; i++) {
                tns[i] = tenants[i];
            }

            identity = new UserIdentity(
                    tns,
                    jwt.getClaim("id").asInt(),
                    jwt.getClaim("sub").asString()
            );
            identity.setTenant(jwt.getClaim("tnt").asInt());


        } catch (JWTVerificationException E){
            //Invalid signature/claims
        }
        return identity;
    }
    */


    public static String createToken(UserIdentity identity) {

        String token = null;
        try {
            Algorithm algorithm = Algorithm.HMAC256(getSecretKey());


            token = JWT.create()
                    //.withClaim("tns", identity.getTenants())
                    .withClaim("id",identity.getId())
                    .withClaim("sub",identity.getName())

                    .sign(algorithm);
        } catch (JWTCreationException e){
            //Invalid Signing configuration / Couldn't convert Claims.
            e.printStackTrace();
        }

        return token;
    }




}
