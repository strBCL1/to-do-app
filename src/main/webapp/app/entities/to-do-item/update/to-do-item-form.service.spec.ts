import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../to-do-item.test-samples';

import { ToDoItemFormService } from './to-do-item-form.service';

describe('ToDoItem Form Service', () => {
  let service: ToDoItemFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ToDoItemFormService);
  });

  describe('Service methods', () => {
    describe('createToDoItemFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createToDoItemFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            isCompleted: expect.any(Object),
            plannedDueDate: expect.any(Object),
            actualDueDate: expect.any(Object),
            comment: expect.any(Object),
            priority: expect.any(Object),
            applicationUser: expect.any(Object),
          })
        );
      });

      it('passing IToDoItem should create a new form with FormGroup', () => {
        const formGroup = service.createToDoItemFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            isCompleted: expect.any(Object),
            plannedDueDate: expect.any(Object),
            actualDueDate: expect.any(Object),
            comment: expect.any(Object),
            priority: expect.any(Object),
            applicationUser: expect.any(Object),
          })
        );
      });
    });

    describe('getToDoItem', () => {
      it('should return NewToDoItem for default ToDoItem initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createToDoItemFormGroup(sampleWithNewData);

        const toDoItem = service.getToDoItem(formGroup) as any;

        expect(toDoItem).toMatchObject(sampleWithNewData);
      });

      it('should return NewToDoItem for empty ToDoItem initial value', () => {
        const formGroup = service.createToDoItemFormGroup();

        const toDoItem = service.getToDoItem(formGroup) as any;

        expect(toDoItem).toMatchObject({});
      });

      it('should return IToDoItem', () => {
        const formGroup = service.createToDoItemFormGroup(sampleWithRequiredData);

        const toDoItem = service.getToDoItem(formGroup) as any;

        expect(toDoItem).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IToDoItem should not enable id FormControl', () => {
        const formGroup = service.createToDoItemFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewToDoItem should disable id FormControl', () => {
        const formGroup = service.createToDoItemFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
