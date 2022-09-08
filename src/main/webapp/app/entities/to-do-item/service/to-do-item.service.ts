import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IToDoItem, NewToDoItem } from '../to-do-item.model';

export type PartialUpdateToDoItem = Partial<IToDoItem> & Pick<IToDoItem, 'id'>;

type RestOf<T extends IToDoItem | NewToDoItem> = Omit<T, 'plannedDueDate' | 'actualDueDate'> & {
  plannedDueDate?: string | null;
  actualDueDate?: string | null;
};

export type RestToDoItem = RestOf<IToDoItem>;

export type NewRestToDoItem = RestOf<NewToDoItem>;

export type PartialUpdateRestToDoItem = RestOf<PartialUpdateToDoItem>;

export type EntityResponseType = HttpResponse<IToDoItem>;
export type EntityArrayResponseType = HttpResponse<IToDoItem[]>;

@Injectable({ providedIn: 'root' })
export class ToDoItemService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/to-do-items');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(toDoItem: NewToDoItem): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(toDoItem);
    return this.http
      .post<RestToDoItem>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(toDoItem: IToDoItem): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(toDoItem);
    return this.http
      .put<RestToDoItem>(`${this.resourceUrl}/${this.getToDoItemIdentifier(toDoItem)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(toDoItem: PartialUpdateToDoItem): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(toDoItem);
    return this.http
      .patch<RestToDoItem>(`${this.resourceUrl}/${this.getToDoItemIdentifier(toDoItem)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestToDoItem>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestToDoItem[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getToDoItemIdentifier(toDoItem: Pick<IToDoItem, 'id'>): number {
    return toDoItem.id;
  }

  compareToDoItem(o1: Pick<IToDoItem, 'id'> | null, o2: Pick<IToDoItem, 'id'> | null): boolean {
    return o1 && o2 ? this.getToDoItemIdentifier(o1) === this.getToDoItemIdentifier(o2) : o1 === o2;
  }

  addToDoItemToCollectionIfMissing<Type extends Pick<IToDoItem, 'id'>>(
    toDoItemCollection: Type[],
    ...toDoItemsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const toDoItems: Type[] = toDoItemsToCheck.filter(isPresent);
    if (toDoItems.length > 0) {
      const toDoItemCollectionIdentifiers = toDoItemCollection.map(toDoItemItem => this.getToDoItemIdentifier(toDoItemItem)!);
      const toDoItemsToAdd = toDoItems.filter(toDoItemItem => {
        const toDoItemIdentifier = this.getToDoItemIdentifier(toDoItemItem);
        if (toDoItemCollectionIdentifiers.includes(toDoItemIdentifier)) {
          return false;
        }
        toDoItemCollectionIdentifiers.push(toDoItemIdentifier);
        return true;
      });
      return [...toDoItemsToAdd, ...toDoItemCollection];
    }
    return toDoItemCollection;
  }

  protected convertDateFromClient<T extends IToDoItem | NewToDoItem | PartialUpdateToDoItem>(toDoItem: T): RestOf<T> {
    return {
      ...toDoItem,
      plannedDueDate: toDoItem.plannedDueDate?.toJSON() ?? null,
      actualDueDate: toDoItem.actualDueDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restToDoItem: RestToDoItem): IToDoItem {
    return {
      ...restToDoItem,
      plannedDueDate: restToDoItem.plannedDueDate ? dayjs(restToDoItem.plannedDueDate) : undefined,
      actualDueDate: restToDoItem.actualDueDate ? dayjs(restToDoItem.actualDueDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestToDoItem>): HttpResponse<IToDoItem> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestToDoItem[]>): HttpResponse<IToDoItem[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
