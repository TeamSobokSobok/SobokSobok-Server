package io.sobok.SobokSobok.pill.application;

import io.sobok.SobokSobok.exception.ErrorCode;
import io.sobok.SobokSobok.exception.model.NotFoundException;
import io.sobok.SobokSobok.pill.infrastructure.PillRepository;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PillServiceUtil {

    public static void existsPillById(PillRepository pillRepository, Long id) {
        if (!pillRepository.existsById(id)) {
            throw new NotFoundException(ErrorCode.UNREGISTERED_PILL);
        }
    }
}
