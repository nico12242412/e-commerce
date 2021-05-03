import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { ICart } from 'app/shared/model/cart.model';

type EntityResponseType = HttpResponse<ICart>;
type EntityArrayResponseType = HttpResponse<ICart[]>;

@Injectable({ providedIn: 'root' })
export class CartService {
  public resourceUrl = SERVER_API_URL + 'api/carts';

  constructor(protected http: HttpClient) {}

  create(cart: ICart): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(cart);
    return this.http
      .post<ICart>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(cart: ICart): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(cart);
    return this.http
      .put<ICart>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ICart>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ICart[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(cart: ICart): ICart {
    const copy: ICart = Object.assign({}, cart, {
      date: cart.date && cart.date.isValid() ? cart.date.format(DATE_FORMAT) : undefined,
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.date = res.body.date ? moment(res.body.date) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((cart: ICart) => {
        cart.date = cart.date ? moment(cart.date) : undefined;
      });
    }
    return res;
  }
}
