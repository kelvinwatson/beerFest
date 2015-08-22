package com.iamhoppy.hoppy;

/**
 * Created by Bryce on 8/22/2015.
 */
public class User {
    private int id;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    private String firstName;
    private String lastName;

    public String getFacebookCredential() {
        return facebookCredential;
    }

    public void setFacebookCredential(String facebookCredential) {
        this.facebookCredential = facebookCredential;
    }

    private String facebookCredential;
}
