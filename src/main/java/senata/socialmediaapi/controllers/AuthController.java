package senata.socialmediaapi.controllers;


import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import senata.socialmediaapi.dto.PostsDTO;
import senata.socialmediaapi.dto.UserDTO;
import senata.socialmediaapi.entity.User;
import org.springframework.security.core.userdetails.UserDetails;
import senata.socialmediaapi.component.JwtTokenUtil;
import senata.socialmediaapi.securety.UserServiceImpl;
import senata.socialmediaapi.sevice.PostService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class AuthController {
    @Autowired
    private UserServiceImpl userServiceImpl;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private PostService postService;

    public AuthController(UserServiceImpl userServiceImpl, AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil, PostService postService) {
        this.userServiceImpl = userServiceImpl;
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.postService = postService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody User user) {
        Map<String, Object> map = new HashMap<>();
        try {
            Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
            if (auth.isAuthenticated()) {
                UserDetails userDetails = userServiceImpl.loadUserByUsername(user.getUsername());
                String token = jwtTokenUtil.generateToken(userDetails);
                map.put("error", false);
                map.put("message", "Logged In");
                map.put("token", token);
                return ResponseEntity.ok(map);
            } else {
                map.put("error", true);
                map.put("message", "Invalid Credentials");
                return ResponseEntity.status(401).body(map);
            }
        } catch (DisabledException e) {
            e.printStackTrace();
            map.put("error", true);
            map.put("message", "User is disabled");
           return ResponseEntity.status(500).body(map);
        } catch (BadCredentialsException e) {
            map.put("error", true);
            map.put("message", "Invalid Credentials");
            return ResponseEntity.status(401).body(map);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("error", true);
            map.put("message", "Something went wrong");
            return ResponseEntity.status(500).body(map);
        }
    }
    @PostMapping("/register")
    public ResponseEntity <?> registerUser(@Valid @RequestBody UserDTO user){
        userServiceImpl.registerUser(user.getUsername(), user.getPassword(), user.getEmail());
       return ResponseEntity.ok("User registered successfully");
    }
    @GetMapping("/feed/{userid}")
    public ResponseEntity <List<PostsDTO>> getFeedForUser(@PathVariable ("userid")Long userid,
                                                          @RequestParam(defaultValue = "0") int page,
                                                          @RequestParam(defaultValue = "10")int pagesize){
        List<PostsDTO> activfeed=postService.getActivityFeedUser(userid,page,pagesize);
        return ResponseEntity.ok(activfeed);
    }
}