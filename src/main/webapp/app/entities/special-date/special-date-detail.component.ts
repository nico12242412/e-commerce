import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISpecialDate } from 'app/shared/model/special-date.model';

@Component({
  selector: 'jhi-special-date-detail',
  templateUrl: './special-date-detail.component.html',
})
export class SpecialDateDetailComponent implements OnInit {
  specialDate: ISpecialDate | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ specialDate }) => (this.specialDate = specialDate));
  }

  previousState(): void {
    window.history.back();
  }
}
