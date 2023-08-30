package senata.socialmediaapi.entity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name ="user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;
    @Column(name="username",nullable = false)
    @Size(min = 2,message = "Не меньше 2 знаков")
    private String username;
    @Column(name="email",unique = true, nullable = false)
    @Size(min = 5,message = "Не меньше 5 знаков")
    private String email;
    @Column(name="password",nullable = false)
    @Size(min = 5,message = "Не меньше 5 знаков")
    private String password;


    @ManyToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinTable(name="friendships",joinColumns = @JoinColumn(name = "userid"),inverseJoinColumns = @JoinColumn(name="friendid"))
    private Set<User> friends=new HashSet<>();
    @ManyToMany(mappedBy = "friends", fetch = FetchType.LAZY)
    private Set<User> subscribers = new HashSet<>();

    public Set<User> getSubscribers() {
        return subscribers;
    }

    public void setSubscribers(Set<User> subscribers) {
        this.subscribers = subscribers;
    }

    public User() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<User> getFriends() {
        return friends;
    }

    public void setFriends(Set<User> friends) {
        this.friends = friends;
    }
}
