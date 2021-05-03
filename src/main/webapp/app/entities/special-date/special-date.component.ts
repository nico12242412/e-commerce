import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ISpecialDate } from 'app/shared/model/special-date.model';
import { SpecialDateService } from './special-date.service';
import { SpecialDateDeleteDialogComponent } from './special-date-delete-dialog.component';

@Component({
  selector: 'jhi-special-date',
  templateUrl: './special-date.component.html',
})
export class SpecialDateComponent implements OnInit, OnDestroy {
  specialDates?: ISpecialDate[];
  eventSubscriber?: Subscription;

  constructor(
    protected specialDateService: SpecialDateService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal
  ) {}

  loadAll(): void {
    this.specialDateService.query().subscribe((res: HttpResponse<ISpecialDate[]>) => (this.specialDates = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInSpecialDates();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: ISpecialDate): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInSpecialDates(): void {
    this.eventSubscriber = this.eventManager.subscribe('specialDateListModification', () => this.loadAll());
  }

  delete(specialDate: ISpecialDate): void {
    const modalRef = this.modalService.open(SpecialDateDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.specialDate = specialDate;
  }
}
