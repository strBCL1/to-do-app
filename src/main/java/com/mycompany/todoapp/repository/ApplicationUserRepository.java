package com.mycompany.todoapp.repository;

import com.mycompany.todoapp.domain.ApplicationUser;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ApplicationUser entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ApplicationUserRepository extends JpaRepository<ApplicationUser, Long>, JpaSpecificationExecutor<ApplicationUser> {}
