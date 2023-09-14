package senata.socialmediaapi.sevice;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import senata.socialmediaapi.component.JwtTokenUtil;
import senata.socialmediaapi.repository.UserRepository;
import senata.socialmediaapi.entity.User;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    UserRepository userRepository;
    @Mock
    JwtTokenUtil jwtUtil;
    @Mock
    BCryptPasswordEncoder bryptPasswordEncoder;
    @InjectMocks
    UserServiceImpl userServiceImpl;

    @Test
    void register_User() {
        User user = new User();
        user.setUsername("Natasha");
        user.setPassword("$2a$12$NVX44mXiqwa8NdKQESgFW.nDYifatR2l3ZloO6OADiwnFSLCYXbqy");
        user.setEmail("nata@gmail.com");
        when(bryptPasswordEncoder.encode("1111")).thenReturn("$2a$12$NVX44mXiqwa8NdKQESgFW.nDYifatR2l3ZloO6OADiwnFSLCYXbqy");
        userServiceImpl.registerUser("Natasha", "1111", "nata@gmail.com");
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void load_User_By_Username() {
        String username="Natasha";
        User user = new User();
        user.setUsername(username);
        user.setPassword("$2a$12$NVX44mXiqwa8NdKQESgFW.nDYifatR2l3ZloO6OADiwnFSLCYXbqy");
        user.setEmail("nata@gmail.com");
        when(userRepository.findByUsername(username)).thenReturn(user);
        UserDetails userDetails=userServiceImpl.loadUserByUsername(username);
        verify(userRepository,times(1)).findByUsername(username);
        assertSame(user,userDetails);
    }

    @Test
    void get_User_By_Username() {
        String username="Natasha";
        User user = new User();
        user.setUsername(username);
        when(userRepository.findByUsername(username)).thenReturn(user);
        userServiceImpl.getUserByUsername(username);
        verify(userRepository,times(1)).findByUsername(username);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void get_User_By_Id() {
        User user = new User();
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        userServiceImpl.getUserById(anyLong());
        verify(userRepository,times(1)).findById(anyLong());
        verifyNoMoreInteractions(userRepository);

    }
}