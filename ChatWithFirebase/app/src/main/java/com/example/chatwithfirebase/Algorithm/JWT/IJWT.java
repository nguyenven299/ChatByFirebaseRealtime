package com.example.chatwithfirebase.Algorithm.JWT;

import com.example.chatwithfirebase.Model.Chat;
import com.example.chatwithfirebase.Model.User;

public interface IJWT {
    public String _encryptJWTUser(User user) throws Exception;

    public User _decryptJWTUser(String encryptedText) throws Exception;

    public String _encryptJWTChat(Chat chat) throws Exception;

    public Chat _decryptJWTChat(String encryptedText) throws Exception;
}
