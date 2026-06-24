package com.microservice.user.service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;

public class Main {

    public static void main(String[] args) throws Exception {

        String password = "pw1773349257";

        MessageDigest digest = MessageDigest.getInstance("SHA-256");

        byte[] hash = digest.digest(
                password.getBytes(StandardCharsets.UTF_8));

        String token = Base64.getEncoder().encodeToString(hash);

        System.out.println(token);
    }
}