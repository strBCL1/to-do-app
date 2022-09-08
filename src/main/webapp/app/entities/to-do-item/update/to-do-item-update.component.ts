import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ToDoItemFormService, ToDoItemFormGroup } from './to-do-item-form.service';
import { IToDoItem } from '../to-do-item.model';
import { ToDoItemService } from '../service/to-do-item.service';
import { IApplicationUser } from 'app/entities/application-user/application-user.model';
import { ApplicationUserService } from 'app/entities/application-user/service/application-user.service';
import { ToDoItemPriority } from 'app/entities/enumerations/to-do-item-priority.model';

@Component({
  selector: 'jhi-to-do-item-update',
  templateUrl: './to-do-item-update.component.html',
})
export class ToDoItemUpdateComponent implements OnInit {
  isSaving = false;
  toDoItem: IToDoItem | null = null;
  toDoItemPriorityValues = Object.keys(ToDoItemPriority);

  applicationUsersSharedCollection: IApplicationUser[] = [];

  editForm: ToDoItemFormGroup = this.toDoItemFormService.createToDoItemFormGroup();

  constructor(
    protected toDoItemService: ToDoItemService,
    protected toDoItemFormService: ToDoItemFormService,
    protected applicationUserService: ApplicationUserService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareApplicationUser = (o1: IApplicationUser | null, o2: IApplicationUser | null): boolean =>
    this.applicationUserService.compareApplicationUser(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ toDoItem }) => {
      this.toDoItem = toDoItem;
      if (toDoItem) {
        this.updateForm(toDoItem);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const toDoItem = this.toDoItemFormService.getToDoItem(this.editForm);
    if (toDoItem.id !== null) {
      this.subscribeToSaveResponse(this.toDoItemService.update(toDoItem));
    } else {
      this.subscribeToSaveResponse(this.toDoItemService.create(toDoItem));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IToDoItem>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(toDoItem: IToDoItem): void {
    this.toDoItem = toDoItem;
    this.toDoItemFormService.resetForm(this.editForm, toDoItem);

    this.applicationUsersSharedCollection = this.applicationUserService.addApplicationUserToCollectionIfMissing<IApplicationUser>(
      this.applicationUsersSharedCollection,
      toDoItem.applicationUser
    );
  }

  protected loadRelationshipsOptions(): void {
    this.applicationUserService
      .query()
      .pipe(map((res: HttpResponse<IApplicationUser[]>) => res.body ?? []))
      .pipe(
        map((applicationUsers: IApplicationUser[]) =>
          this.applicationUserService.addApplicationUserToCollectionIfMissing<IApplicationUser>(
            applicationUsers,
            this.toDoItem?.applicationUser
          )
        )
      )
      .subscribe((applicationUsers: IApplicationUser[]) => (this.applicationUsersSharedCollection = applicationUsers));
  }
}
