package io.sobok.SobokSobok.notice.infrastructure;

import io.sobok.SobokSobok.notice.domain.Notice;
import io.sobok.SobokSobok.notice.domain.NoticeStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NoticeRepository extends JpaRepository<Notice, Long> {

    // READ
    Boolean existsBySenderIdAndReceiverIdAndIsOkay(Long senderId, Long receiverId, NoticeStatus isOkay);
}
