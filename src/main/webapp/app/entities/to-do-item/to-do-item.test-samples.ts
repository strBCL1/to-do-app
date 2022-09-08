import dayjs from 'dayjs/esm';

import { ToDoItemPriority } from 'app/entities/enumerations/to-do-item-priority.model';

import { IToDoItem, NewToDoItem } from './to-do-item.model';

export const sampleWithRequiredData: IToDoItem = {
  id: 40844,
};

export const sampleWithPartialData: IToDoItem = {
  id: 7684,
  plannedDueDate: dayjs('2022-09-08T00:38'),
  actualDueDate: dayjs('2022-09-08T05:39'),
  comment: 'calculating Brazilian Plastic',
  priority: ToDoItemPriority['MEDIUM'],
};

export const sampleWithFullData: IToDoItem = {
  id: 22341,
  name: 'override',
  isCompleted: true,
  plannedDueDate: dayjs('2022-09-07T11:17'),
  actualDueDate: dayjs('2022-09-07T20:49'),
  comment: 'Bike',
  priority: ToDoItemPriority['LOW'],
};

export const sampleWithNewData: NewToDoItem = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
