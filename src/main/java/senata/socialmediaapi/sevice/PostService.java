package senata.socialmediaapi.sevice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import senata.socialmediaapi.dto.PostsDTO;
import senata.socialmediaapi.dto.UserDTO;
import senata.socialmediaapi.entity.Posts;
import senata.socialmediaapi.repository.PostsRepository;
import senata.socialmediaapi.entity.User;
import senata.socialmediaapi.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


@Service
public class PostService {
    @Autowired
    private PostsRepository postsRepository;
    @Autowired
    private UserRepository userRepository;

    public PostService(PostsRepository postsRepository, UserRepository userRepository) {
        this.postsRepository = postsRepository;
        this.userRepository = userRepository;
    }

    public  void createPost(Posts post){
        postsRepository.save(post);
    }
    public List<PostsDTO> getallUserPosts(Long userid){
        List<Posts> listpost=postsRepository.findAllByUserId(userid);
        List<PostsDTO> listdto=new ArrayList<>();
        for(Posts p:listpost){
            PostsDTO postdto=new PostsDTO();
            postdto.setId(p.getId());
            postdto.setTitle(p.getTitle());
            postdto.setText(p.getText());
            postdto.setImage(p.getImage());

            listdto.add(postdto);
        }
        return listdto;

    }
    public PostsDTO getPostById(Long postid){
       Optional <Posts> optionalPpost=postsRepository.findById(postid);
       if(optionalPpost.isPresent()){
           Posts posts=optionalPpost.get();

           PostsDTO postdto=new PostsDTO();
           postdto.setId(posts.getId());
           postdto.setText(posts.getText());
           postdto.setTitle(posts.getTitle());
           postdto.setImage(posts.getImage());
           return  postdto;
       }
return  null;
    }
    public  boolean updatePost(Long idpost,Posts updatepost){
        Optional <Posts> optionalpost=postsRepository.findById(idpost);
        if(optionalpost.isPresent()){
            Posts posts=optionalpost.get();
            posts.setTitle(updatepost.getTitle());
            posts.setText(updatepost.getText());
            posts.setImage(updatepost.getImage());
            postsRepository.save(posts);
            return true;
        }
        else {
            return false;
        }
    }
    public boolean deletePost(Long idpost){
        Optional <Posts> optionalPost=postsRepository.findById(idpost);
        if(optionalPost.isPresent()){
            postsRepository.deleteById(idpost);
            return  true;
        }
        else {
            return  false;
        }
    }
    public  List <Posts> getPostsByUsers(Set<User> users, int page, int pageSize){
        Set<Long> userIds=users.stream().map(User::getId).collect(Collectors.toSet());
        Sort sort=Sort.by(Sort.Direction.DESC,"id");
        Pageable pageable= PageRequest.of(page,pageSize,sort);
        return  postsRepository.findByUserIdIn(userIds,pageable);
    }
    public  boolean isPostBelongToUser (Long idpost, String username){
        Posts post=postsRepository.findById(idpost).orElse(null);
        if(post==null){
            return false;
        }
       User user=post.getUser();
        return user.getUsername().equals(username);

    }
    private PostsDTO convertToPostDto(Posts post) {
        PostsDTO postDto = new PostsDTO();
        postDto.setId(post.getId());
        postDto.setText(post.getText());
        postDto.setTitle(post.getTitle());

        UserDTO userDto = new UserDTO();
        userDto.setId(post.getUser().getId());
        userDto.setUsername(post.getUser().getUsername());
        userDto.setEmail(post.getUser().getEmail());

        postDto.setUser(userDto);

        return postDto;
    }
    public  List<PostsDTO> getActivityFeedUser(Long iduser,int page, int pageSize){
        User user=userRepository.findById(iduser).orElseThrow(()-> new IllegalArgumentException("User not found"));
        Set<User> subscriptions=user.getSubscribers();
        List<Posts>  posts=getPostsByUsers(subscriptions,page,pageSize);
    return posts.stream().map(this::convertToPostDto).collect(Collectors.toList());
    }



}
