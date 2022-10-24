package ml.pic.tech.security.session.security.repository;

import ml.pic.tech.security.session.security.entity.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {
    Utilisateur findByEmail(String email);

    @Modifying
    @Query("UPDATE Utilisateur u SET u.password=?2 WHERE u.email=?1")
    void updatePasswordByEmail(String email, String passwordNew);

}
