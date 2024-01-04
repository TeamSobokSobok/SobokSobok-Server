package io.sobok.SobokSobok.friend.infrastructure;

import io.sobok.SobokSobok.friend.domain.SendFriend;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SendFriendRepository extends JpaRepository<SendFriend, Long> {

}
