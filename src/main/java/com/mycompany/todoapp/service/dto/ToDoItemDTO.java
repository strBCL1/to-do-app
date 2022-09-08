package com.mycompany.todoapp.service.dto;

import com.mycompany.todoapp.domain.enumeration.ToDoItemPriority;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.mycompany.todoapp.domain.ToDoItem} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ToDoItemDTO implements Serializable {

    private Long id;

    @Size(max = 100)
    private String name;

    private Boolean isCompleted;

    private ZonedDateTime plannedDueDate;

    private ZonedDateTime actualDueDate;

    @Size(max = 500)
    private String comment;

    private ToDoItemPriority priority;

    private ApplicationUserDTO applicationUser;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getIsCompleted() {
        return isCompleted;
    }

    public void setIsCompleted(Boolean isCompleted) {
        this.isCompleted = isCompleted;
    }

    public ZonedDateTime getPlannedDueDate() {
        return plannedDueDate;
    }

    public void setPlannedDueDate(ZonedDateTime plannedDueDate) {
        this.plannedDueDate = plannedDueDate;
    }

    public ZonedDateTime getActualDueDate() {
        return actualDueDate;
    }

    public void setActualDueDate(ZonedDateTime actualDueDate) {
        this.actualDueDate = actualDueDate;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public ToDoItemPriority getPriority() {
        return priority;
    }

    public void setPriority(ToDoItemPriority priority) {
        this.priority = priority;
    }

    public ApplicationUserDTO getApplicationUser() {
        return applicationUser;
    }

    public void setApplicationUser(ApplicationUserDTO applicationUser) {
        this.applicationUser = applicationUser;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ToDoItemDTO)) {
            return false;
        }

        ToDoItemDTO toDoItemDTO = (ToDoItemDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, toDoItemDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ToDoItemDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", isCompleted='" + getIsCompleted() + "'" +
            ", plannedDueDate='" + getPlannedDueDate() + "'" +
            ", actualDueDate='" + getActualDueDate() + "'" +
            ", comment='" + getComment() + "'" +
            ", priority='" + getPriority() + "'" +
            ", applicationUser=" + getApplicationUser() +
            "}";
    }
}
