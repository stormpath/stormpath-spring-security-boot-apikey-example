package com.stormpath.spring.boot.examples.controller;

import com.stormpath.sdk.account.Account;
import com.stormpath.sdk.api.ApiKey;
import com.stormpath.sdk.application.Application;
import com.stormpath.sdk.authc.AuthenticationRequest;
import com.stormpath.sdk.authc.AuthenticationResult;
import com.stormpath.sdk.authc.UsernamePasswordRequests;
import com.stormpath.spring.boot.examples.model.LoginRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
public class AppController {

    @Autowired
    Application application;

    @RequestMapping(path = "/newApiKey", method = POST)
    public @ResponseBody Map<String, String> newApiKey(@RequestBody LoginRequest loginRequest) {
        Map<String, String> ret = new HashMap<>();
        ret.put("STATUS", "SUCCESS");
        ret.put("msg", "This is for testing purposes only!");

        AuthenticationRequest authReq = UsernamePasswordRequests.builder()
            .setUsernameOrEmail(loginRequest.getEmail())
            .setPassword(loginRequest.getPassword())
            .build();

        AuthenticationResult result = application.authenticateAccount(authReq);

        if (result == null || result.getAccount() == null) {
            ret.put("STATUS", "FAILURE");
            ret.put("msg", "Couldn't authenticate: " + loginRequest.getEmail());
            return ret;
        }

        Account account = result.getAccount();
        ApiKey newKey = account.createApiKey();
        ret.put("keyID", newKey.getId());
        ret.put("keySecret", newKey.getSecret());

        return ret;
    }

    @RequestMapping("/restricted")
    public String restricted() {
        return "You must have authenticated, or you wouldn't be here.";
    }
}
