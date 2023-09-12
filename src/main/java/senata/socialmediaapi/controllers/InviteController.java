package senata.socialmediaapi.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import senata.socialmediaapi.sevice.UserServiceImpl;
import senata.socialmediaapi.sevice.InviteService;
import senata.socialmediaapi.entity.User;

import java.util.Optional;

@RestController
@RequestMapping("/invite")
public class InviteController {
    @Autowired
    private InviteService inviteService;
    @Autowired
    private UserServiceImpl userServiceImpl;

    public InviteController(InviteService inviteService, UserServiceImpl userServiceImpl) {
        this.inviteService = inviteService;
        this.userServiceImpl = userServiceImpl;
    }

    @PostMapping("/send/{idreceiver}")
    public ResponseEntity<String> sendRequest(@AuthenticationPrincipal UserDetails userDetails,
                                              @PathVariable Long idreceiver) {
        String senderuser = userDetails.getUsername();
        Optional<User> receiverOptional = userServiceImpl.getUserById(idreceiver);
        if (receiverOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        User sender = userServiceImpl.getUserByUsername(senderuser);
        User receiver = receiverOptional.get();
        inviteService.sendRequest(sender, receiver);
        return ResponseEntity.ok("Friendship request sent.");
    }
    @PostMapping("/accept/{idinvite}")
    public ResponseEntity<String>acceptRequest(@AuthenticationPrincipal UserDetails userDetails,
                                               @PathVariable Long idinvite){
        String receiveruser=userDetails.getUsername();
        User receiver=userServiceImpl.getUserByUsername(receiveruser);
        inviteService.acceptRequest(idinvite,receiver);
        return  ResponseEntity.ok("Friendship request accept.");
    }
    @PostMapping("/reject/{idinvite}")
    public ResponseEntity<String> rejectRequest(@AuthenticationPrincipal UserDetails userDetails,
                                                @PathVariable  Long idinvite){
        String receiveruser=userDetails.getUsername();
     inviteService.rejectRequest(idinvite,receiveruser);
     return ResponseEntity.ok("Friendship request reject.");
    }
    @PostMapping("/cancel/{idreceiver}")
    public ResponseEntity<String> cancelRequest(@AuthenticationPrincipal UserDetails userDetails,
                                                @PathVariable Long idreceiver){
        String senderuser=userDetails.getUsername();
        User sender=userServiceImpl.getUserByUsername(senderuser);
        User receiver=userServiceImpl.getUserById(idreceiver).orElse(null);
        if (receiver==null){
            return  ResponseEntity.notFound().build();
        }
        inviteService.cancelRequest(sender,receiver);
        return ResponseEntity.ok("Friend request successfully canceled.");
    }
    @DeleteMapping("/remove/{idfriend}")
    public ResponseEntity<String> removeFriend(@AuthenticationPrincipal UserDetails userDetails,
                                               @PathVariable Long idfriend){
        String senderuser=userDetails.getUsername();
        User sender=userServiceImpl.getUserByUsername(senderuser);
        User friend=userServiceImpl.getUserById(idfriend).orElse(null);
        if (friend==null){
            return ResponseEntity.notFound().build();
        }
        inviteService.removeFriend(sender,friend);
        return ResponseEntity.ok("Friend removed successfully.");
    }


}
