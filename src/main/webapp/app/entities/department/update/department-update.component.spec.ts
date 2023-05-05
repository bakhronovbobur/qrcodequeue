import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { DepartmentFormService } from './department-form.service';
import { DepartmentService } from '../service/department.service';
import { IDepartment } from '../department.model';
import { IHospital } from 'app/entities/hospital/hospital.model';
import { HospitalService } from 'app/entities/hospital/service/hospital.service';
import { IDoctor } from 'app/entities/doctor/doctor.model';
import { DoctorService } from 'app/entities/doctor/service/doctor.service';

import { DepartmentUpdateComponent } from './department-update.component';

describe('Department Management Update Component', () => {
  let comp: DepartmentUpdateComponent;
  let fixture: ComponentFixture<DepartmentUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let departmentFormService: DepartmentFormService;
  let departmentService: DepartmentService;
  let hospitalService: HospitalService;
  let doctorService: DoctorService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [DepartmentUpdateComponent],
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
      .overrideTemplate(DepartmentUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DepartmentUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    departmentFormService = TestBed.inject(DepartmentFormService);
    departmentService = TestBed.inject(DepartmentService);
    hospitalService = TestBed.inject(HospitalService);
    doctorService = TestBed.inject(DoctorService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Hospital query and add missing value', () => {
      const department: IDepartment = { id: 456 };
      const hospital: IHospital = { id: 98182 };
      department.hospital = hospital;

      const hospitalCollection: IHospital[] = [{ id: 53034 }];
      jest.spyOn(hospitalService, 'query').mockReturnValue(of(new HttpResponse({ body: hospitalCollection })));
      const additionalHospitals = [hospital];
      const expectedCollection: IHospital[] = [...additionalHospitals, ...hospitalCollection];
      jest.spyOn(hospitalService, 'addHospitalToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ department });
      comp.ngOnInit();

      expect(hospitalService.query).toHaveBeenCalled();
      expect(hospitalService.addHospitalToCollectionIfMissing).toHaveBeenCalledWith(
        hospitalCollection,
        ...additionalHospitals.map(expect.objectContaining)
      );
      expect(comp.hospitalsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Doctor query and add missing value', () => {
      const department: IDepartment = { id: 456 };
      const doctors: IDoctor[] = [{ id: 52602 }];
      department.doctors = doctors;

      const doctorCollection: IDoctor[] = [{ id: 36405 }];
      jest.spyOn(doctorService, 'query').mockReturnValue(of(new HttpResponse({ body: doctorCollection })));
      const additionalDoctors = [...doctors];
      const expectedCollection: IDoctor[] = [...additionalDoctors, ...doctorCollection];
      jest.spyOn(doctorService, 'addDoctorToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ department });
      comp.ngOnInit();

      expect(doctorService.query).toHaveBeenCalled();
      expect(doctorService.addDoctorToCollectionIfMissing).toHaveBeenCalledWith(
        doctorCollection,
        ...additionalDoctors.map(expect.objectContaining)
      );
      expect(comp.doctorsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const department: IDepartment = { id: 456 };
      const hospital: IHospital = { id: 78429 };
      department.hospital = hospital;
      const doctors: IDoctor = { id: 27710 };
      department.doctors = [doctors];

      activatedRoute.data = of({ department });
      comp.ngOnInit();

      expect(comp.hospitalsSharedCollection).toContain(hospital);
      expect(comp.doctorsSharedCollection).toContain(doctors);
      expect(comp.department).toEqual(department);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDepartment>>();
      const department = { id: 123 };
      jest.spyOn(departmentFormService, 'getDepartment').mockReturnValue(department);
      jest.spyOn(departmentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ department });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: department }));
      saveSubject.complete();

      // THEN
      expect(departmentFormService.getDepartment).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(departmentService.update).toHaveBeenCalledWith(expect.objectContaining(department));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDepartment>>();
      const department = { id: 123 };
      jest.spyOn(departmentFormService, 'getDepartment').mockReturnValue({ id: null });
      jest.spyOn(departmentService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ department: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: department }));
      saveSubject.complete();

      // THEN
      expect(departmentFormService.getDepartment).toHaveBeenCalled();
      expect(departmentService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDepartment>>();
      const department = { id: 123 };
      jest.spyOn(departmentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ department });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(departmentService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareHospital', () => {
      it('Should forward to hospitalService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(hospitalService, 'compareHospital');
        comp.compareHospital(entity, entity2);
        expect(hospitalService.compareHospital).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareDoctor', () => {
      it('Should forward to doctorService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(doctorService, 'compareDoctor');
        comp.compareDoctor(entity, entity2);
        expect(doctorService.compareDoctor).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
