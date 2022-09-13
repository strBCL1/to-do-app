package com.mycompany.todoapp.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(value = { "id", "isCompleted", "applicationUser" }, allowGetters = true)
public class ToDoItemCreationDTO extends ToDoItemDTO {}
