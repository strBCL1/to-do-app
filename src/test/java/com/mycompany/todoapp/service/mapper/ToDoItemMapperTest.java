package com.mycompany.todoapp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ToDoItemMapperTest {

    private ToDoItemMapper toDoItemMapper;

    @BeforeEach
    public void setUp() {
        toDoItemMapper = new ToDoItemMapperImpl();
    }
}
