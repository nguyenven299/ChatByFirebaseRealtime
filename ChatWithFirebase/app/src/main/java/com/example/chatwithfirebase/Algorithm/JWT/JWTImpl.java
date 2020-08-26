package com.example.chatwithfirebase.Algorithm.JWT;

import android.util.Base64;

import com.example.chatwithfirebase.Algorithm.DES.ITripleDes;
import com.example.chatwithfirebase.Algorithm.DES.TripleDesImpl;
import com.example.chatwithfirebase.Model.Chat;
import com.example.chatwithfirebase.Model.TripleDesModel;
import com.example.chatwithfirebase.Model.User;
import com.google.gson.Gson;

import java.nio.charset.StandardCharsets;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JWTImpl implements IJWT {
    private static ITripleDes iTripleDes = new TripleDesImpl();
    private static String secretKeyJWT = "khongcochodauNhachUdungcoMAdoihoiNhieuVLoDay.@12256";

    @Override
    public String _encryptJWTUser(User user) throws Exception {
        User userTam = new User();
        userTam.setId(user.getId());
        userTam.setUsername(iTripleDes._encryptTripleDes(user.getUsername()));
        userTam.setPassword(iTripleDes._encryptTripleDes(user.getPassword()));
        userTam.setImageURL(iTripleDes._encryptTripleDes(user.getImageURL()));

        String s = "";
        Gson gson = new Gson();
        String json = "";
        json = gson.toJson(userTam);

        byte[] bytes;
        byte[] base64;
        bytes = json.getBytes(StandardCharsets.UTF_8);
        base64 = Base64.encode(bytes, Base64.DEFAULT);
        String base64String = new String(base64);

        s = Jwts.builder()
                .setSubject("User")
                .claim("userJsonBase64", base64String)
                .signWith(SignatureAlgorithm.HS512, secretKeyJWT.getBytes("UTF-8"))
                .compact();

        return s;
    }

    @Override
    public User _decryptJWTUser(String encryptedText) throws Exception {
        byte[] bytes;
        String json = "";
        Gson gson = new Gson();

        String userJsonBase64 = Jwts.parser()
                .setSigningKey(secretKeyJWT.getBytes("UTF-8"))
                .parseClaimsJws(encryptedText).getBody().get("userJsonBase64", String.class);
        bytes = Base64.decode(userJsonBase64, Base64.DEFAULT);
        json = new String(bytes, StandardCharsets.UTF_8);

        User user = gson.fromJson(json, User.class);
        TripleDesModel tripleDesModelUserName = new TripleDesModel("", user.getUsername());
        TripleDesModel tripleDesModelPassword = new TripleDesModel("", user.getPassword());
        TripleDesModel tripleDesModelImageURL = new TripleDesModel("", user.getImageURL());
        user.setUsername(iTripleDes._decryptTripleDes(user.getUsername()));
        user.setPassword(iTripleDes._decryptTripleDes(user.getPassword()));
        user.setImageURL(iTripleDes._decryptTripleDes(user.getImageURL()));

        return user;
    }

    @Override
    public String _encryptJWTChat(Chat chat) throws Exception {
        Chat chatTam = new Chat();
        chatTam.setMessage(iTripleDes._encryptTripleDes(chat.getMessage()));
        chatTam.setReceiver(chat.getReceiver());
        chatTam.setSender(chat.getSender());

        String s = "";
        Gson gson = new Gson();
        String json = "";
        json = gson.toJson(chatTam);

        byte[] bytes;
        byte[] base64;
        bytes = json.getBytes(StandardCharsets.UTF_8);
        base64 = Base64.encode(bytes, Base64.DEFAULT);
        String base64String = new String(base64);

        s = Jwts.builder()
                .setSubject("Chat")
                .claim("chatJsonBase64", base64String)
                .signWith(SignatureAlgorithm.HS512, secretKeyJWT.getBytes("UTF-8"))
                .compact();

        return s;
    }

    @Override
    public Chat _decryptJWTChat(String encryptedText) throws Exception {
        byte[] bytes;
        String json = "";
        Gson gson = new Gson();

        String chatJsonBase64 = Jwts.parser()
                .setSigningKey(secretKeyJWT.getBytes("UTF-8"))
                .parseClaimsJws(encryptedText).getBody().get("chatJsonBase64", String.class);
        bytes = Base64.decode(chatJsonBase64, Base64.DEFAULT);
        json = new String(bytes, StandardCharsets.UTF_8);

        Chat chat = gson.fromJson(json, Chat.class);
        TripleDesModel tripleDesModel = new TripleDesModel();
        tripleDesModel.setEncrypt(chat.getMessage());
        chat.setMessage(iTripleDes._decryptTripleDes(chat.getMessage()));

        return chat;
    }
}
