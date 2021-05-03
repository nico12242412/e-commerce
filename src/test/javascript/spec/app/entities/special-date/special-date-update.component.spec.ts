import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { TiendaVirtualTestModule } from '../../../test.module';
import { SpecialDateUpdateComponent } from 'app/entities/special-date/special-date-update.component';
import { SpecialDateService } from 'app/entities/special-date/special-date.service';
import { SpecialDate } from 'app/shared/model/special-date.model';

describe('Component Tests', () => {
  describe('SpecialDate Management Update Component', () => {
    let comp: SpecialDateUpdateComponent;
    let fixture: ComponentFixture<SpecialDateUpdateComponent>;
    let service: SpecialDateService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [TiendaVirtualTestModule],
        declarations: [SpecialDateUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(SpecialDateUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(SpecialDateUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(SpecialDateService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new SpecialDate(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new SpecialDate();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
