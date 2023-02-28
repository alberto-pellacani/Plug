package com.plug.api.auth;

import com.plug.api.Response;
import com.plug.auth.AuthenticaionIdentity;
import com.plug.auth.AuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class Login {


    @Autowired
    private AuthenticationProvider provider;



    @PostMapping("/login")
    @ResponseBody
    public Response login(@RequestParam("uid") String userId, @RequestParam("pwd") String password  ) {
        System.out.println("---LOGIN----");

        AuthenticaionIdentity ai = this.provider.login(userId,password);


        //UserIdentity uid = this.provider.login(tenant, userId, password);
        //Map map = new HashMap();


        //UserIdentity uid = new UserIdentity("1","Alberto");
        //uid.addTenant(10).addRole("Admin").addRole("Sales");

        //map.put("token", Auth0.createToken(uid));
        /*
        if(uid!=null) {
            map.put("status","ok");

            map.put("identity", uid);

            map.put("token", Auth0.createToken(uid));
        } else {
            map.put("status","error");
        }
        */



        return Response.ok(null);
    }





    /*
    @PostMapping("/reset")
    @ResponseBody
    public ResponseEntity resetPassword(@RequestParam("uid") String userId, @RequestParam("pwd") String password ) {

        UserDao dao = new UserDao(this.dataSource);
        int i = dao.resetPassword(userId,password);

        Map map = new HashMap();
        map.put("uid",userId);
        if(i==1) {
            map.put("status","ok");
        } else {
            map.put("status","error");
        }

        return ResponseEntity.ok(map);

    }
    */

}
