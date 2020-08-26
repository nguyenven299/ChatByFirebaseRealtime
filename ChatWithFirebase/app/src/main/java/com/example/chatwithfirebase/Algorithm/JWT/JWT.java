package com.example.chatwithfirebase.Algorithm.JWT;

import android.util.Base64;

import com.example.chatwithfirebase.Algorithm.DES.TrippleDes;
import com.example.chatwithfirebase.Model.Chat;
import com.example.chatwithfirebase.Model.User;
import com.google.gson.Gson;

import java.nio.charset.StandardCharsets;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JWT {
    // key
    public static String key = "khongcochodauNhachUdungcoMAdoihoiNhieuVLoDay.@12256";

    private User user;
    private Chat chat;

    public JWT(User user) {
        this.user = user;
    }

    public JWT(Chat chat) {
        this.chat = chat;
    }

    public static String _encryptJWTUser(User user) throws Exception {
        User userTam = new User();
        userTam.setId(user.getId());
        userTam.setUsername(TrippleDes._encrypt(user.getUsername(), key));
        userTam.setPassword(TrippleDes._encrypt(user.getPassword(), key));
        userTam.setImageURL(TrippleDes._encrypt(user.getImageURL(), key));

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
                .signWith(SignatureAlgorithm.HS512, key.getBytes("UTF-8"))
                .compact();

        return s;
    }

    public static User _decryptJWTUser(String encryptedText) throws Exception {
        byte[] bytes;
        String json = "";
        Gson gson = new Gson();

        String userJsonBase64 = Jwts.parser()
                .setSigningKey(key.getBytes("UTF-8"))
                .parseClaimsJws(encryptedText).getBody().get("userJsonBase64", String.class);
        bytes = Base64.decode(userJsonBase64, Base64.DEFAULT);
        json = new String(bytes, StandardCharsets.UTF_8);

        User user = gson.fromJson(json, User.class);
        user.setUsername(TrippleDes._decrypt(user.getUsername(), key));
        user.setPassword(TrippleDes._decrypt(user.getPassword(), key));
        user.setImageURL(TrippleDes._decrypt(user.getImageURL(), key));

        return user;
    }

    public static String _encryptJWTChat(Chat chat) throws Exception {
        Chat chatTam = new Chat();
        chatTam.setMessage(TrippleDes._encrypt(chat.getMessage(), key));
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
                .signWith(SignatureAlgorithm.HS512, key.getBytes("UTF-8"))
                .compact();

        return s;
    }

    public static Chat _decryptJWTChat(String encryptedText) throws Exception {
        byte[] bytes;
        String json = "";
        Gson gson = new Gson();

        String chatJsonBase64 = Jwts.parser()
                .setSigningKey(key.getBytes("UTF-8"))
                .parseClaimsJws(encryptedText).getBody().get("chatJsonBase64", String.class);
        bytes = Base64.decode(chatJsonBase64, Base64.DEFAULT);
        json = new String(bytes, StandardCharsets.UTF_8);

        Chat chat = gson.fromJson(json, Chat.class);
        chat.setMessage(TrippleDes._decrypt(chat.getMessage(), key));

        return chat;
    }

}
