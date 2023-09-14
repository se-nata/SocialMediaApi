package senata.socialmediaapi.sevice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import senata.socialmediaapi.component.JwtTokenUtil;
import senata.socialmediaapi.repository.UserRepository;
import senata.socialmediaapi.entity.User;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserDetailsService {
    @Autowired
    private final JwtTokenUtil jwtUtil;
    @Autowired
    UserRepository userRepository;

    @Autowired
    BCryptPasswordEncoder  bryptPasswordEncoder;

    public UserServiceImpl(JwtTokenUtil jwtUtil, UserRepository userRepository, BCryptPasswordEncoder bryptPasswordEncoder) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        this.bryptPasswordEncoder = bryptPasswordEncoder;
    }

    public void registerUser(String username, String password, String email) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(bryptPasswordEncoder.encode(password));
        user.setEmail(email);
        userRepository.save(user);
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return user;
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    public Optional<User> getUserById(Long userId) {
        return userRepository.findById(userId);
    }
 }