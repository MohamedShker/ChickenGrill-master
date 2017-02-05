package tm.shker.mohamed.chickengrill.Objects;

import java.io.Serializable;

/**
 * Created by mohamed on 30/09/2016.
 * pojo
 */
public class User implements Serializable{
    private String email;
    private String displayName;

    public User() {
    }

    public User(String email, String displayName) {
        this.email = email;
        this.displayName = displayName;
    }

    public User(String email) {
        this.email = email;
        this.displayName = email.split("@")[0];
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

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", displayName='" + displayName + '\'' +
                '}';
    }
}
