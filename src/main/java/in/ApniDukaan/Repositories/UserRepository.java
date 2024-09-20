package in.ApniDukaan.Repositories;

import in.ApniDukaan.Entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {

    UserEntity findByEmail(String email);

    List<UserEntity> findByRole(String role);

    UserEntity findByResetToken(String token);

    Boolean existsByEmail(String email);
}
