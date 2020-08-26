package com.example.chatwithfirebase.Model;

public class TripleDesModel {
    private String decrypt;
    private String encrypt;

    public TripleDesModel() {
    }

    public String getDecrypt() {
        return decrypt;
    }

    public void setDecrypt(String decrypt) {
        this.decrypt = decrypt;
    }

    public String getEncrypt() {
        return encrypt;
    }

    public void setEncrypt(String encrypt) {
        this.encrypt = encrypt;
    }

    public TripleDesModel(String decrypt, String encrypt) {
        this.decrypt = decrypt;
        this.encrypt = encrypt;
    }
}
