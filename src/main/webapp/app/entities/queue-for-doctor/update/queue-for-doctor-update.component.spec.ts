import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { QueueForDoctorFormService } from './queue-for-doctor-form.service';
import { QueueForDoctorService } from '../service/queue-for-doctor.service';
import { IQueueForDoctor } from '../queue-for-doctor.model';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IDoctor } from 'app/entities/doctor/doctor.model';
import { DoctorService } from 'app/entities/doctor/service/doctor.service';
import { IDepartment } from 'app/entities/department/department.model';
import { DepartmentService } from 'app/entities/department/service/department.service';
import { IHospital } from 'app/entities/hospital/hospital.model';
import { HospitalService } from 'app/entities/hospital/service/hospital.service';

import { QueueForDoctorUpdateComponent } from './queue-for-doctor-update.component';

describe('QueueForDoctor Management Update Component', () => {
  let comp: QueueForDoctorUpdateComponent;
  let fixture: ComponentFixture<QueueForDoctorUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let queueForDoctorFormService: QueueForDoctorFormService;
  let queueForDoctorService: QueueForDoctorService;
  let userService: UserService;
  let doctorService: DoctorService;
  let departmentService: DepartmentService;
  let hospitalService: HospitalService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [QueueForDoctorUpdateComponent],
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
      .overrideTemplate(QueueForDoctorUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(QueueForDoctorUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    queueForDoctorFormService = TestBed.inject(QueueForDoctorFormService);
    queueForDoctorService = TestBed.inject(QueueForDoctorService);
    userService = TestBed.inject(UserService);
    doctorService = TestBed.inject(DoctorService);
    departmentService = TestBed.inject(DepartmentService);
    hospitalService = TestBed.inject(HospitalService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call User query and add missing value', () => {
      const queueForDoctor: IQueueForDoctor = { id: 456 };
      const user: IUser = { id: 12997 };
      queueForDoctor.user = user;

      const userCollection: IUser[] = [{ id: 1216 }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [user];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ queueForDoctor });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(expect.objectContaining)
      );
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Doctor query and add missing value', () => {
      const queueForDoctor: IQueueForDoctor = { id: 456 };
      const doctor: IDoctor = { id: 41373 };
      queueForDoctor.doctor = doctor;

      const doctorCollection: IDoctor[] = [{ id: 17011 }];
      jest.spyOn(doctorService, 'query').mockReturnValue(of(new HttpResponse({ body: doctorCollection })));
      const additionalDoctors = [doctor];
      const expectedCollection: IDoctor[] = [...additionalDoctors, ...doctorCollection];
      jest.spyOn(doctorService, 'addDoctorToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ queueForDoctor });
      comp.ngOnInit();

      expect(doctorService.query).toHaveBeenCalled();
      expect(doctorService.addDoctorToCollectionIfMissing).toHaveBeenCalledWith(
        doctorCollection,
        ...additionalDoctors.map(expect.objectContaining)
      );
      expect(comp.doctorsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Department query and add missing value', () => {
      const queueForDoctor: IQueueForDoctor = { id: 456 };
      const department: IDepartment = { id: 13146 };
      queueForDoctor.department = department;

      const departmentCollection: IDepartment[] = [{ id: 1742 }];
      jest.spyOn(departmentService, 'query').mockReturnValue(of(new HttpResponse({ body: departmentCollection })));
      const additionalDepartments = [department];
      const expectedCollection: IDepartment[] = [...additionalDepartments, ...departmentCollection];
      jest.spyOn(departmentService, 'addDepartmentToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ queueForDoctor });
      comp.ngOnInit();

      expect(departmentService.query).toHaveBeenCalled();
      expect(departmentService.addDepartmentToCollectionIfMissing).toHaveBeenCalledWith(
        departmentCollection,
        ...additionalDepartments.map(expect.objectContaining)
      );
      expect(comp.departmentsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Hospital query and add missing value', () => {
      const queueForDoctor: IQueueForDoctor = { id: 456 };
      const hospital: IHospital = { id: 82335 };
      queueForDoctor.hospital = hospital;

      const hospitalCollection: IHospital[] = [{ id: 68643 }];
      jest.spyOn(hospitalService, 'query').mockReturnValue(of(new HttpResponse({ body: hospitalCollection })));
      const additionalHospitals = [hospital];
      const expectedCollection: IHospital[] = [...additionalHospitals, ...hospitalCollection];
      jest.spyOn(hospitalService, 'addHospitalToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ queueForDoctor });
      comp.ngOnInit();

      expect(hospitalService.query).toHaveBeenCalled();
      expect(hospitalService.addHospitalToCollectionIfMissing).toHaveBeenCalledWith(
        hospitalCollection,
        ...additionalHospitals.map(expect.objectContaining)
      );
      expect(comp.hospitalsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const queueForDoctor: IQueueForDoctor = { id: 456 };
      const user: IUser = { id: 93207 };
      queueForDoctor.user = user;
      const doctor: IDoctor = { id: 40257 };
      queueForDoctor.doctor = doctor;
      const department: IDepartment = { id: 87716 };
      queueForDoctor.department = department;
      const hospital: IHospital = { id: 65185 };
      queueForDoctor.hospital = hospital;

      activatedRoute.data = of({ queueForDoctor });
      comp.ngOnInit();

      expect(comp.usersSharedCollection).toContain(user);
      expect(comp.doctorsSharedCollection).toContain(doctor);
      expect(comp.departmentsSharedCollection).toContain(department);
      expect(comp.hospitalsSharedCollection).toContain(hospital);
      expect(comp.queueForDoctor).toEqual(queueForDoctor);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IQueueForDoctor>>();
      const queueForDoctor = { id: 123 };
      jest.spyOn(queueForDoctorFormService, 'getQueueForDoctor').mockReturnValue(queueForDoctor);
      jest.spyOn(queueForDoctorService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ queueForDoctor });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: queueForDoctor }));
      saveSubject.complete();

      // THEN
      expect(queueForDoctorFormService.getQueueForDoctor).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(queueForDoctorService.update).toHaveBeenCalledWith(expect.objectContaining(queueForDoctor));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IQueueForDoctor>>();
      const queueForDoctor = { id: 123 };
      jest.spyOn(queueForDoctorFormService, 'getQueueForDoctor').mockReturnValue({ id: null });
      jest.spyOn(queueForDoctorService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ queueForDoctor: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: queueForDoctor }));
      saveSubject.complete();

      // THEN
      expect(queueForDoctorFormService.getQueueForDoctor).toHaveBeenCalled();
      expect(queueForDoctorService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IQueueForDoctor>>();
      const queueForDoctor = { id: 123 };
      jest.spyOn(queueForDoctorService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ queueForDoctor });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(queueForDoctorService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareUser', () => {
      it('Should forward to userService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(userService, 'compareUser');
        comp.compareUser(entity, entity2);
        expect(userService.compareUser).toHaveBeenCalledWith(entity, entity2);
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

    describe('compareDepartment', () => {
      it('Should forward to departmentService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(departmentService, 'compareDepartment');
        comp.compareDepartment(entity, entity2);
        expect(departmentService.compareDepartment).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareHospital', () => {
      it('Should forward to hospitalService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(hospitalService, 'compareHospital');
        comp.compareHospital(entity, entity2);
        expect(hospitalService.compareHospital).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
