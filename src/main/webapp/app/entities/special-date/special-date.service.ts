import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { ISpecialDate } from 'app/shared/model/special-date.model';

type EntityResponseType = HttpResponse<ISpecialDate>;
type EntityArrayResponseType = HttpResponse<ISpecialDate[]>;

@Injectable({ providedIn: 'root' })
export class SpecialDateService {
  public resourceUrl = SERVER_API_URL + 'api/special-dates';

  constructor(protected http: HttpClient) {}

  create(specialDate: ISpecialDate): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(specialDate);
    return this.http
      .post<ISpecialDate>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(specialDate: ISpecialDate): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(specialDate);
    return this.http
      .put<ISpecialDate>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ISpecialDate>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ISpecialDate[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(specialDate: ISpecialDate): ISpecialDate {
    const copy: ISpecialDate = Object.assign({}, specialDate, {
      beginDate: specialDate.beginDate && specialDate.beginDate.isValid() ? specialDate.beginDate.format(DATE_FORMAT) : undefined,
      finishDate: specialDate.finishDate && specialDate.finishDate.isValid() ? specialDate.finishDate.format(DATE_FORMAT) : undefined,
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.beginDate = res.body.beginDate ? moment(res.body.beginDate) : undefined;
      res.body.finishDate = res.body.finishDate ? moment(res.body.finishDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((specialDate: ISpecialDate) => {
        specialDate.beginDate = specialDate.beginDate ? moment(specialDate.beginDate) : undefined;
        specialDate.finishDate = specialDate.finishDate ? moment(specialDate.finishDate) : undefined;
      });
    }
    return res;
  }
}
