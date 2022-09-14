package com.mycompany.todoapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mycompany.todoapp.domain.enumeration.ToDoItemPriority;
import java.io.Serializable;
import java.time.ZonedDateTime;
import javax.persistence.*;
import javax.validation.constraints.Size;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ToDoItem.
 */
@Entity
@Table(name = "to_do_item")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ToDoItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Size(max = 100)
    @Column(name = "name", length = 100)
    private String name;

    @Column(name = "is_completed")
    private Boolean isCompleted;

    @Column(name = "planned_due_date")
    private ZonedDateTime plannedDueDate;

    @Column(name = "actual_due_date")
    private ZonedDateTime actualDueDate;

    @Size(max = 500)
    @Column(name = "comment", length = 500)
    private String comment;

    @Enumerated(EnumType.STRING)
    @Column(name = "priority")
    private ToDoItemPriority priority;

    @ManyToOne
    @JsonIgnoreProperties(value = { "user", "toDoItems" }, allowSetters = true)
    private ApplicationUser applicationUser;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ToDoItem id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public ToDoItem name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getIsCompleted() {
        return this.isCompleted;
    }

    public ToDoItem isCompleted(Boolean isCompleted) {
        this.setIsCompleted(isCompleted);
        return this;
    }

    public void setIsCompleted(Boolean isCompleted) {
        this.isCompleted = isCompleted;
    }

    public ZonedDateTime getPlannedDueDate() {
        return this.plannedDueDate;
    }

    public ToDoItem plannedDueDate(ZonedDateTime plannedDueDate) {
        this.setPlannedDueDate(plannedDueDate);
        return this;
    }

    public void setPlannedDueDate(ZonedDateTime plannedDueDate) {
        this.plannedDueDate = plannedDueDate;
    }

    public ZonedDateTime getActualDueDate() {
        return this.actualDueDate;
    }

    public ToDoItem actualDueDate(ZonedDateTime actualDueDate) {
        this.setActualDueDate(actualDueDate);
        return this;
    }

    public void setActualDueDate(ZonedDateTime actualDueDate) {
        this.actualDueDate = actualDueDate;
    }

    public String getComment() {
        return this.comment;
    }

    public ToDoItem comment(String comment) {
        this.setComment(comment);
        return this;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public ToDoItemPriority getPriority() {
        return this.priority;
    }

    public ToDoItem priority(ToDoItemPriority priority) {
        this.setPriority(priority);
        return this;
    }

    public void setPriority(ToDoItemPriority priority) {
        this.priority = priority;
    }

    public ApplicationUser getApplicationUser() {
        return this.applicationUser;
    }

    public void setApplicationUser(ApplicationUser applicationUser) {
        this.applicationUser = applicationUser;
    }

    public ToDoItem applicationUser(ApplicationUser applicationUser) {
        this.setApplicationUser(applicationUser);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ToDoItem)) {
            return false;
        }
        return id != null && id.equals(((ToDoItem) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ToDoItem{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", isCompleted='" + getIsCompleted() + "'" +
            ", plannedDueDate='" + getPlannedDueDate() + "'" +
            ", actualDueDate='" + getActualDueDate() + "'" +
            ", comment='" + getComment() + "'" +
            ", priority='" + getPriority() + "'" +
            "}";
    }
}
