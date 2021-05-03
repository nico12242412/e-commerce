import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { ISpecialDate, SpecialDate } from 'app/shared/model/special-date.model';
import { SpecialDateService } from './special-date.service';
import { SpecialDateComponent } from './special-date.component';
import { SpecialDateDetailComponent } from './special-date-detail.component';
import { SpecialDateUpdateComponent } from './special-date-update.component';

@Injectable({ providedIn: 'root' })
export class SpecialDateResolve implements Resolve<ISpecialDate> {
  constructor(private service: SpecialDateService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISpecialDate> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((specialDate: HttpResponse<SpecialDate>) => {
          if (specialDate.body) {
            return of(specialDate.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new SpecialDate());
  }
}

export const specialDateRoute: Routes = [
  {
    path: '',
    component: SpecialDateComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'tiendaVirtualApp.specialDate.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SpecialDateDetailComponent,
    resolve: {
      specialDate: SpecialDateResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'tiendaVirtualApp.specialDate.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SpecialDateUpdateComponent,
    resolve: {
      specialDate: SpecialDateResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'tiendaVirtualApp.specialDate.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SpecialDateUpdateComponent,
    resolve: {
      specialDate: SpecialDateResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'tiendaVirtualApp.specialDate.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
