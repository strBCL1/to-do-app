package com.mycompany.todoapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.todoapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ToDoItemDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ToDoItemDTO.class);
        ToDoItemDTO toDoItemDTO1 = new ToDoItemDTO();
        toDoItemDTO1.setId(1L);
        ToDoItemDTO toDoItemDTO2 = new ToDoItemDTO();
        assertThat(toDoItemDTO1).isNotEqualTo(toDoItemDTO2);
        toDoItemDTO2.setId(toDoItemDTO1.getId());
        assertThat(toDoItemDTO1).isEqualTo(toDoItemDTO2);
        toDoItemDTO2.setId(2L);
        assertThat(toDoItemDTO1).isNotEqualTo(toDoItemDTO2);
        toDoItemDTO1.setId(null);
        assertThat(toDoItemDTO1).isNotEqualTo(toDoItemDTO2);
    }
}
