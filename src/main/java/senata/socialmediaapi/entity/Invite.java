package senata.socialmediaapi.entity;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name="invitation")
public class Invite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;



    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn (name = "sender",nullable = false)
    private  User sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn (name = "receiver",nullable = false)
    private  User receiver;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "status")
    private InviteStatus status;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Invite invite)) return false;
        return Objects.equals(getSender(), invite.getSender()) && Objects.equals(getReceiver(), invite.getReceiver()) && getStatus() == invite.getStatus();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSender(), getReceiver(), getStatus());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public InviteStatus getStatus() {
        return status;
    }

    public void setStatus(InviteStatus status) {
        this.status = status;
    }

    public Invite() {
    }
}
