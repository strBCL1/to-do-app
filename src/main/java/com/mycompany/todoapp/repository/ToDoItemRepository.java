package com.mycompany.todoapp.repository;

import com.mycompany.todoapp.domain.ToDoItem;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ToDoItem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ToDoItemRepository extends JpaRepository<ToDoItem, Long>, JpaSpecificationExecutor<ToDoItem> {
    @Query("select t from ToDoItem t where t.applicationUser.user.login = ?1")
    List<ToDoItem> findAllByUserLogin(String login, Pageable pageable);
}
