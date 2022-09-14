package com.mycompany.todoapp.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(value = { "id", "applicationUser" }, allowGetters = true)
public class ToDoItemModificationDTO extends ToDoItemDTO {}
