import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { ICart, Cart } from 'app/shared/model/cart.model';
import { CartService } from './cart.service';
import { ICustomer } from 'app/shared/model/customer.model';
import { CustomerService } from 'app/entities/customer/customer.service';

@Component({
  selector: 'jhi-cart-update',
  templateUrl: './cart-update.component.html',
})
export class CartUpdateComponent implements OnInit {
  isSaving = false;
  customers: ICustomer[] = [];
  dateDp: any;

  editForm = this.fb.group({
    id: [],
    totalPrice: [],
    date: [],
    status: [],
    customer: [],
  });

  constructor(
    protected cartService: CartService,
    protected customerService: CustomerService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cart }) => {
      this.updateForm(cart);

      this.customerService.query().subscribe((res: HttpResponse<ICustomer[]>) => (this.customers = res.body || []));
    });
  }

  updateForm(cart: ICart): void {
    this.editForm.patchValue({
      id: cart.id,
      totalPrice: cart.totalPrice,
      date: cart.date,
      status: cart.status,
      customer: cart.customer,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const cart = this.createFromForm();
    if (cart.id !== undefined) {
      this.subscribeToSaveResponse(this.cartService.update(cart));
    } else {
      this.subscribeToSaveResponse(this.cartService.create(cart));
    }
  }

  private createFromForm(): ICart {
    return {
      ...new Cart(),
      id: this.editForm.get(['id'])!.value,
      totalPrice: this.editForm.get(['totalPrice'])!.value,
      date: this.editForm.get(['date'])!.value,
      status: this.editForm.get(['status'])!.value,
      customer: this.editForm.get(['customer'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICart>>): void {
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

  trackById(index: number, item: ICustomer): any {
    return item.id;
  }
}
