package io.sobok.SobokSobok.notice.infrastructure;

import io.sobok.SobokSobok.notice.domain.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice, Long> {

}
