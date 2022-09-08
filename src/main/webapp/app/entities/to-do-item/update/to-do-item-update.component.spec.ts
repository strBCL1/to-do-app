import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ToDoItemFormService } from './to-do-item-form.service';
import { ToDoItemService } from '../service/to-do-item.service';
import { IToDoItem } from '../to-do-item.model';
import { IApplicationUser } from 'app/entities/application-user/application-user.model';
import { ApplicationUserService } from 'app/entities/application-user/service/application-user.service';

import { ToDoItemUpdateComponent } from './to-do-item-update.component';

describe('ToDoItem Management Update Component', () => {
  let comp: ToDoItemUpdateComponent;
  let fixture: ComponentFixture<ToDoItemUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let toDoItemFormService: ToDoItemFormService;
  let toDoItemService: ToDoItemService;
  let applicationUserService: ApplicationUserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ToDoItemUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(ToDoItemUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ToDoItemUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    toDoItemFormService = TestBed.inject(ToDoItemFormService);
    toDoItemService = TestBed.inject(ToDoItemService);
    applicationUserService = TestBed.inject(ApplicationUserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call ApplicationUser query and add missing value', () => {
      const toDoItem: IToDoItem = { id: 456 };
      const applicationUser: IApplicationUser = { id: 18961 };
      toDoItem.applicationUser = applicationUser;

      const applicationUserCollection: IApplicationUser[] = [{ id: 56748 }];
      jest.spyOn(applicationUserService, 'query').mockReturnValue(of(new HttpResponse({ body: applicationUserCollection })));
      const additionalApplicationUsers = [applicationUser];
      const expectedCollection: IApplicationUser[] = [...additionalApplicationUsers, ...applicationUserCollection];
      jest.spyOn(applicationUserService, 'addApplicationUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ toDoItem });
      comp.ngOnInit();

      expect(applicationUserService.query).toHaveBeenCalled();
      expect(applicationUserService.addApplicationUserToCollectionIfMissing).toHaveBeenCalledWith(
        applicationUserCollection,
        ...additionalApplicationUsers.map(expect.objectContaining)
      );
      expect(comp.applicationUsersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const toDoItem: IToDoItem = { id: 456 };
      const applicationUser: IApplicationUser = { id: 73910 };
      toDoItem.applicationUser = applicationUser;

      activatedRoute.data = of({ toDoItem });
      comp.ngOnInit();

      expect(comp.applicationUsersSharedCollection).toContain(applicationUser);
      expect(comp.toDoItem).toEqual(toDoItem);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IToDoItem>>();
      const toDoItem = { id: 123 };
      jest.spyOn(toDoItemFormService, 'getToDoItem').mockReturnValue(toDoItem);
      jest.spyOn(toDoItemService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ toDoItem });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: toDoItem }));
      saveSubject.complete();

      // THEN
      expect(toDoItemFormService.getToDoItem).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(toDoItemService.update).toHaveBeenCalledWith(expect.objectContaining(toDoItem));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IToDoItem>>();
      const toDoItem = { id: 123 };
      jest.spyOn(toDoItemFormService, 'getToDoItem').mockReturnValue({ id: null });
      jest.spyOn(toDoItemService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ toDoItem: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: toDoItem }));
      saveSubject.complete();

      // THEN
      expect(toDoItemFormService.getToDoItem).toHaveBeenCalled();
      expect(toDoItemService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IToDoItem>>();
      const toDoItem = { id: 123 };
      jest.spyOn(toDoItemService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ toDoItem });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(toDoItemService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareApplicationUser', () => {
      it('Should forward to applicationUserService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(applicationUserService, 'compareApplicationUser');
        comp.compareApplicationUser(entity, entity2);
        expect(applicationUserService.compareApplicationUser).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
