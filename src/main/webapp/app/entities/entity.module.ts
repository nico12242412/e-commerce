import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'customer',
        loadChildren: () => import('./customer/customer.module').then(m => m.TiendaVirtualCustomerModule),
      },
      {
        path: 'product',
        loadChildren: () => import('./product/product.module').then(m => m.TiendaVirtualProductModule),
      },
      {
        path: 'cart',
        loadChildren: () => import('./cart/cart.module').then(m => m.TiendaVirtualCartModule),
      },
      {
        path: 'special-date',
        loadChildren: () => import('./special-date/special-date.module').then(m => m.TiendaVirtualSpecialDateModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class TiendaVirtualEntityModule {}
