import { Moment } from 'moment';
import { IProduct } from 'app/shared/model/product.model';
import { ICustomer } from 'app/shared/model/customer.model';
import { CartStatus } from 'app/shared/model/enumerations/cart-status.model';

export interface ICart {
  id?: number;
  totalPrice?: number;
  date?: Moment;
  status?: CartStatus;
  products?: IProduct[];
  customer?: ICustomer;
}

export class Cart implements ICart {
  constructor(
    public id?: number,
    public totalPrice?: number,
    public date?: Moment,
    public status?: CartStatus,
    public products?: IProduct[],
    public customer?: ICustomer
  ) {}
}
