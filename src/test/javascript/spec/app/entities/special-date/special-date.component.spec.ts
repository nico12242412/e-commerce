import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { TiendaVirtualTestModule } from '../../../test.module';
import { SpecialDateComponent } from 'app/entities/special-date/special-date.component';
import { SpecialDateService } from 'app/entities/special-date/special-date.service';
import { SpecialDate } from 'app/shared/model/special-date.model';

describe('Component Tests', () => {
  describe('SpecialDate Management Component', () => {
    let comp: SpecialDateComponent;
    let fixture: ComponentFixture<SpecialDateComponent>;
    let service: SpecialDateService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [TiendaVirtualTestModule],
        declarations: [SpecialDateComponent],
      })
        .overrideTemplate(SpecialDateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(SpecialDateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(SpecialDateService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new SpecialDate(123)],
            headers,
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.specialDates && comp.specialDates[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
