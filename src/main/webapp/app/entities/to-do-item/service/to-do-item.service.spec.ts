import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IToDoItem } from '../to-do-item.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../to-do-item.test-samples';

import { ToDoItemService, RestToDoItem } from './to-do-item.service';

const requireRestSample: RestToDoItem = {
  ...sampleWithRequiredData,
  plannedDueDate: sampleWithRequiredData.plannedDueDate?.toJSON(),
  actualDueDate: sampleWithRequiredData.actualDueDate?.toJSON(),
};

describe('ToDoItem Service', () => {
  let service: ToDoItemService;
  let httpMock: HttpTestingController;
  let expectedResult: IToDoItem | IToDoItem[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ToDoItemService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a ToDoItem', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const toDoItem = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(toDoItem).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ToDoItem', () => {
      const toDoItem = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(toDoItem).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ToDoItem', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ToDoItem', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ToDoItem', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addToDoItemToCollectionIfMissing', () => {
      it('should add a ToDoItem to an empty array', () => {
        const toDoItem: IToDoItem = sampleWithRequiredData;
        expectedResult = service.addToDoItemToCollectionIfMissing([], toDoItem);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(toDoItem);
      });

      it('should not add a ToDoItem to an array that contains it', () => {
        const toDoItem: IToDoItem = sampleWithRequiredData;
        const toDoItemCollection: IToDoItem[] = [
          {
            ...toDoItem,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addToDoItemToCollectionIfMissing(toDoItemCollection, toDoItem);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ToDoItem to an array that doesn't contain it", () => {
        const toDoItem: IToDoItem = sampleWithRequiredData;
        const toDoItemCollection: IToDoItem[] = [sampleWithPartialData];
        expectedResult = service.addToDoItemToCollectionIfMissing(toDoItemCollection, toDoItem);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(toDoItem);
      });

      it('should add only unique ToDoItem to an array', () => {
        const toDoItemArray: IToDoItem[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const toDoItemCollection: IToDoItem[] = [sampleWithRequiredData];
        expectedResult = service.addToDoItemToCollectionIfMissing(toDoItemCollection, ...toDoItemArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const toDoItem: IToDoItem = sampleWithRequiredData;
        const toDoItem2: IToDoItem = sampleWithPartialData;
        expectedResult = service.addToDoItemToCollectionIfMissing([], toDoItem, toDoItem2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(toDoItem);
        expect(expectedResult).toContain(toDoItem2);
      });

      it('should accept null and undefined values', () => {
        const toDoItem: IToDoItem = sampleWithRequiredData;
        expectedResult = service.addToDoItemToCollectionIfMissing([], null, toDoItem, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(toDoItem);
      });

      it('should return initial array if no ToDoItem is added', () => {
        const toDoItemCollection: IToDoItem[] = [sampleWithRequiredData];
        expectedResult = service.addToDoItemToCollectionIfMissing(toDoItemCollection, undefined, null);
        expect(expectedResult).toEqual(toDoItemCollection);
      });
    });

    describe('compareToDoItem', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareToDoItem(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareToDoItem(entity1, entity2);
        const compareResult2 = service.compareToDoItem(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareToDoItem(entity1, entity2);
        const compareResult2 = service.compareToDoItem(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareToDoItem(entity1, entity2);
        const compareResult2 = service.compareToDoItem(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
