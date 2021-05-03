import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ISpecialDate } from 'app/shared/model/special-date.model';
import { SpecialDateService } from './special-date.service';

@Component({
  templateUrl: './special-date-delete-dialog.component.html',
})
export class SpecialDateDeleteDialogComponent {
  specialDate?: ISpecialDate;

  constructor(
    protected specialDateService: SpecialDateService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.specialDateService.delete(id).subscribe(() => {
      this.eventManager.broadcast('specialDateListModification');
      this.activeModal.close();
    });
  }
}
