package com.mycompany.todoapp.web.rest;

import com.mycompany.todoapp.repository.ToDoItemRepository;
import com.mycompany.todoapp.service.ToDoItemQueryService;
import com.mycompany.todoapp.service.ToDoItemService;
import com.mycompany.todoapp.service.criteria.ToDoItemCriteria;
import com.mycompany.todoapp.service.dto.ToDoItemDTO;
import com.mycompany.todoapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.todoapp.domain.ToDoItem}.
 */
@RestController
@RequestMapping("/api")
public class ToDoItemResource {

    private final Logger log = LoggerFactory.getLogger(ToDoItemResource.class);

    private static final String ENTITY_NAME = "toDoItem";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ToDoItemService toDoItemService;

    private final ToDoItemRepository toDoItemRepository;

    private final ToDoItemQueryService toDoItemQueryService;

    public ToDoItemResource(
        ToDoItemService toDoItemService,
        ToDoItemRepository toDoItemRepository,
        ToDoItemQueryService toDoItemQueryService
    ) {
        this.toDoItemService = toDoItemService;
        this.toDoItemRepository = toDoItemRepository;
        this.toDoItemQueryService = toDoItemQueryService;
    }

    /**
     * {@code POST  /to-do-items} : Create a new toDoItem.
     *
     * @param toDoItemDTO the toDoItemDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new toDoItemDTO, or with status {@code 400 (Bad Request)} if the toDoItem has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/to-do-items")
    public ResponseEntity<ToDoItemDTO> createToDoItem(@Valid @RequestBody ToDoItemDTO toDoItemDTO) throws URISyntaxException {
        log.debug("REST request to save ToDoItem : {}", toDoItemDTO);
        if (toDoItemDTO.getId() != null) {
            throw new BadRequestAlertException("A new toDoItem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ToDoItemDTO result = toDoItemService.save(toDoItemDTO);
        return ResponseEntity
            .created(new URI("/api/to-do-items/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /to-do-items/:id} : Updates an existing toDoItem.
     *
     * @param id the id of the toDoItemDTO to save.
     * @param toDoItemDTO the toDoItemDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated toDoItemDTO,
     * or with status {@code 400 (Bad Request)} if the toDoItemDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the toDoItemDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/to-do-items/{id}")
    public ResponseEntity<ToDoItemDTO> updateToDoItem(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ToDoItemDTO toDoItemDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ToDoItem : {}, {}", id, toDoItemDTO);
        if (toDoItemDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, toDoItemDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!toDoItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ToDoItemDTO result = toDoItemService.update(toDoItemDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, toDoItemDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /to-do-items/:id} : Partial updates given fields of an existing toDoItem, field will ignore if it is null
     *
     * @param id the id of the toDoItemDTO to save.
     * @param toDoItemDTO the toDoItemDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated toDoItemDTO,
     * or with status {@code 400 (Bad Request)} if the toDoItemDTO is not valid,
     * or with status {@code 404 (Not Found)} if the toDoItemDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the toDoItemDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/to-do-items/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ToDoItemDTO> partialUpdateToDoItem(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ToDoItemDTO toDoItemDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ToDoItem partially : {}, {}", id, toDoItemDTO);
        if (toDoItemDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, toDoItemDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!toDoItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ToDoItemDTO> result = toDoItemService.partialUpdate(toDoItemDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, toDoItemDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /to-do-items} : get all the toDoItems.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of toDoItems in body.
     */
    @GetMapping("/to-do-items")
    public ResponseEntity<List<ToDoItemDTO>> getAllToDoItems(
        ToDoItemCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get ToDoItems by criteria: {}", criteria);
        Page<ToDoItemDTO> page = toDoItemQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /to-do-items/count} : count all the toDoItems.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/to-do-items/count")
    public ResponseEntity<Long> countToDoItems(ToDoItemCriteria criteria) {
        log.debug("REST request to count ToDoItems by criteria: {}", criteria);
        return ResponseEntity.ok().body(toDoItemQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /to-do-items/:id} : get the "id" toDoItem.
     *
     * @param id the id of the toDoItemDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the toDoItemDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/to-do-items/{id}")
    public ResponseEntity<ToDoItemDTO> getToDoItem(@PathVariable Long id) {
        log.debug("REST request to get ToDoItem : {}", id);
        Optional<ToDoItemDTO> toDoItemDTO = toDoItemService.findOne(id);
        return ResponseUtil.wrapOrNotFound(toDoItemDTO);
    }

    /**
     * {@code DELETE  /to-do-items/:id} : delete the "id" toDoItem.
     *
     * @param id the id of the toDoItemDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/to-do-items/{id}")
    public ResponseEntity<Void> deleteToDoItem(@PathVariable Long id) {
        log.debug("REST request to delete ToDoItem : {}", id);
        toDoItemService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
