import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { TiendaVirtualSharedModule } from 'app/shared/shared.module';
import { SpecialDateComponent } from './special-date.component';
import { SpecialDateDetailComponent } from './special-date-detail.component';
import { SpecialDateUpdateComponent } from './special-date-update.component';
import { SpecialDateDeleteDialogComponent } from './special-date-delete-dialog.component';
import { specialDateRoute } from './special-date.route';

@NgModule({
  imports: [TiendaVirtualSharedModule, RouterModule.forChild(specialDateRoute)],
  declarations: [SpecialDateComponent, SpecialDateDetailComponent, SpecialDateUpdateComponent, SpecialDateDeleteDialogComponent],
  entryComponents: [SpecialDateDeleteDialogComponent],
})
export class TiendaVirtualSpecialDateModule {}
