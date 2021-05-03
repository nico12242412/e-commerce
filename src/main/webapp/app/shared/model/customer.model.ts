import { ICart } from 'app/shared/model/cart.model';

export interface ICustomer {
  id?: number;
  firstName?: string;
  lastName?: string;
  email?: string;
  telephone?: string;
  carts?: ICart[];
}

export class Customer implements ICustomer {
  constructor(
    public id?: number,
    public firstName?: string,
    public lastName?: string,
    public email?: string,
    public telephone?: string,
    public carts?: ICart[]
  ) {}
}
