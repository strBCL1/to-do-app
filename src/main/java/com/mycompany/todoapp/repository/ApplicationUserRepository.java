package com.mycompany.todoapp.repository;

import com.mycompany.todoapp.domain.ApplicationUser;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ApplicationUser entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ApplicationUserRepository extends JpaRepository<ApplicationUser, Long>, JpaSpecificationExecutor<ApplicationUser> {
    @Query("select a from ApplicationUser a where a.user.login = ?1")
    Optional<ApplicationUser> findApplicationUserByLogin(String login);
}
