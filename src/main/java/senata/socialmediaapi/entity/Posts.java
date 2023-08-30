package senata.socialmediaapi.entity;

import jakarta.persistence.*;

import java.awt.Image;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="posts")
public class Posts {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "title",nullable = true)
    private String title;
    @Column(name = "text",nullable = true)
    private String text;
    @Column(name = "image",nullable = true)
    private Blob image;

@ManyToOne
@JoinColumn (name="userid")
private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Blob getImage() {
        return image;
    }

    public void setImage(Blob image) {
        this.image = image;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User username) {
        this.user = user;
    }

    public Posts() {
    }
}
