package com.example.chatwithfirebase.Model;

public class UploadImage  {
    private String ImageURL;
private String URL;

    public UploadImage(String imageURL, String URL) {
        ImageURL = imageURL;
        this.URL = URL;
    }

    public UploadImage() {
    }

    public String getImageURL() {
        return ImageURL;
    }

    public void setImageURL(String imageURL) {
        ImageURL = imageURL;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }
}
