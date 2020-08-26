package com.example.chatwithfirebase.Model;

//import static com.example.chatwithfirebase.Algorithm.DES.TrippleDes._decrypt;
//import static com.example.chatwithfirebase.Algorithm.DES.TrippleDes.key;

public class User {
    private String id;
    private String username;
    private String imageURL;
    private String password;

    public User() {
    }

    public User(String id, String username, String imageURL, String password) {
        this.id = id;
        this.username = username;
        this.imageURL = imageURL;
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

//    public String getUsername() throws Exception {
//        String userNameDecrypt = _decrypt(username, key);
//        return userNameDecrypt;
//    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

//    public String getImageURL() throws Exception {
//        String imageURLDecrypt = _decrypt(imageURL, key);
//        return imageURLDecrypt;
//    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
