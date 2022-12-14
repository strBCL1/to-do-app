package com.mycompany.todoapp.service;

import com.mycompany.todoapp.domain.ToDoItem;
import com.mycompany.todoapp.repository.ToDoItemRepository;
import com.mycompany.todoapp.service.dto.ToDoItemDTO;
import com.mycompany.todoapp.service.mapper.ToDoItemMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ToDoItem}.
 */
@Service
@Transactional
public class ToDoItemService {

    private final Logger log = LoggerFactory.getLogger(ToDoItemService.class);

    private final ToDoItemRepository toDoItemRepository;

    private final ToDoItemMapper toDoItemMapper;

    public ToDoItemService(ToDoItemRepository toDoItemRepository, ToDoItemMapper toDoItemMapper) {
        this.toDoItemRepository = toDoItemRepository;
        this.toDoItemMapper = toDoItemMapper;
    }

    /**
     * Save a toDoItem.
     *
     * @param toDoItemDTO the entity to save.
     * @return the persisted entity.
     */
    public ToDoItemDTO save(ToDoItemDTO toDoItemDTO) {
        log.debug("Request to save ToDoItem : {}", toDoItemDTO);
        ToDoItem toDoItem = toDoItemMapper.toEntity(toDoItemDTO);
        toDoItem = toDoItemRepository.save(toDoItem);
        return toDoItemMapper.toDto(toDoItem);
    }

    /**
     * Update a toDoItem.
     *
     * @param toDoItemDTO the entity to save.
     * @return the persisted entity.
     */
    public ToDoItemDTO update(ToDoItemDTO toDoItemDTO) {
        log.debug("Request to update ToDoItem : {}", toDoItemDTO);
        ToDoItem toDoItem = toDoItemMapper.toEntity(toDoItemDTO);
        toDoItem = toDoItemRepository.save(toDoItem);
        return toDoItemMapper.toDto(toDoItem);
    }

    /**
     * Partially update a toDoItem.
     *
     * @param toDoItemDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ToDoItemDTO> partialUpdate(ToDoItemDTO toDoItemDTO) {
        log.debug("Request to partially update ToDoItem : {}", toDoItemDTO);

        return toDoItemRepository
            .findById(toDoItemDTO.getId())
            .map(existingToDoItem -> {
                toDoItemMapper.partialUpdate(existingToDoItem, toDoItemDTO);

                return existingToDoItem;
            })
            .map(toDoItemRepository::save)
            .map(toDoItemMapper::toDto);
    }

    /**
     * Get all the toDoItems.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ToDoItemDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ToDoItems");
        return toDoItemRepository.findAll(pageable).map(toDoItemMapper::toDto);
    }

    /**
     * Get one toDoItem by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ToDoItemDTO> findOne(Long id) {
        log.debug("Request to get ToDoItem : {}", id);
        return toDoItemRepository.findById(id).map(toDoItemMapper::toDto);
    }

    /**
     * Delete the toDoItem by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ToDoItem : {}", id);
        toDoItemRepository.deleteById(id);
    }
}
