package com.mycompany.todoapp.service.criteria;

import com.mycompany.todoapp.domain.enumeration.ToDoItemPriority;
import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.mycompany.todoapp.domain.ToDoItem} entity. This class is used
 * in {@link com.mycompany.todoapp.web.rest.ToDoItemResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /to-do-items?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ToDoItemCriteria implements Serializable, Criteria {

    /**
     * Class for filtering ToDoItemPriority
     */
    public static class ToDoItemPriorityFilter extends Filter<ToDoItemPriority> {

        public ToDoItemPriorityFilter() {}

        public ToDoItemPriorityFilter(ToDoItemPriorityFilter filter) {
            super(filter);
        }

        @Override
        public ToDoItemPriorityFilter copy() {
            return new ToDoItemPriorityFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private BooleanFilter isCompleted;

    private ZonedDateTimeFilter plannedDueDate;

    private ZonedDateTimeFilter actualDueDate;

    private StringFilter comment;

    private ToDoItemPriorityFilter priority;

    private LongFilter applicationUserId;

    private Boolean distinct;

    public ToDoItemCriteria() {}

    public ToDoItemCriteria(ToDoItemCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.isCompleted = other.isCompleted == null ? null : other.isCompleted.copy();
        this.plannedDueDate = other.plannedDueDate == null ? null : other.plannedDueDate.copy();
        this.actualDueDate = other.actualDueDate == null ? null : other.actualDueDate.copy();
        this.comment = other.comment == null ? null : other.comment.copy();
        this.priority = other.priority == null ? null : other.priority.copy();
        this.applicationUserId = other.applicationUserId == null ? null : other.applicationUserId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public ToDoItemCriteria copy() {
        return new ToDoItemCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getName() {
        return name;
    }

    public StringFilter name() {
        if (name == null) {
            name = new StringFilter();
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public BooleanFilter getIsCompleted() {
        return isCompleted;
    }

    public BooleanFilter isCompleted() {
        if (isCompleted == null) {
            isCompleted = new BooleanFilter();
        }
        return isCompleted;
    }

    public void setIsCompleted(BooleanFilter isCompleted) {
        this.isCompleted = isCompleted;
    }

    public ZonedDateTimeFilter getPlannedDueDate() {
        return plannedDueDate;
    }

    public ZonedDateTimeFilter plannedDueDate() {
        if (plannedDueDate == null) {
            plannedDueDate = new ZonedDateTimeFilter();
        }
        return plannedDueDate;
    }

    public void setPlannedDueDate(ZonedDateTimeFilter plannedDueDate) {
        this.plannedDueDate = plannedDueDate;
    }

    public ZonedDateTimeFilter getActualDueDate() {
        return actualDueDate;
    }

    public ZonedDateTimeFilter actualDueDate() {
        if (actualDueDate == null) {
            actualDueDate = new ZonedDateTimeFilter();
        }
        return actualDueDate;
    }

    public void setActualDueDate(ZonedDateTimeFilter actualDueDate) {
        this.actualDueDate = actualDueDate;
    }

    public StringFilter getComment() {
        return comment;
    }

    public StringFilter comment() {
        if (comment == null) {
            comment = new StringFilter();
        }
        return comment;
    }

    public void setComment(StringFilter comment) {
        this.comment = comment;
    }

    public ToDoItemPriorityFilter getPriority() {
        return priority;
    }

    public ToDoItemPriorityFilter priority() {
        if (priority == null) {
            priority = new ToDoItemPriorityFilter();
        }
        return priority;
    }

    public void setPriority(ToDoItemPriorityFilter priority) {
        this.priority = priority;
    }

    public LongFilter getApplicationUserId() {
        return applicationUserId;
    }

    public LongFilter applicationUserId() {
        if (applicationUserId == null) {
            applicationUserId = new LongFilter();
        }
        return applicationUserId;
    }

    public void setApplicationUserId(LongFilter applicationUserId) {
        this.applicationUserId = applicationUserId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ToDoItemCriteria that = (ToDoItemCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(isCompleted, that.isCompleted) &&
            Objects.equals(plannedDueDate, that.plannedDueDate) &&
            Objects.equals(actualDueDate, that.actualDueDate) &&
            Objects.equals(comment, that.comment) &&
            Objects.equals(priority, that.priority) &&
            Objects.equals(applicationUserId, that.applicationUserId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, isCompleted, plannedDueDate, actualDueDate, comment, priority, applicationUserId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ToDoItemCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (isCompleted != null ? "isCompleted=" + isCompleted + ", " : "") +
            (plannedDueDate != null ? "plannedDueDate=" + plannedDueDate + ", " : "") +
            (actualDueDate != null ? "actualDueDate=" + actualDueDate + ", " : "") +
            (comment != null ? "comment=" + comment + ", " : "") +
            (priority != null ? "priority=" + priority + ", " : "") +
            (applicationUserId != null ? "applicationUserId=" + applicationUserId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
