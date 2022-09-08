package com.mycompany.todoapp.repository;

import com.mycompany.todoapp.domain.ToDoItem;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ToDoItem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ToDoItemRepository extends JpaRepository<ToDoItem, Long>, JpaSpecificationExecutor<ToDoItem> {}
