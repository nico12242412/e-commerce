import { ICart } from 'app/shared/model/cart.model';

export interface IProduct {
  id?: number;
  title?: string;
  description?: string;
  price?: number;
  cart?: ICart;
}

export class Product implements IProduct {
  constructor(public id?: number, public title?: string, public description?: string, public price?: number, public cart?: ICart) {}
}
