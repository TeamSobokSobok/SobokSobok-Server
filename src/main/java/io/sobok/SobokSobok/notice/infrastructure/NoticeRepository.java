package io.sobok.SobokSobok.notice.infrastructure;

import io.sobok.SobokSobok.notice.domain.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoticeRepository extends JpaRepository<Notice, Long> {

    // READ
    List<Notice> findByReceiverId(Long receiverId);
}
