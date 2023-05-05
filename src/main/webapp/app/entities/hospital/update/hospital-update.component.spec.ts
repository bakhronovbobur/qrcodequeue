import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { HospitalFormService } from './hospital-form.service';
import { HospitalService } from '../service/hospital.service';
import { IHospital } from '../hospital.model';
import { IRegion } from 'app/entities/region/region.model';
import { RegionService } from 'app/entities/region/service/region.service';
import { IDistrict } from 'app/entities/district/district.model';
import { DistrictService } from 'app/entities/district/service/district.service';

import { HospitalUpdateComponent } from './hospital-update.component';

describe('Hospital Management Update Component', () => {
  let comp: HospitalUpdateComponent;
  let fixture: ComponentFixture<HospitalUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let hospitalFormService: HospitalFormService;
  let hospitalService: HospitalService;
  let regionService: RegionService;
  let districtService: DistrictService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [HospitalUpdateComponent],
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
      .overrideTemplate(HospitalUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(HospitalUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    hospitalFormService = TestBed.inject(HospitalFormService);
    hospitalService = TestBed.inject(HospitalService);
    regionService = TestBed.inject(RegionService);
    districtService = TestBed.inject(DistrictService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Region query and add missing value', () => {
      const hospital: IHospital = { id: 456 };
      const region: IRegion = { id: 42644 };
      hospital.region = region;

      const regionCollection: IRegion[] = [{ id: 46015 }];
      jest.spyOn(regionService, 'query').mockReturnValue(of(new HttpResponse({ body: regionCollection })));
      const additionalRegions = [region];
      const expectedCollection: IRegion[] = [...additionalRegions, ...regionCollection];
      jest.spyOn(regionService, 'addRegionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ hospital });
      comp.ngOnInit();

      expect(regionService.query).toHaveBeenCalled();
      expect(regionService.addRegionToCollectionIfMissing).toHaveBeenCalledWith(
        regionCollection,
        ...additionalRegions.map(expect.objectContaining)
      );
      expect(comp.regionsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call District query and add missing value', () => {
      const hospital: IHospital = { id: 456 };
      const district: IDistrict = { id: 48930 };
      hospital.district = district;

      const districtCollection: IDistrict[] = [{ id: 48330 }];
      jest.spyOn(districtService, 'query').mockReturnValue(of(new HttpResponse({ body: districtCollection })));
      const additionalDistricts = [district];
      const expectedCollection: IDistrict[] = [...additionalDistricts, ...districtCollection];
      jest.spyOn(districtService, 'addDistrictToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ hospital });
      comp.ngOnInit();

      expect(districtService.query).toHaveBeenCalled();
      expect(districtService.addDistrictToCollectionIfMissing).toHaveBeenCalledWith(
        districtCollection,
        ...additionalDistricts.map(expect.objectContaining)
      );
      expect(comp.districtsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const hospital: IHospital = { id: 456 };
      const region: IRegion = { id: 66947 };
      hospital.region = region;
      const district: IDistrict = { id: 38628 };
      hospital.district = district;

      activatedRoute.data = of({ hospital });
      comp.ngOnInit();

      expect(comp.regionsSharedCollection).toContain(region);
      expect(comp.districtsSharedCollection).toContain(district);
      expect(comp.hospital).toEqual(hospital);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IHospital>>();
      const hospital = { id: 123 };
      jest.spyOn(hospitalFormService, 'getHospital').mockReturnValue(hospital);
      jest.spyOn(hospitalService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ hospital });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: hospital }));
      saveSubject.complete();

      // THEN
      expect(hospitalFormService.getHospital).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(hospitalService.update).toHaveBeenCalledWith(expect.objectContaining(hospital));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IHospital>>();
      const hospital = { id: 123 };
      jest.spyOn(hospitalFormService, 'getHospital').mockReturnValue({ id: null });
      jest.spyOn(hospitalService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ hospital: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: hospital }));
      saveSubject.complete();

      // THEN
      expect(hospitalFormService.getHospital).toHaveBeenCalled();
      expect(hospitalService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IHospital>>();
      const hospital = { id: 123 };
      jest.spyOn(hospitalService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ hospital });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(hospitalService.update).toHaveBeenCalled();
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

    describe('compareDistrict', () => {
      it('Should forward to districtService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(districtService, 'compareDistrict');
        comp.compareDistrict(entity, entity2);
        expect(districtService.compareDistrict).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
