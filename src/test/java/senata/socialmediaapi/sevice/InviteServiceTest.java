package senata.socialmediaapi.sevice;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import senata.socialmediaapi.entity.Invite;
import senata.socialmediaapi.entity.InviteStatus;
import senata.socialmediaapi.repository.InviteRepository;
import senata.socialmediaapi.repository.UserRepository;
import senata.socialmediaapi.entity.User;
import java.util.HashSet;
import java.util.Set;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InviteServiceTest {

    @Mock
    InviteRepository inviteRepository;
    @Mock
    UserRepository userRepository;
    @InjectMocks
    InviteService inviteService;

    @Test
    void send_Request() {
        Set <User> subscribers= new HashSet<>();
        User sender = new User();
        sender.setSubscribers(subscribers);
        sender.setUsername("Natasha");
        User receiver = new User();
        receiver.setUsername("Dima");
        Invite invite = new Invite();
        invite.setSender(sender);
        invite.setReceiver(receiver);
        invite.setStatus(InviteStatus.PENDING);
        sender.getSubscribers().add(receiver);
        when(userRepository.findByUsername("Natasha")).thenReturn(sender);
        when(userRepository.findByUsername("Dima")).thenReturn(receiver);
        inviteService.sendRequest(sender,receiver);
        verify(inviteRepository, times(1)).save(invite);
        verifyNoMoreInteractions(inviteRepository);
    }

    @Test
    void acceptRequest() {

    }

    @Test
    void rejectRequest() {
    }

    @Test
    void removeFriend() {
    }

    @Test
    void cancelRequest() {
    }
}