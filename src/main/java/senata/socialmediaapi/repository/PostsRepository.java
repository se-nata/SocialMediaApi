package senata.socialmediaapi.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import senata.socialmediaapi.entity.Posts;


import java.util.Collection;
import java.util.List;


public interface PostsRepository extends JpaRepository<Posts,Long> {
    List<Posts> findAllByUserId(Long userId);
    List<Posts> findByUserIdIn(Collection<Long> userIds, Pageable  pageable) ;
}
