package senata.socialmediaapi.sevice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import senata.socialmediaapi.entity.Invite;
import senata.socialmediaapi.entity.InviteStatus;
import senata.socialmediaapi.exceptions.InviteNotFoundException;
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
        User sender = userRepository.findByUsername(senderuser.getUsername()).orElseThrow(()-> new UsernameNotFoundException("User not found"));
        User receiver = userRepository.findByUsername(receiveruser.getUsername()).orElseThrow(()-> new UsernameNotFoundException("User not found"));
        if (sender == null || receiver == null) {
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
        Invite invite = inviteRepository.findById(idinvite).orElse(null);
        if (invite == null || invite.getStatus() != InviteStatus.PENDING || !invite.getReceiver().equals(receiver)) {
            throw new IllegalStateException("Invalid request");
        }
        User sender = invite.getSender();
        receiver.getFriends().add(sender);
        sender.getFriends().add(receiver);

        userRepository.save(sender);
        userRepository.save(receiver);

        invite.setStatus(InviteStatus.INFRIENDS);
        inviteRepository.save(invite);
    }

    public void rejectRequest(Long idinvite, String receiver) {
        Invite invite = inviteRepository.findById(idinvite).orElseThrow(()->new InviteNotFoundException("Invite not found"));
        if ( invite.getStatus() != InviteStatus.PENDING) {
            throw new IllegalStateException("Invalid request.");
        }
        User user= userRepository.findByUsername(receiver).orElseThrow(()->new UsernameNotFoundException("User not found"));
        if (!invite.getSender().equals(user)) {
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
