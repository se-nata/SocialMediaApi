package senata.socialmediaapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import senata.socialmediaapi.entity.Invite;
import senata.socialmediaapi.entity.User;

public interface InviteRepository extends JpaRepository<Invite,Long> {
    @Query("SELECT i FROM Invite i WHERE i.from.id=:from AND i.to.id=:to")
    Invite findByFromTo(@Param("from") User from, @Param("to") User to);
}
