package com.example.chatwithfirebase.Algorithm.DES;

import com.example.chatwithfirebase.Model.TripleDesModel;

public interface ITripleDes {
    public String _encryptTripleDes(String message) throws Exception;

    public String _decryptTripleDes(String encryptedText) throws Exception;
}
