package com.mycompany.todoapp.service.criteria;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springdoc.api.annotations.ParameterObject;

@ParameterObject
@JsonIgnoreProperties(value = { "applicationUserId" })
public class ToDoItemCriteriaWithoutApplicationUserId extends ToDoItemCriteria {
    //    TODO: ignore ApplicationUserId in current-user endpoints!
}
