package com.iamhoppy.hoppy;

import java.io.Serializable;

/* User class */
public class User implements Serializable{
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
