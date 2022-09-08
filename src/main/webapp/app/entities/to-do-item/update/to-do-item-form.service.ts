import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IToDoItem, NewToDoItem } from '../to-do-item.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IToDoItem for edit and NewToDoItemFormGroupInput for create.
 */
type ToDoItemFormGroupInput = IToDoItem | PartialWithRequiredKeyOf<NewToDoItem>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IToDoItem | NewToDoItem> = Omit<T, 'plannedDueDate' | 'actualDueDate'> & {
  plannedDueDate?: string | null;
  actualDueDate?: string | null;
};

type ToDoItemFormRawValue = FormValueOf<IToDoItem>;

type NewToDoItemFormRawValue = FormValueOf<NewToDoItem>;

type ToDoItemFormDefaults = Pick<NewToDoItem, 'id' | 'isCompleted' | 'plannedDueDate' | 'actualDueDate'>;

type ToDoItemFormGroupContent = {
  id: FormControl<ToDoItemFormRawValue['id'] | NewToDoItem['id']>;
  name: FormControl<ToDoItemFormRawValue['name']>;
  isCompleted: FormControl<ToDoItemFormRawValue['isCompleted']>;
  plannedDueDate: FormControl<ToDoItemFormRawValue['plannedDueDate']>;
  actualDueDate: FormControl<ToDoItemFormRawValue['actualDueDate']>;
  comment: FormControl<ToDoItemFormRawValue['comment']>;
  priority: FormControl<ToDoItemFormRawValue['priority']>;
  applicationUser: FormControl<ToDoItemFormRawValue['applicationUser']>;
};

export type ToDoItemFormGroup = FormGroup<ToDoItemFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ToDoItemFormService {
  createToDoItemFormGroup(toDoItem: ToDoItemFormGroupInput = { id: null }): ToDoItemFormGroup {
    const toDoItemRawValue = this.convertToDoItemToToDoItemRawValue({
      ...this.getFormDefaults(),
      ...toDoItem,
    });
    return new FormGroup<ToDoItemFormGroupContent>({
      id: new FormControl(
        { value: toDoItemRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      name: new FormControl(toDoItemRawValue.name, {
        validators: [Validators.maxLength(100)],
      }),
      isCompleted: new FormControl(toDoItemRawValue.isCompleted),
      plannedDueDate: new FormControl(toDoItemRawValue.plannedDueDate),
      actualDueDate: new FormControl(toDoItemRawValue.actualDueDate),
      comment: new FormControl(toDoItemRawValue.comment, {
        validators: [Validators.maxLength(500)],
      }),
      priority: new FormControl(toDoItemRawValue.priority),
      applicationUser: new FormControl(toDoItemRawValue.applicationUser),
    });
  }

  getToDoItem(form: ToDoItemFormGroup): IToDoItem | NewToDoItem {
    return this.convertToDoItemRawValueToToDoItem(form.getRawValue() as ToDoItemFormRawValue | NewToDoItemFormRawValue);
  }

  resetForm(form: ToDoItemFormGroup, toDoItem: ToDoItemFormGroupInput): void {
    const toDoItemRawValue = this.convertToDoItemToToDoItemRawValue({ ...this.getFormDefaults(), ...toDoItem });
    form.reset(
      {
        ...toDoItemRawValue,
        id: { value: toDoItemRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): ToDoItemFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      isCompleted: false,
      plannedDueDate: currentTime,
      actualDueDate: currentTime,
    };
  }

  private convertToDoItemRawValueToToDoItem(rawToDoItem: ToDoItemFormRawValue | NewToDoItemFormRawValue): IToDoItem | NewToDoItem {
    return {
      ...rawToDoItem,
      plannedDueDate: dayjs(rawToDoItem.plannedDueDate, DATE_TIME_FORMAT),
      actualDueDate: dayjs(rawToDoItem.actualDueDate, DATE_TIME_FORMAT),
    };
  }

  private convertToDoItemToToDoItemRawValue(
    toDoItem: IToDoItem | (Partial<NewToDoItem> & ToDoItemFormDefaults)
  ): ToDoItemFormRawValue | PartialWithRequiredKeyOf<NewToDoItemFormRawValue> {
    return {
      ...toDoItem,
      plannedDueDate: toDoItem.plannedDueDate ? toDoItem.plannedDueDate.format(DATE_TIME_FORMAT) : undefined,
      actualDueDate: toDoItem.actualDueDate ? toDoItem.actualDueDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
