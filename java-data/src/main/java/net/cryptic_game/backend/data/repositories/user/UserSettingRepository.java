package net.cryptic_game.backend.data.repositories.user;

import net.cryptic_game.backend.data.entities.user.User;
import net.cryptic_game.backend.data.entities.user.UserSetting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserSettingRepository extends JpaRepository<UserSetting, UUID> {

    List<UserSetting> findAllByKeyUser(final User user);

    Optional<UserSetting> findByKeyUserAndKeyKey(final User user, final String key);
}
