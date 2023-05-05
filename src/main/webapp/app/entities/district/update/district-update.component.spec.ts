import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { DistrictFormService } from './district-form.service';
import { DistrictService } from '../service/district.service';
import { IDistrict } from '../district.model';
import { IRegion } from 'app/entities/region/region.model';
import { RegionService } from 'app/entities/region/service/region.service';

import { DistrictUpdateComponent } from './district-update.component';

describe('District Management Update Component', () => {
  let comp: DistrictUpdateComponent;
  let fixture: ComponentFixture<DistrictUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let districtFormService: DistrictFormService;
  let districtService: DistrictService;
  let regionService: RegionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [DistrictUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(DistrictUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DistrictUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    districtFormService = TestBed.inject(DistrictFormService);
    districtService = TestBed.inject(DistrictService);
    regionService = TestBed.inject(RegionService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Region query and add missing value', () => {
      const district: IDistrict = { id: 456 };
      const region: IRegion = { id: 19992 };
      district.region = region;

      const regionCollection: IRegion[] = [{ id: 85894 }];
      jest.spyOn(regionService, 'query').mockReturnValue(of(new HttpResponse({ body: regionCollection })));
      const additionalRegions = [region];
      const expectedCollection: IRegion[] = [...additionalRegions, ...regionCollection];
      jest.spyOn(regionService, 'addRegionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ district });
      comp.ngOnInit();

      expect(regionService.query).toHaveBeenCalled();
      expect(regionService.addRegionToCollectionIfMissing).toHaveBeenCalledWith(
        regionCollection,
        ...additionalRegions.map(expect.objectContaining)
      );
      expect(comp.regionsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const district: IDistrict = { id: 456 };
      const region: IRegion = { id: 2460 };
      district.region = region;

      activatedRoute.data = of({ district });
      comp.ngOnInit();

      expect(comp.regionsSharedCollection).toContain(region);
      expect(comp.district).toEqual(district);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDistrict>>();
      const district = { id: 123 };
      jest.spyOn(districtFormService, 'getDistrict').mockReturnValue(district);
      jest.spyOn(districtService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ district });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: district }));
      saveSubject.complete();

      // THEN
      expect(districtFormService.getDistrict).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(districtService.update).toHaveBeenCalledWith(expect.objectContaining(district));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDistrict>>();
      const district = { id: 123 };
      jest.spyOn(districtFormService, 'getDistrict').mockReturnValue({ id: null });
      jest.spyOn(districtService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ district: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: district }));
      saveSubject.complete();

      // THEN
      expect(districtFormService.getDistrict).toHaveBeenCalled();
      expect(districtService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDistrict>>();
      const district = { id: 123 };
      jest.spyOn(districtService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ district });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(districtService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareRegion', () => {
      it('Should forward to regionService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(regionService, 'compareRegion');
        comp.compareRegion(entity, entity2);
        expect(regionService.compareRegion).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
