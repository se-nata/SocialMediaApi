package senata.socialmediaapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import senata.socialmediaapi.entity.Invite;
import senata.socialmediaapi.entity.User;

public interface InviteRepository extends JpaRepository<Invite,Long> {

    Invite findBySenderAndReceiver(User sender, User receiver);
}
