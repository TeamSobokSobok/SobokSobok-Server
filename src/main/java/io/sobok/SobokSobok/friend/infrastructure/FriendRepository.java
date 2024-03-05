package io.sobok.SobokSobok.friend.infrastructure;

import io.sobok.SobokSobok.friend.domain.Friend;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FriendRepository extends JpaRepository<Friend, Long> {

    // READ
    List<Friend> findAllBySenderId(Long senderId);

    Integer countBySenderId(Long senderId);

    // DELETE
    void deleteAllBySenderIdOrReceiverId(Long senderId, Long receiverId);
}
