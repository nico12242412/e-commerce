import { Moment } from 'moment';

export interface ISpecialDate {
  id?: number;
  beginDate?: Moment;
  finishDate?: Moment;
}

export class SpecialDate implements ISpecialDate {
  constructor(public id?: number, public beginDate?: Moment, public finishDate?: Moment) {}
}
