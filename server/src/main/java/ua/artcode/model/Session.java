package ua.artcode.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Created by zhenia on 19.05.17.
 */
@Entity
@Table(name = "session")
public class Session {
    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @GeneratedValue(generator = "uuid")
    @Column(unique = true)
    private String key;
    @OneToOne
    private User user;

    public Session() {
    }

    public Session(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getAccessKey() {
        return key;
    }

    public void setAccessKey(String accessKey) {
        this.key = accessKey;
    }
}
