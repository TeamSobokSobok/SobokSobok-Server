package io.sobok.SobokSobok.sticker.infrastructure;

import io.sobok.SobokSobok.sticker.domain.Sticker;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StickerRepository extends JpaRepository<Sticker, Long> {

}
