package senata.socialmediaapi.entity;

import jakarta.persistence.*;

@Entity
@Table(name="invitation")
public class Invite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn (name = "from",nullable = false)
    private  User from;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn (name = "to",nullable = false)
    private  User to;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "status")
    private InviteStatus status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getFrom() {
        return from;
    }

    public void setFrom(User from) {
        this.from = from;
    }

    public User getTo() {
        return to;
    }

    public void setTo(User to) {
        this.to = to;
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
