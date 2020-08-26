package com.example.chatwithfirebase.Algorithm.Services;

import android.util.Log;

import com.example.chatwithfirebase.Algorithm.DES.ITripleDes;
import com.example.chatwithfirebase.Algorithm.DES.TripleDesImpl;
import com.example.chatwithfirebase.Algorithm.JWT.IJWT;
import com.example.chatwithfirebase.Algorithm.JWT.JWTImpl;
import com.example.chatwithfirebase.Model.Chat;
import com.example.chatwithfirebase.Model.TripleDesModel;
import com.example.chatwithfirebase.Model.User;

public class ServicesImpl implements IServices {
    private ITripleDes iTripleDes = new TripleDesImpl();
    private IJWT ijwt = new JWTImpl();

    @Override
    public String _encryptTripleDes(String message) throws Exception {
        return iTripleDes._encryptTripleDes(message);
    }

    @Override
    public String _decryptTripleDes(String encryptedText) throws Exception {
        return iTripleDes._decryptTripleDes(encryptedText);
    }

    @Override
    public String _encryptJWTUser(User user) throws Exception {
        return ijwt._encryptJWTUser(user);
    }

    @Override
    public User _decryptJWTUser(String encryptedText) throws Exception {
        return ijwt._decryptJWTUser(encryptedText);
    }

    @Override
    public String _encryptJWTChat(Chat chat) throws Exception {
        return ijwt._encryptJWTChat(chat);
    }

    @Override
    public Chat _decryptJWTChat(String encryptedText) throws Exception {
        return ijwt._decryptJWTChat(encryptedText);
    }
}
