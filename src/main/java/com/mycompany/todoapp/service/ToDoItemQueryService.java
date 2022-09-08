package com.mycompany.todoapp.service;

import com.mycompany.todoapp.domain.*; // for static metamodels
import com.mycompany.todoapp.domain.ToDoItem;
import com.mycompany.todoapp.repository.ToDoItemRepository;
import com.mycompany.todoapp.service.criteria.ToDoItemCriteria;
import com.mycompany.todoapp.service.dto.ToDoItemDTO;
import com.mycompany.todoapp.service.mapper.ToDoItemMapper;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link ToDoItem} entities in the database.
 * The main input is a {@link ToDoItemCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ToDoItemDTO} or a {@link Page} of {@link ToDoItemDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ToDoItemQueryService extends QueryService<ToDoItem> {

    private final Logger log = LoggerFactory.getLogger(ToDoItemQueryService.class);

    private final ToDoItemRepository toDoItemRepository;

    private final ToDoItemMapper toDoItemMapper;

    public ToDoItemQueryService(ToDoItemRepository toDoItemRepository, ToDoItemMapper toDoItemMapper) {
        this.toDoItemRepository = toDoItemRepository;
        this.toDoItemMapper = toDoItemMapper;
    }

    /**
     * Return a {@link List} of {@link ToDoItemDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ToDoItemDTO> findByCriteria(ToDoItemCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ToDoItem> specification = createSpecification(criteria);
        return toDoItemMapper.toDto(toDoItemRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ToDoItemDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ToDoItemDTO> findByCriteria(ToDoItemCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ToDoItem> specification = createSpecification(criteria);
        return toDoItemRepository.findAll(specification, page).map(toDoItemMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ToDoItemCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ToDoItem> specification = createSpecification(criteria);
        return toDoItemRepository.count(specification);
    }

    /**
     * Function to convert {@link ToDoItemCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ToDoItem> createSpecification(ToDoItemCriteria criteria) {
        Specification<ToDoItem> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ToDoItem_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), ToDoItem_.name));
            }
            if (criteria.getIsCompleted() != null) {
                specification = specification.and(buildSpecification(criteria.getIsCompleted(), ToDoItem_.isCompleted));
            }
            if (criteria.getPlannedDueDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPlannedDueDate(), ToDoItem_.plannedDueDate));
            }
            if (criteria.getActualDueDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getActualDueDate(), ToDoItem_.actualDueDate));
            }
            if (criteria.getComment() != null) {
                specification = specification.and(buildStringSpecification(criteria.getComment(), ToDoItem_.comment));
            }
            if (criteria.getPriority() != null) {
                specification = specification.and(buildSpecification(criteria.getPriority(), ToDoItem_.priority));
            }
            if (criteria.getApplicationUserId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getApplicationUserId(),
                            root -> root.join(ToDoItem_.applicationUser, JoinType.LEFT).get(ApplicationUser_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
