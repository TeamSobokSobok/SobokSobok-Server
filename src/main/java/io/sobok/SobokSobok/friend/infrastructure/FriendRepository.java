package io.sobok.SobokSobok.friend.infrastructure;

import io.sobok.SobokSobok.friend.domain.Friend;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FriendRepository extends JpaRepository<Friend, Long> {

    Boolean existsBySenderIdAndReceiverId(Long senderId, Long receiverId);
}
