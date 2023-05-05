import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { QueueForDoctorFormService, QueueForDoctorFormGroup } from './queue-for-doctor-form.service';
import { IQueueForDoctor } from '../queue-for-doctor.model';
import { QueueForDoctorService } from '../service/queue-for-doctor.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IDoctor } from 'app/entities/doctor/doctor.model';
import { DoctorService } from 'app/entities/doctor/service/doctor.service';
import { IDepartment } from 'app/entities/department/department.model';
import { DepartmentService } from 'app/entities/department/service/department.service';
import { IHospital } from 'app/entities/hospital/hospital.model';
import { HospitalService } from 'app/entities/hospital/service/hospital.service';

@Component({
  selector: 'jhi-queue-for-doctor-update',
  templateUrl: './queue-for-doctor-update.component.html',
})
export class QueueForDoctorUpdateComponent implements OnInit {
  isSaving = false;
  queueForDoctor: IQueueForDoctor | null = null;

  usersSharedCollection: IUser[] = [];
  doctorsSharedCollection: IDoctor[] = [];
  departmentsSharedCollection: IDepartment[] = [];
  hospitalsSharedCollection: IHospital[] = [];

  editForm: QueueForDoctorFormGroup = this.queueForDoctorFormService.createQueueForDoctorFormGroup();

  constructor(
    protected queueForDoctorService: QueueForDoctorService,
    protected queueForDoctorFormService: QueueForDoctorFormService,
    protected userService: UserService,
    protected doctorService: DoctorService,
    protected departmentService: DepartmentService,
    protected hospitalService: HospitalService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  compareDoctor = (o1: IDoctor | null, o2: IDoctor | null): boolean => this.doctorService.compareDoctor(o1, o2);

  compareDepartment = (o1: IDepartment | null, o2: IDepartment | null): boolean => this.departmentService.compareDepartment(o1, o2);

  compareHospital = (o1: IHospital | null, o2: IHospital | null): boolean => this.hospitalService.compareHospital(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ queueForDoctor }) => {
      this.queueForDoctor = queueForDoctor;
      if (queueForDoctor) {
        this.updateForm(queueForDoctor);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const queueForDoctor = this.queueForDoctorFormService.getQueueForDoctor(this.editForm);
    if (queueForDoctor.id !== null) {
      this.subscribeToSaveResponse(this.queueForDoctorService.update(queueForDoctor));
    } else {
      this.subscribeToSaveResponse(this.queueForDoctorService.create(queueForDoctor));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IQueueForDoctor>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(queueForDoctor: IQueueForDoctor): void {
    this.queueForDoctor = queueForDoctor;
    this.queueForDoctorFormService.resetForm(this.editForm, queueForDoctor);

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(this.usersSharedCollection, queueForDoctor.user);
    this.doctorsSharedCollection = this.doctorService.addDoctorToCollectionIfMissing<IDoctor>(
      this.doctorsSharedCollection,
      queueForDoctor.doctor
    );
    this.departmentsSharedCollection = this.departmentService.addDepartmentToCollectionIfMissing<IDepartment>(
      this.departmentsSharedCollection,
      queueForDoctor.department
    );
    this.hospitalsSharedCollection = this.hospitalService.addHospitalToCollectionIfMissing<IHospital>(
      this.hospitalsSharedCollection,
      queueForDoctor.hospital
    );
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing<IUser>(users, this.queueForDoctor?.user)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));

    this.doctorService
      .query()
      .pipe(map((res: HttpResponse<IDoctor[]>) => res.body ?? []))
      .pipe(map((doctors: IDoctor[]) => this.doctorService.addDoctorToCollectionIfMissing<IDoctor>(doctors, this.queueForDoctor?.doctor)))
      .subscribe((doctors: IDoctor[]) => (this.doctorsSharedCollection = doctors));

    this.departmentService
      .query()
      .pipe(map((res: HttpResponse<IDepartment[]>) => res.body ?? []))
      .pipe(
        map((departments: IDepartment[]) =>
          this.departmentService.addDepartmentToCollectionIfMissing<IDepartment>(departments, this.queueForDoctor?.department)
        )
      )
      .subscribe((departments: IDepartment[]) => (this.departmentsSharedCollection = departments));

    this.hospitalService
      .query()
      .pipe(map((res: HttpResponse<IHospital[]>) => res.body ?? []))
      .pipe(
        map((hospitals: IHospital[]) =>
          this.hospitalService.addHospitalToCollectionIfMissing<IHospital>(hospitals, this.queueForDoctor?.hospital)
        )
      )
      .subscribe((hospitals: IHospital[]) => (this.hospitalsSharedCollection = hospitals));
  }
}
