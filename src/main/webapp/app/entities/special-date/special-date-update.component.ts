import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { ISpecialDate, SpecialDate } from 'app/shared/model/special-date.model';
import { SpecialDateService } from './special-date.service';

@Component({
  selector: 'jhi-special-date-update',
  templateUrl: './special-date-update.component.html',
})
export class SpecialDateUpdateComponent implements OnInit {
  isSaving = false;
  beginDateDp: any;
  finishDateDp: any;

  editForm = this.fb.group({
    id: [],
    beginDate: [],
    finishDate: [],
  });

  constructor(protected specialDateService: SpecialDateService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ specialDate }) => {
      this.updateForm(specialDate);
    });
  }

  updateForm(specialDate: ISpecialDate): void {
    this.editForm.patchValue({
      id: specialDate.id,
      beginDate: specialDate.beginDate,
      finishDate: specialDate.finishDate,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const specialDate = this.createFromForm();
    if (specialDate.id !== undefined) {
      this.subscribeToSaveResponse(this.specialDateService.update(specialDate));
    } else {
      this.subscribeToSaveResponse(this.specialDateService.create(specialDate));
    }
  }

  private createFromForm(): ISpecialDate {
    return {
      ...new SpecialDate(),
      id: this.editForm.get(['id'])!.value,
      beginDate: this.editForm.get(['beginDate'])!.value,
      finishDate: this.editForm.get(['finishDate'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISpecialDate>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }
}
