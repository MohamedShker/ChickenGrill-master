package tm.shker.mohamed.chickengrill.Objects;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by mohamed on 30/09/2016.
 * pojo
 */
public class User implements Serializable{
    private String email;
    private String displayName;
    private String phoneNumber;
    //private ArrayList<String> Addresses; // TODO: 17/02/2017 add a list of addresses for every user, and display them as a Suggestions in the address edit text.

    public User() {
    }

    public User(String email) {
        this.email = email;
        this.displayName = email.split("@")[0];
    }

    public User(String email, String phoneNumber) {
        this.email = email;
        this.displayName = email.split("@")[0];
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", displayName='" + displayName + '\'' +
                '}';
    }
}
