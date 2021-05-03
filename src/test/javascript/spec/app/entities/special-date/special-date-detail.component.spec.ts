import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TiendaVirtualTestModule } from '../../../test.module';
import { SpecialDateDetailComponent } from 'app/entities/special-date/special-date-detail.component';
import { SpecialDate } from 'app/shared/model/special-date.model';

describe('Component Tests', () => {
  describe('SpecialDate Management Detail Component', () => {
    let comp: SpecialDateDetailComponent;
    let fixture: ComponentFixture<SpecialDateDetailComponent>;
    const route = ({ data: of({ specialDate: new SpecialDate(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [TiendaVirtualTestModule],
        declarations: [SpecialDateDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(SpecialDateDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(SpecialDateDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load specialDate on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.specialDate).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
