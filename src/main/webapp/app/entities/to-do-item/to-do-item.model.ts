import dayjs from 'dayjs/esm';
import { IApplicationUser } from 'app/entities/application-user/application-user.model';
import { ToDoItemPriority } from 'app/entities/enumerations/to-do-item-priority.model';

export interface IToDoItem {
  id: number;
  name?: string | null;
  isCompleted?: boolean | null;
  plannedDueDate?: dayjs.Dayjs | null;
  actualDueDate?: dayjs.Dayjs | null;
  comment?: string | null;
  priority?: ToDoItemPriority | null;
  applicationUser?: Pick<IApplicationUser, 'id'> | null;
}

export type NewToDoItem = Omit<IToDoItem, 'id'> & { id: null };
