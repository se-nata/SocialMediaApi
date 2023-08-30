package senata.socialmediaapi.securety;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import senata.socialmediaapi.component.JwtTokenUtil;
import senata.socialmediaapi.repository.UserRepository;
import senata.socialmediaapi.entity.User;
import java.util.ArrayList;
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

    public void registerUser(String username, String email, String password) {
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(bryptPasswordEncoder.encode(password));
        userRepository.save(user);
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.getUserByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), new ArrayList<>());
    }
    public String login(String username, String password) {
        UserDetails userDetails = loadUserByUsername(username);
        return jwtUtil.generateToken(userDetails);
    }

    public User getUserByUsername(String username) {
        return userRepository.getUserByUsername(username);
    }
    public Optional<User> getUserById(Long userId) {
        return userRepository.findById(userId);
    }
 }