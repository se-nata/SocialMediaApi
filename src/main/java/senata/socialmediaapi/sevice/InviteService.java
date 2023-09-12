package senata.socialmediaapi.sevice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import senata.socialmediaapi.entity.Invite;
import senata.socialmediaapi.entity.InviteStatus;
import senata.socialmediaapi.repository.InviteRepository;
import senata.socialmediaapi.entity.User;
import senata.socialmediaapi.repository.UserRepository;

@Service
public class InviteService {
    @Autowired
    private InviteRepository inviteRepository;
    @Autowired
    private UserRepository userRepository;

    public InviteService(InviteRepository inviteRepository, UserRepository userRepository) {
        this.inviteRepository = inviteRepository;
        this.userRepository = userRepository;
    }

    public void sendRequest(User senderuser, User receiveruser) {
        User sender = userRepository.findByUsername(senderuser.getUsername());
        User receiver = userRepository.findByUsername(receiveruser.getUsername());
        if (senderuser == null || receiver == null) {
            throw new IllegalStateException("Sender or receiver does not exist");
        }
        Invite invite = new Invite();
        invite.setSender(sender);
        invite.setReceiver(receiver);
        invite.setStatus(InviteStatus.PENDING);
        sender.getSubscribers().add(receiver);
        userRepository.save(sender);
        inviteRepository.save(invite);
    }

    public void acceptRequest(Long idinvite, User receiver) {
        Invite invate = inviteRepository.findById(idinvite).orElse(null);
        if (invate == null || invate.getStatus() != InviteStatus.PENDING || !invate.getReceiver().equals(receiver)) {
            throw new IllegalStateException("Invalid request");
        }
        User sender = invate.getSender();
        receiver.getFriends().add(sender);
        sender.getFriends().add(receiver);

        userRepository.save(sender);
        userRepository.save(receiver);

        invate.setStatus(InviteStatus.INFRIENDS);
        inviteRepository.save(invate);
    }

    public void rejectRequest(Long idinvite, String receiver) {
        Invite invite = inviteRepository.findById(idinvite).orElse(null);
        if (invite == null || invite.getStatus() != InviteStatus.PENDING) {
            System.out.println(" getStatus(" + invite.getStatus().toString());
            throw new IllegalStateException("Invalid request.");
        }
        User username = userRepository.findByUsername(receiver);
        if (!invite.getSender().equals(username)) {
            throw new IllegalStateException("You cannot reject this request");
        }
        invite.setStatus(InviteStatus.REJECTED);
        inviteRepository.save(invite);
    }

    public void removeFriend(User username, User removefriend) {
        username.getFriends().remove(removefriend);
        username.getSubscribers().remove(removefriend);

        removefriend.getFriends().remove(username);

        userRepository.save(username);
        userRepository.save(removefriend);

    }

    public void cancelRequest(User sender, User receiver) {
        Invite invite = inviteRepository.findBySenderAndReceiver(sender, receiver);
        if (invite == null || invite.getStatus() != InviteStatus.PENDING) {
            throw new IllegalStateException("Invalid friend request");
        }
        inviteRepository.delete(invite);
        sender.getSubscribers().remove(receiver);
        userRepository.save(sender);
    }
}
