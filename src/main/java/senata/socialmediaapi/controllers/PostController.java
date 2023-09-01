package senata.socialmediaapi.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import senata.socialmediaapi.dto.PostsDTO;
import senata.socialmediaapi.entity.Posts;
import senata.socialmediaapi.securety.UserServiceImpl;
import senata.socialmediaapi.sevice.PostService;
import senata.socialmediaapi.entity.User;

@RestController
@RequestMapping("/api/post")
public class PostController {
    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private PostService postService;

    public PostController(UserServiceImpl userService, PostService postService) {
        this.userService = userService;
        this.postService = postService;
    }

    @PostMapping("/create")
    public ResponseEntity<String> createPost( @RequestBody Posts post, Authentication authentication) {
        System.out.println("Заходит");
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You are not authorized to create a post.");
        }
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userService.getUserByUsername(userDetails.getUsername());
        System.out.println("Заходит2"+user.toString());
        post.setUser(user);
        postService.createPost(post);
        return ResponseEntity.ok("Post created successfully.");

    }

    @PutMapping("post/{idpost}")
    public ResponseEntity<String> updatePost(@PathVariable("idpost") Long idpost, @RequestBody Posts updatePost, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You are not authorized.");
        }
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String userName = userDetails.getUsername();
        boolean isPostBelongToUser = postService.isPostBelongToUser(idpost, userName);
        if (!isPostBelongToUser) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not allowed to update this post.");
        }
        boolean success = postService.updatePost(idpost, updatePost);
        if (success) {
            return ResponseEntity.ok("Post updated successfully.");
        } else {
            return ResponseEntity.notFound().build();

        }
    }

    @DeleteMapping("post/{idpost}")
    public ResponseEntity<String> deletePost(@PathVariable("idpost") Long idpost, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You are not authorized to delete the post.");
        }
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        boolean isPostBelongToUser = postService.isPostBelongToUser(idpost, username);
        if (!isPostBelongToUser) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not allowed to delete this post.");
        }
        boolean success = postService.deletePost(idpost);
        if (success) {
            return ResponseEntity.ok("Post deleted successfully.");
        } else {
            return ResponseEntity.notFound().build();
        }

    }

    @GetMapping("post/{idpost}")
    public ResponseEntity<?> getPostbyId(@PathVariable("idpost") Long idpost, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You are not authorized to view the post.");
        }
        PostsDTO postsDTO = postService.getPostById(idpost);
        if (postsDTO != null) {
            return ResponseEntity.ok(postsDTO);

        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
