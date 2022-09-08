package com.mycompany.todoapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ApplicationUser.
 */
@Entity
@Table(name = "application_user")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ApplicationUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @OneToOne
    @JoinColumn(unique = true)
    private User user;

    @OneToMany(mappedBy = "applicationUser")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "applicationUser" }, allowSetters = true)
    private Set<ToDoItem> toDoItems = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ApplicationUser id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ApplicationUser user(User user) {
        this.setUser(user);
        return this;
    }

    public Set<ToDoItem> getToDoItems() {
        return this.toDoItems;
    }

    public void setToDoItems(Set<ToDoItem> toDoItems) {
        if (this.toDoItems != null) {
            this.toDoItems.forEach(i -> i.setApplicationUser(null));
        }
        if (toDoItems != null) {
            toDoItems.forEach(i -> i.setApplicationUser(this));
        }
        this.toDoItems = toDoItems;
    }

    public ApplicationUser toDoItems(Set<ToDoItem> toDoItems) {
        this.setToDoItems(toDoItems);
        return this;
    }

    public ApplicationUser addToDoItem(ToDoItem toDoItem) {
        this.toDoItems.add(toDoItem);
        toDoItem.setApplicationUser(this);
        return this;
    }

    public ApplicationUser removeToDoItem(ToDoItem toDoItem) {
        this.toDoItems.remove(toDoItem);
        toDoItem.setApplicationUser(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ApplicationUser)) {
            return false;
        }
        return id != null && id.equals(((ApplicationUser) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ApplicationUser{" +
            "id=" + getId() +
            "}";
    }
}
