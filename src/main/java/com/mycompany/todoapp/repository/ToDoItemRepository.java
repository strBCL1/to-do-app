package com.mycompany.todoapp.repository;

import com.mycompany.todoapp.domain.ApplicationUser;
import com.mycompany.todoapp.domain.ToDoItem;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Spring Data JPA repository for the ToDoItem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ToDoItemRepository extends JpaRepository<ToDoItem, Long>, JpaSpecificationExecutor<ToDoItem> {
    @Query("select t from ToDoItem t where t.applicationUser.user.login = ?1")
    List<ToDoItem> findAllByUserLogin(String login, Pageable pageable);

    @Query("select t from ToDoItem t where t.applicationUser.user.login = ?1 and t.id = ?2")
    Optional<ToDoItem> findOneByUserLoginById(String login, Long id);

    @Transactional
    @Modifying
    @Query("delete from ToDoItem t where t.id = ?1 and t.applicationUser = ?2")
    void deleteByIdAndApplicationUser(Long id, ApplicationUser applicationUser);
}
