package com.example.chatwithfirebase.Algorithm.Services;

import com.example.chatwithfirebase.Model.Chat;
import com.example.chatwithfirebase.Model.TripleDesModel;
import com.example.chatwithfirebase.Model.User;

public interface IServices {
    public String _encryptTripleDes(String message) throws Exception;

    public String _decryptTripleDes(String encryptedText) throws Exception;

    public String _encryptJWTUser(User user) throws Exception;

    public User _decryptJWTUser(String encryptedText) throws Exception;

    public String _encryptJWTChat(Chat chat) throws Exception;

    public Chat _decryptJWTChat(String encryptedText) throws Exception;
}
