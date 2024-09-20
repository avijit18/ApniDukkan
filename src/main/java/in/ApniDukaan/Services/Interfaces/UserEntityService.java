package in.ApniDukaan.Services.Interfaces;

import in.ApniDukaan.Entities.UserEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserEntityService {
    public UserEntity saveUser(UserEntity user);

    public UserEntity getUserByEmail(String email);

    public List<UserEntity> getUsers(String role);

    public Boolean updateAccountStatus(Integer id, Boolean status);

    public void increaseFailedAttempt(UserEntity user);

    public void userAccountLock(UserEntity user);

    public boolean unlockAccountTimeExpired(UserEntity user);

    public void resetAttempt(int userId);

    public void updateUserResetToken(String email, String resetToken);

    public UserEntity getUserByToken(String token);

    public UserEntity updateUser(UserEntity user);

    public UserEntity updateUserProfile(UserEntity user, MultipartFile img);

    public UserEntity saveAdmin(UserEntity user);

    public Boolean existsEmail(String email);
}
