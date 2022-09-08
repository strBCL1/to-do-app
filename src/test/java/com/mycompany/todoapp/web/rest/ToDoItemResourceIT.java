package com.mycompany.todoapp.web.rest;

import static com.mycompany.todoapp.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.todoapp.IntegrationTest;
import com.mycompany.todoapp.domain.ApplicationUser;
import com.mycompany.todoapp.domain.ToDoItem;
import com.mycompany.todoapp.domain.enumeration.ToDoItemPriority;
import com.mycompany.todoapp.repository.ToDoItemRepository;
import com.mycompany.todoapp.service.criteria.ToDoItemCriteria;
import com.mycompany.todoapp.service.dto.ToDoItemDTO;
import com.mycompany.todoapp.service.mapper.ToDoItemMapper;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ToDoItemResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ToDoItemResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_COMPLETED = false;
    private static final Boolean UPDATED_IS_COMPLETED = true;

    private static final ZonedDateTime DEFAULT_PLANNED_DUE_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_PLANNED_DUE_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_PLANNED_DUE_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final ZonedDateTime DEFAULT_ACTUAL_DUE_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_ACTUAL_DUE_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_ACTUAL_DUE_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final String DEFAULT_COMMENT = "AAAAAAAAAA";
    private static final String UPDATED_COMMENT = "BBBBBBBBBB";

    private static final ToDoItemPriority DEFAULT_PRIORITY = ToDoItemPriority.HIGH;
    private static final ToDoItemPriority UPDATED_PRIORITY = ToDoItemPriority.MEDIUM;

    private static final String ENTITY_API_URL = "/api/to-do-items";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ToDoItemRepository toDoItemRepository;

    @Autowired
    private ToDoItemMapper toDoItemMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restToDoItemMockMvc;

    private ToDoItem toDoItem;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ToDoItem createEntity(EntityManager em) {
        ToDoItem toDoItem = new ToDoItem()
            .name(DEFAULT_NAME)
            .isCompleted(DEFAULT_IS_COMPLETED)
            .plannedDueDate(DEFAULT_PLANNED_DUE_DATE)
            .actualDueDate(DEFAULT_ACTUAL_DUE_DATE)
            .comment(DEFAULT_COMMENT)
            .priority(DEFAULT_PRIORITY);
        return toDoItem;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ToDoItem createUpdatedEntity(EntityManager em) {
        ToDoItem toDoItem = new ToDoItem()
            .name(UPDATED_NAME)
            .isCompleted(UPDATED_IS_COMPLETED)
            .plannedDueDate(UPDATED_PLANNED_DUE_DATE)
            .actualDueDate(UPDATED_ACTUAL_DUE_DATE)
            .comment(UPDATED_COMMENT)
            .priority(UPDATED_PRIORITY);
        return toDoItem;
    }

    @BeforeEach
    public void initTest() {
        toDoItem = createEntity(em);
    }

    @Test
    @Transactional
    void createToDoItem() throws Exception {
        int databaseSizeBeforeCreate = toDoItemRepository.findAll().size();
        // Create the ToDoItem
        ToDoItemDTO toDoItemDTO = toDoItemMapper.toDto(toDoItem);
        restToDoItemMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(toDoItemDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ToDoItem in the database
        List<ToDoItem> toDoItemList = toDoItemRepository.findAll();
        assertThat(toDoItemList).hasSize(databaseSizeBeforeCreate + 1);
        ToDoItem testToDoItem = toDoItemList.get(toDoItemList.size() - 1);
        assertThat(testToDoItem.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testToDoItem.getIsCompleted()).isEqualTo(DEFAULT_IS_COMPLETED);
        assertThat(testToDoItem.getPlannedDueDate()).isEqualTo(DEFAULT_PLANNED_DUE_DATE);
        assertThat(testToDoItem.getActualDueDate()).isEqualTo(DEFAULT_ACTUAL_DUE_DATE);
        assertThat(testToDoItem.getComment()).isEqualTo(DEFAULT_COMMENT);
        assertThat(testToDoItem.getPriority()).isEqualTo(DEFAULT_PRIORITY);
    }

    @Test
    @Transactional
    void createToDoItemWithExistingId() throws Exception {
        // Create the ToDoItem with an existing ID
        toDoItem.setId(1L);
        ToDoItemDTO toDoItemDTO = toDoItemMapper.toDto(toDoItem);

        int databaseSizeBeforeCreate = toDoItemRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restToDoItemMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(toDoItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ToDoItem in the database
        List<ToDoItem> toDoItemList = toDoItemRepository.findAll();
        assertThat(toDoItemList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllToDoItems() throws Exception {
        // Initialize the database
        toDoItemRepository.saveAndFlush(toDoItem);

        // Get all the toDoItemList
        restToDoItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(toDoItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].isCompleted").value(hasItem(DEFAULT_IS_COMPLETED.booleanValue())))
            .andExpect(jsonPath("$.[*].plannedDueDate").value(hasItem(sameInstant(DEFAULT_PLANNED_DUE_DATE))))
            .andExpect(jsonPath("$.[*].actualDueDate").value(hasItem(sameInstant(DEFAULT_ACTUAL_DUE_DATE))))
            .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT)))
            .andExpect(jsonPath("$.[*].priority").value(hasItem(DEFAULT_PRIORITY.toString())));
    }

    @Test
    @Transactional
    void getToDoItem() throws Exception {
        // Initialize the database
        toDoItemRepository.saveAndFlush(toDoItem);

        // Get the toDoItem
        restToDoItemMockMvc
            .perform(get(ENTITY_API_URL_ID, toDoItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(toDoItem.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.isCompleted").value(DEFAULT_IS_COMPLETED.booleanValue()))
            .andExpect(jsonPath("$.plannedDueDate").value(sameInstant(DEFAULT_PLANNED_DUE_DATE)))
            .andExpect(jsonPath("$.actualDueDate").value(sameInstant(DEFAULT_ACTUAL_DUE_DATE)))
            .andExpect(jsonPath("$.comment").value(DEFAULT_COMMENT))
            .andExpect(jsonPath("$.priority").value(DEFAULT_PRIORITY.toString()));
    }

    @Test
    @Transactional
    void getToDoItemsByIdFiltering() throws Exception {
        // Initialize the database
        toDoItemRepository.saveAndFlush(toDoItem);

        Long id = toDoItem.getId();

        defaultToDoItemShouldBeFound("id.equals=" + id);
        defaultToDoItemShouldNotBeFound("id.notEquals=" + id);

        defaultToDoItemShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultToDoItemShouldNotBeFound("id.greaterThan=" + id);

        defaultToDoItemShouldBeFound("id.lessThanOrEqual=" + id);
        defaultToDoItemShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllToDoItemsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        toDoItemRepository.saveAndFlush(toDoItem);

        // Get all the toDoItemList where name equals to DEFAULT_NAME
        defaultToDoItemShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the toDoItemList where name equals to UPDATED_NAME
        defaultToDoItemShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllToDoItemsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        toDoItemRepository.saveAndFlush(toDoItem);

        // Get all the toDoItemList where name in DEFAULT_NAME or UPDATED_NAME
        defaultToDoItemShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the toDoItemList where name equals to UPDATED_NAME
        defaultToDoItemShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllToDoItemsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        toDoItemRepository.saveAndFlush(toDoItem);

        // Get all the toDoItemList where name is not null
        defaultToDoItemShouldBeFound("name.specified=true");

        // Get all the toDoItemList where name is null
        defaultToDoItemShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllToDoItemsByNameContainsSomething() throws Exception {
        // Initialize the database
        toDoItemRepository.saveAndFlush(toDoItem);

        // Get all the toDoItemList where name contains DEFAULT_NAME
        defaultToDoItemShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the toDoItemList where name contains UPDATED_NAME
        defaultToDoItemShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllToDoItemsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        toDoItemRepository.saveAndFlush(toDoItem);

        // Get all the toDoItemList where name does not contain DEFAULT_NAME
        defaultToDoItemShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the toDoItemList where name does not contain UPDATED_NAME
        defaultToDoItemShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllToDoItemsByIsCompletedIsEqualToSomething() throws Exception {
        // Initialize the database
        toDoItemRepository.saveAndFlush(toDoItem);

        // Get all the toDoItemList where isCompleted equals to DEFAULT_IS_COMPLETED
        defaultToDoItemShouldBeFound("isCompleted.equals=" + DEFAULT_IS_COMPLETED);

        // Get all the toDoItemList where isCompleted equals to UPDATED_IS_COMPLETED
        defaultToDoItemShouldNotBeFound("isCompleted.equals=" + UPDATED_IS_COMPLETED);
    }

    @Test
    @Transactional
    void getAllToDoItemsByIsCompletedIsInShouldWork() throws Exception {
        // Initialize the database
        toDoItemRepository.saveAndFlush(toDoItem);

        // Get all the toDoItemList where isCompleted in DEFAULT_IS_COMPLETED or UPDATED_IS_COMPLETED
        defaultToDoItemShouldBeFound("isCompleted.in=" + DEFAULT_IS_COMPLETED + "," + UPDATED_IS_COMPLETED);

        // Get all the toDoItemList where isCompleted equals to UPDATED_IS_COMPLETED
        defaultToDoItemShouldNotBeFound("isCompleted.in=" + UPDATED_IS_COMPLETED);
    }

    @Test
    @Transactional
    void getAllToDoItemsByIsCompletedIsNullOrNotNull() throws Exception {
        // Initialize the database
        toDoItemRepository.saveAndFlush(toDoItem);

        // Get all the toDoItemList where isCompleted is not null
        defaultToDoItemShouldBeFound("isCompleted.specified=true");

        // Get all the toDoItemList where isCompleted is null
        defaultToDoItemShouldNotBeFound("isCompleted.specified=false");
    }

    @Test
    @Transactional
    void getAllToDoItemsByPlannedDueDateIsEqualToSomething() throws Exception {
        // Initialize the database
        toDoItemRepository.saveAndFlush(toDoItem);

        // Get all the toDoItemList where plannedDueDate equals to DEFAULT_PLANNED_DUE_DATE
        defaultToDoItemShouldBeFound("plannedDueDate.equals=" + DEFAULT_PLANNED_DUE_DATE);

        // Get all the toDoItemList where plannedDueDate equals to UPDATED_PLANNED_DUE_DATE
        defaultToDoItemShouldNotBeFound("plannedDueDate.equals=" + UPDATED_PLANNED_DUE_DATE);
    }

    @Test
    @Transactional
    void getAllToDoItemsByPlannedDueDateIsInShouldWork() throws Exception {
        // Initialize the database
        toDoItemRepository.saveAndFlush(toDoItem);

        // Get all the toDoItemList where plannedDueDate in DEFAULT_PLANNED_DUE_DATE or UPDATED_PLANNED_DUE_DATE
        defaultToDoItemShouldBeFound("plannedDueDate.in=" + DEFAULT_PLANNED_DUE_DATE + "," + UPDATED_PLANNED_DUE_DATE);

        // Get all the toDoItemList where plannedDueDate equals to UPDATED_PLANNED_DUE_DATE
        defaultToDoItemShouldNotBeFound("plannedDueDate.in=" + UPDATED_PLANNED_DUE_DATE);
    }

    @Test
    @Transactional
    void getAllToDoItemsByPlannedDueDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        toDoItemRepository.saveAndFlush(toDoItem);

        // Get all the toDoItemList where plannedDueDate is not null
        defaultToDoItemShouldBeFound("plannedDueDate.specified=true");

        // Get all the toDoItemList where plannedDueDate is null
        defaultToDoItemShouldNotBeFound("plannedDueDate.specified=false");
    }

    @Test
    @Transactional
    void getAllToDoItemsByPlannedDueDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        toDoItemRepository.saveAndFlush(toDoItem);

        // Get all the toDoItemList where plannedDueDate is greater than or equal to DEFAULT_PLANNED_DUE_DATE
        defaultToDoItemShouldBeFound("plannedDueDate.greaterThanOrEqual=" + DEFAULT_PLANNED_DUE_DATE);

        // Get all the toDoItemList where plannedDueDate is greater than or equal to UPDATED_PLANNED_DUE_DATE
        defaultToDoItemShouldNotBeFound("plannedDueDate.greaterThanOrEqual=" + UPDATED_PLANNED_DUE_DATE);
    }

    @Test
    @Transactional
    void getAllToDoItemsByPlannedDueDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        toDoItemRepository.saveAndFlush(toDoItem);

        // Get all the toDoItemList where plannedDueDate is less than or equal to DEFAULT_PLANNED_DUE_DATE
        defaultToDoItemShouldBeFound("plannedDueDate.lessThanOrEqual=" + DEFAULT_PLANNED_DUE_DATE);

        // Get all the toDoItemList where plannedDueDate is less than or equal to SMALLER_PLANNED_DUE_DATE
        defaultToDoItemShouldNotBeFound("plannedDueDate.lessThanOrEqual=" + SMALLER_PLANNED_DUE_DATE);
    }

    @Test
    @Transactional
    void getAllToDoItemsByPlannedDueDateIsLessThanSomething() throws Exception {
        // Initialize the database
        toDoItemRepository.saveAndFlush(toDoItem);

        // Get all the toDoItemList where plannedDueDate is less than DEFAULT_PLANNED_DUE_DATE
        defaultToDoItemShouldNotBeFound("plannedDueDate.lessThan=" + DEFAULT_PLANNED_DUE_DATE);

        // Get all the toDoItemList where plannedDueDate is less than UPDATED_PLANNED_DUE_DATE
        defaultToDoItemShouldBeFound("plannedDueDate.lessThan=" + UPDATED_PLANNED_DUE_DATE);
    }

    @Test
    @Transactional
    void getAllToDoItemsByPlannedDueDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        toDoItemRepository.saveAndFlush(toDoItem);

        // Get all the toDoItemList where plannedDueDate is greater than DEFAULT_PLANNED_DUE_DATE
        defaultToDoItemShouldNotBeFound("plannedDueDate.greaterThan=" + DEFAULT_PLANNED_DUE_DATE);

        // Get all the toDoItemList where plannedDueDate is greater than SMALLER_PLANNED_DUE_DATE
        defaultToDoItemShouldBeFound("plannedDueDate.greaterThan=" + SMALLER_PLANNED_DUE_DATE);
    }

    @Test
    @Transactional
    void getAllToDoItemsByActualDueDateIsEqualToSomething() throws Exception {
        // Initialize the database
        toDoItemRepository.saveAndFlush(toDoItem);

        // Get all the toDoItemList where actualDueDate equals to DEFAULT_ACTUAL_DUE_DATE
        defaultToDoItemShouldBeFound("actualDueDate.equals=" + DEFAULT_ACTUAL_DUE_DATE);

        // Get all the toDoItemList where actualDueDate equals to UPDATED_ACTUAL_DUE_DATE
        defaultToDoItemShouldNotBeFound("actualDueDate.equals=" + UPDATED_ACTUAL_DUE_DATE);
    }

    @Test
    @Transactional
    void getAllToDoItemsByActualDueDateIsInShouldWork() throws Exception {
        // Initialize the database
        toDoItemRepository.saveAndFlush(toDoItem);

        // Get all the toDoItemList where actualDueDate in DEFAULT_ACTUAL_DUE_DATE or UPDATED_ACTUAL_DUE_DATE
        defaultToDoItemShouldBeFound("actualDueDate.in=" + DEFAULT_ACTUAL_DUE_DATE + "," + UPDATED_ACTUAL_DUE_DATE);

        // Get all the toDoItemList where actualDueDate equals to UPDATED_ACTUAL_DUE_DATE
        defaultToDoItemShouldNotBeFound("actualDueDate.in=" + UPDATED_ACTUAL_DUE_DATE);
    }

    @Test
    @Transactional
    void getAllToDoItemsByActualDueDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        toDoItemRepository.saveAndFlush(toDoItem);

        // Get all the toDoItemList where actualDueDate is not null
        defaultToDoItemShouldBeFound("actualDueDate.specified=true");

        // Get all the toDoItemList where actualDueDate is null
        defaultToDoItemShouldNotBeFound("actualDueDate.specified=false");
    }

    @Test
    @Transactional
    void getAllToDoItemsByActualDueDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        toDoItemRepository.saveAndFlush(toDoItem);

        // Get all the toDoItemList where actualDueDate is greater than or equal to DEFAULT_ACTUAL_DUE_DATE
        defaultToDoItemShouldBeFound("actualDueDate.greaterThanOrEqual=" + DEFAULT_ACTUAL_DUE_DATE);

        // Get all the toDoItemList where actualDueDate is greater than or equal to UPDATED_ACTUAL_DUE_DATE
        defaultToDoItemShouldNotBeFound("actualDueDate.greaterThanOrEqual=" + UPDATED_ACTUAL_DUE_DATE);
    }

    @Test
    @Transactional
    void getAllToDoItemsByActualDueDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        toDoItemRepository.saveAndFlush(toDoItem);

        // Get all the toDoItemList where actualDueDate is less than or equal to DEFAULT_ACTUAL_DUE_DATE
        defaultToDoItemShouldBeFound("actualDueDate.lessThanOrEqual=" + DEFAULT_ACTUAL_DUE_DATE);

        // Get all the toDoItemList where actualDueDate is less than or equal to SMALLER_ACTUAL_DUE_DATE
        defaultToDoItemShouldNotBeFound("actualDueDate.lessThanOrEqual=" + SMALLER_ACTUAL_DUE_DATE);
    }

    @Test
    @Transactional
    void getAllToDoItemsByActualDueDateIsLessThanSomething() throws Exception {
        // Initialize the database
        toDoItemRepository.saveAndFlush(toDoItem);

        // Get all the toDoItemList where actualDueDate is less than DEFAULT_ACTUAL_DUE_DATE
        defaultToDoItemShouldNotBeFound("actualDueDate.lessThan=" + DEFAULT_ACTUAL_DUE_DATE);

        // Get all the toDoItemList where actualDueDate is less than UPDATED_ACTUAL_DUE_DATE
        defaultToDoItemShouldBeFound("actualDueDate.lessThan=" + UPDATED_ACTUAL_DUE_DATE);
    }

    @Test
    @Transactional
    void getAllToDoItemsByActualDueDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        toDoItemRepository.saveAndFlush(toDoItem);

        // Get all the toDoItemList where actualDueDate is greater than DEFAULT_ACTUAL_DUE_DATE
        defaultToDoItemShouldNotBeFound("actualDueDate.greaterThan=" + DEFAULT_ACTUAL_DUE_DATE);

        // Get all the toDoItemList where actualDueDate is greater than SMALLER_ACTUAL_DUE_DATE
        defaultToDoItemShouldBeFound("actualDueDate.greaterThan=" + SMALLER_ACTUAL_DUE_DATE);
    }

    @Test
    @Transactional
    void getAllToDoItemsByCommentIsEqualToSomething() throws Exception {
        // Initialize the database
        toDoItemRepository.saveAndFlush(toDoItem);

        // Get all the toDoItemList where comment equals to DEFAULT_COMMENT
        defaultToDoItemShouldBeFound("comment.equals=" + DEFAULT_COMMENT);

        // Get all the toDoItemList where comment equals to UPDATED_COMMENT
        defaultToDoItemShouldNotBeFound("comment.equals=" + UPDATED_COMMENT);
    }

    @Test
    @Transactional
    void getAllToDoItemsByCommentIsInShouldWork() throws Exception {
        // Initialize the database
        toDoItemRepository.saveAndFlush(toDoItem);

        // Get all the toDoItemList where comment in DEFAULT_COMMENT or UPDATED_COMMENT
        defaultToDoItemShouldBeFound("comment.in=" + DEFAULT_COMMENT + "," + UPDATED_COMMENT);

        // Get all the toDoItemList where comment equals to UPDATED_COMMENT
        defaultToDoItemShouldNotBeFound("comment.in=" + UPDATED_COMMENT);
    }

    @Test
    @Transactional
    void getAllToDoItemsByCommentIsNullOrNotNull() throws Exception {
        // Initialize the database
        toDoItemRepository.saveAndFlush(toDoItem);

        // Get all the toDoItemList where comment is not null
        defaultToDoItemShouldBeFound("comment.specified=true");

        // Get all the toDoItemList where comment is null
        defaultToDoItemShouldNotBeFound("comment.specified=false");
    }

    @Test
    @Transactional
    void getAllToDoItemsByCommentContainsSomething() throws Exception {
        // Initialize the database
        toDoItemRepository.saveAndFlush(toDoItem);

        // Get all the toDoItemList where comment contains DEFAULT_COMMENT
        defaultToDoItemShouldBeFound("comment.contains=" + DEFAULT_COMMENT);

        // Get all the toDoItemList where comment contains UPDATED_COMMENT
        defaultToDoItemShouldNotBeFound("comment.contains=" + UPDATED_COMMENT);
    }

    @Test
    @Transactional
    void getAllToDoItemsByCommentNotContainsSomething() throws Exception {
        // Initialize the database
        toDoItemRepository.saveAndFlush(toDoItem);

        // Get all the toDoItemList where comment does not contain DEFAULT_COMMENT
        defaultToDoItemShouldNotBeFound("comment.doesNotContain=" + DEFAULT_COMMENT);

        // Get all the toDoItemList where comment does not contain UPDATED_COMMENT
        defaultToDoItemShouldBeFound("comment.doesNotContain=" + UPDATED_COMMENT);
    }

    @Test
    @Transactional
    void getAllToDoItemsByPriorityIsEqualToSomething() throws Exception {
        // Initialize the database
        toDoItemRepository.saveAndFlush(toDoItem);

        // Get all the toDoItemList where priority equals to DEFAULT_PRIORITY
        defaultToDoItemShouldBeFound("priority.equals=" + DEFAULT_PRIORITY);

        // Get all the toDoItemList where priority equals to UPDATED_PRIORITY
        defaultToDoItemShouldNotBeFound("priority.equals=" + UPDATED_PRIORITY);
    }

    @Test
    @Transactional
    void getAllToDoItemsByPriorityIsInShouldWork() throws Exception {
        // Initialize the database
        toDoItemRepository.saveAndFlush(toDoItem);

        // Get all the toDoItemList where priority in DEFAULT_PRIORITY or UPDATED_PRIORITY
        defaultToDoItemShouldBeFound("priority.in=" + DEFAULT_PRIORITY + "," + UPDATED_PRIORITY);

        // Get all the toDoItemList where priority equals to UPDATED_PRIORITY
        defaultToDoItemShouldNotBeFound("priority.in=" + UPDATED_PRIORITY);
    }

    @Test
    @Transactional
    void getAllToDoItemsByPriorityIsNullOrNotNull() throws Exception {
        // Initialize the database
        toDoItemRepository.saveAndFlush(toDoItem);

        // Get all the toDoItemList where priority is not null
        defaultToDoItemShouldBeFound("priority.specified=true");

        // Get all the toDoItemList where priority is null
        defaultToDoItemShouldNotBeFound("priority.specified=false");
    }

    @Test
    @Transactional
    void getAllToDoItemsByApplicationUserIsEqualToSomething() throws Exception {
        ApplicationUser applicationUser;
        if (TestUtil.findAll(em, ApplicationUser.class).isEmpty()) {
            toDoItemRepository.saveAndFlush(toDoItem);
            applicationUser = ApplicationUserResourceIT.createEntity(em);
        } else {
            applicationUser = TestUtil.findAll(em, ApplicationUser.class).get(0);
        }
        em.persist(applicationUser);
        em.flush();
        toDoItem.setApplicationUser(applicationUser);
        toDoItemRepository.saveAndFlush(toDoItem);
        Long applicationUserId = applicationUser.getId();

        // Get all the toDoItemList where applicationUser equals to applicationUserId
        defaultToDoItemShouldBeFound("applicationUserId.equals=" + applicationUserId);

        // Get all the toDoItemList where applicationUser equals to (applicationUserId + 1)
        defaultToDoItemShouldNotBeFound("applicationUserId.equals=" + (applicationUserId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultToDoItemShouldBeFound(String filter) throws Exception {
        restToDoItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(toDoItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].isCompleted").value(hasItem(DEFAULT_IS_COMPLETED.booleanValue())))
            .andExpect(jsonPath("$.[*].plannedDueDate").value(hasItem(sameInstant(DEFAULT_PLANNED_DUE_DATE))))
            .andExpect(jsonPath("$.[*].actualDueDate").value(hasItem(sameInstant(DEFAULT_ACTUAL_DUE_DATE))))
            .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT)))
            .andExpect(jsonPath("$.[*].priority").value(hasItem(DEFAULT_PRIORITY.toString())));

        // Check, that the count call also returns 1
        restToDoItemMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultToDoItemShouldNotBeFound(String filter) throws Exception {
        restToDoItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restToDoItemMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingToDoItem() throws Exception {
        // Get the toDoItem
        restToDoItemMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingToDoItem() throws Exception {
        // Initialize the database
        toDoItemRepository.saveAndFlush(toDoItem);

        int databaseSizeBeforeUpdate = toDoItemRepository.findAll().size();

        // Update the toDoItem
        ToDoItem updatedToDoItem = toDoItemRepository.findById(toDoItem.getId()).get();
        // Disconnect from session so that the updates on updatedToDoItem are not directly saved in db
        em.detach(updatedToDoItem);
        updatedToDoItem
            .name(UPDATED_NAME)
            .isCompleted(UPDATED_IS_COMPLETED)
            .plannedDueDate(UPDATED_PLANNED_DUE_DATE)
            .actualDueDate(UPDATED_ACTUAL_DUE_DATE)
            .comment(UPDATED_COMMENT)
            .priority(UPDATED_PRIORITY);
        ToDoItemDTO toDoItemDTO = toDoItemMapper.toDto(updatedToDoItem);

        restToDoItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, toDoItemDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(toDoItemDTO))
            )
            .andExpect(status().isOk());

        // Validate the ToDoItem in the database
        List<ToDoItem> toDoItemList = toDoItemRepository.findAll();
        assertThat(toDoItemList).hasSize(databaseSizeBeforeUpdate);
        ToDoItem testToDoItem = toDoItemList.get(toDoItemList.size() - 1);
        assertThat(testToDoItem.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testToDoItem.getIsCompleted()).isEqualTo(UPDATED_IS_COMPLETED);
        assertThat(testToDoItem.getPlannedDueDate()).isEqualTo(UPDATED_PLANNED_DUE_DATE);
        assertThat(testToDoItem.getActualDueDate()).isEqualTo(UPDATED_ACTUAL_DUE_DATE);
        assertThat(testToDoItem.getComment()).isEqualTo(UPDATED_COMMENT);
        assertThat(testToDoItem.getPriority()).isEqualTo(UPDATED_PRIORITY);
    }

    @Test
    @Transactional
    void putNonExistingToDoItem() throws Exception {
        int databaseSizeBeforeUpdate = toDoItemRepository.findAll().size();
        toDoItem.setId(count.incrementAndGet());

        // Create the ToDoItem
        ToDoItemDTO toDoItemDTO = toDoItemMapper.toDto(toDoItem);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restToDoItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, toDoItemDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(toDoItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ToDoItem in the database
        List<ToDoItem> toDoItemList = toDoItemRepository.findAll();
        assertThat(toDoItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchToDoItem() throws Exception {
        int databaseSizeBeforeUpdate = toDoItemRepository.findAll().size();
        toDoItem.setId(count.incrementAndGet());

        // Create the ToDoItem
        ToDoItemDTO toDoItemDTO = toDoItemMapper.toDto(toDoItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restToDoItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(toDoItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ToDoItem in the database
        List<ToDoItem> toDoItemList = toDoItemRepository.findAll();
        assertThat(toDoItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamToDoItem() throws Exception {
        int databaseSizeBeforeUpdate = toDoItemRepository.findAll().size();
        toDoItem.setId(count.incrementAndGet());

        // Create the ToDoItem
        ToDoItemDTO toDoItemDTO = toDoItemMapper.toDto(toDoItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restToDoItemMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(toDoItemDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ToDoItem in the database
        List<ToDoItem> toDoItemList = toDoItemRepository.findAll();
        assertThat(toDoItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateToDoItemWithPatch() throws Exception {
        // Initialize the database
        toDoItemRepository.saveAndFlush(toDoItem);

        int databaseSizeBeforeUpdate = toDoItemRepository.findAll().size();

        // Update the toDoItem using partial update
        ToDoItem partialUpdatedToDoItem = new ToDoItem();
        partialUpdatedToDoItem.setId(toDoItem.getId());

        partialUpdatedToDoItem.plannedDueDate(UPDATED_PLANNED_DUE_DATE).actualDueDate(UPDATED_ACTUAL_DUE_DATE).comment(UPDATED_COMMENT);

        restToDoItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedToDoItem.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedToDoItem))
            )
            .andExpect(status().isOk());

        // Validate the ToDoItem in the database
        List<ToDoItem> toDoItemList = toDoItemRepository.findAll();
        assertThat(toDoItemList).hasSize(databaseSizeBeforeUpdate);
        ToDoItem testToDoItem = toDoItemList.get(toDoItemList.size() - 1);
        assertThat(testToDoItem.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testToDoItem.getIsCompleted()).isEqualTo(DEFAULT_IS_COMPLETED);
        assertThat(testToDoItem.getPlannedDueDate()).isEqualTo(UPDATED_PLANNED_DUE_DATE);
        assertThat(testToDoItem.getActualDueDate()).isEqualTo(UPDATED_ACTUAL_DUE_DATE);
        assertThat(testToDoItem.getComment()).isEqualTo(UPDATED_COMMENT);
        assertThat(testToDoItem.getPriority()).isEqualTo(DEFAULT_PRIORITY);
    }

    @Test
    @Transactional
    void fullUpdateToDoItemWithPatch() throws Exception {
        // Initialize the database
        toDoItemRepository.saveAndFlush(toDoItem);

        int databaseSizeBeforeUpdate = toDoItemRepository.findAll().size();

        // Update the toDoItem using partial update
        ToDoItem partialUpdatedToDoItem = new ToDoItem();
        partialUpdatedToDoItem.setId(toDoItem.getId());

        partialUpdatedToDoItem
            .name(UPDATED_NAME)
            .isCompleted(UPDATED_IS_COMPLETED)
            .plannedDueDate(UPDATED_PLANNED_DUE_DATE)
            .actualDueDate(UPDATED_ACTUAL_DUE_DATE)
            .comment(UPDATED_COMMENT)
            .priority(UPDATED_PRIORITY);

        restToDoItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedToDoItem.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedToDoItem))
            )
            .andExpect(status().isOk());

        // Validate the ToDoItem in the database
        List<ToDoItem> toDoItemList = toDoItemRepository.findAll();
        assertThat(toDoItemList).hasSize(databaseSizeBeforeUpdate);
        ToDoItem testToDoItem = toDoItemList.get(toDoItemList.size() - 1);
        assertThat(testToDoItem.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testToDoItem.getIsCompleted()).isEqualTo(UPDATED_IS_COMPLETED);
        assertThat(testToDoItem.getPlannedDueDate()).isEqualTo(UPDATED_PLANNED_DUE_DATE);
        assertThat(testToDoItem.getActualDueDate()).isEqualTo(UPDATED_ACTUAL_DUE_DATE);
        assertThat(testToDoItem.getComment()).isEqualTo(UPDATED_COMMENT);
        assertThat(testToDoItem.getPriority()).isEqualTo(UPDATED_PRIORITY);
    }

    @Test
    @Transactional
    void patchNonExistingToDoItem() throws Exception {
        int databaseSizeBeforeUpdate = toDoItemRepository.findAll().size();
        toDoItem.setId(count.incrementAndGet());

        // Create the ToDoItem
        ToDoItemDTO toDoItemDTO = toDoItemMapper.toDto(toDoItem);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restToDoItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, toDoItemDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(toDoItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ToDoItem in the database
        List<ToDoItem> toDoItemList = toDoItemRepository.findAll();
        assertThat(toDoItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchToDoItem() throws Exception {
        int databaseSizeBeforeUpdate = toDoItemRepository.findAll().size();
        toDoItem.setId(count.incrementAndGet());

        // Create the ToDoItem
        ToDoItemDTO toDoItemDTO = toDoItemMapper.toDto(toDoItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restToDoItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(toDoItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ToDoItem in the database
        List<ToDoItem> toDoItemList = toDoItemRepository.findAll();
        assertThat(toDoItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamToDoItem() throws Exception {
        int databaseSizeBeforeUpdate = toDoItemRepository.findAll().size();
        toDoItem.setId(count.incrementAndGet());

        // Create the ToDoItem
        ToDoItemDTO toDoItemDTO = toDoItemMapper.toDto(toDoItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restToDoItemMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(toDoItemDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ToDoItem in the database
        List<ToDoItem> toDoItemList = toDoItemRepository.findAll();
        assertThat(toDoItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteToDoItem() throws Exception {
        // Initialize the database
        toDoItemRepository.saveAndFlush(toDoItem);

        int databaseSizeBeforeDelete = toDoItemRepository.findAll().size();

        // Delete the toDoItem
        restToDoItemMockMvc
            .perform(delete(ENTITY_API_URL_ID, toDoItem.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ToDoItem> toDoItemList = toDoItemRepository.findAll();
        assertThat(toDoItemList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
