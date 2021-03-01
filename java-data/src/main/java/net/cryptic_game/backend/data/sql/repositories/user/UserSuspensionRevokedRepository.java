package net.cryptic_game.backend.data.sql.repositories.user;

import net.cryptic_game.backend.data.sql.entities.user.UserSuspensionRevoked;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserSuspensionRevokedRepository extends JpaRepository<UserSuspensionRevoked, UUID> {

    Page<UserSuspensionRevoked> findAllByUserSuspensionUserId(UUID userId, Pageable pageable);
}
