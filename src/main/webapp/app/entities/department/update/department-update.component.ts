import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { DepartmentFormService, DepartmentFormGroup } from './department-form.service';
import { IDepartment } from '../department.model';
import { DepartmentService } from '../service/department.service';
import { IHospital } from 'app/entities/hospital/hospital.model';
import { HospitalService } from 'app/entities/hospital/service/hospital.service';
import { IDoctor } from 'app/entities/doctor/doctor.model';
import { DoctorService } from 'app/entities/doctor/service/doctor.service';

@Component({
  selector: 'jhi-department-update',
  templateUrl: './department-update.component.html',
})
export class DepartmentUpdateComponent implements OnInit {
  isSaving = false;
  department: IDepartment | null = null;

  hospitalsSharedCollection: IHospital[] = [];
  doctorsSharedCollection: IDoctor[] = [];

  editForm: DepartmentFormGroup = this.departmentFormService.createDepartmentFormGroup();

  constructor(
    protected departmentService: DepartmentService,
    protected departmentFormService: DepartmentFormService,
    protected hospitalService: HospitalService,
    protected doctorService: DoctorService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareHospital = (o1: IHospital | null, o2: IHospital | null): boolean => this.hospitalService.compareHospital(o1, o2);

  compareDoctor = (o1: IDoctor | null, o2: IDoctor | null): boolean => this.doctorService.compareDoctor(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ department }) => {
      this.department = department;
      if (department) {
        this.updateForm(department);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const department = this.departmentFormService.getDepartment(this.editForm);
    if (department.id !== null) {
      this.subscribeToSaveResponse(this.departmentService.update(department));
    } else {
      this.subscribeToSaveResponse(this.departmentService.create(department));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDepartment>>): void {
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

  protected updateForm(department: IDepartment): void {
    this.department = department;
    this.departmentFormService.resetForm(this.editForm, department);

    this.hospitalsSharedCollection = this.hospitalService.addHospitalToCollectionIfMissing<IHospital>(
      this.hospitalsSharedCollection,
      department.hospital
    );
    this.doctorsSharedCollection = this.doctorService.addDoctorToCollectionIfMissing<IDoctor>(
      this.doctorsSharedCollection,
      ...(department.doctors ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.hospitalService
      .query()
      .pipe(map((res: HttpResponse<IHospital[]>) => res.body ?? []))
      .pipe(
        map((hospitals: IHospital[]) =>
          this.hospitalService.addHospitalToCollectionIfMissing<IHospital>(hospitals, this.department?.hospital)
        )
      )
      .subscribe((hospitals: IHospital[]) => (this.hospitalsSharedCollection = hospitals));

    this.doctorService
      .query()
      .pipe(map((res: HttpResponse<IDoctor[]>) => res.body ?? []))
      .pipe(
        map((doctors: IDoctor[]) =>
          this.doctorService.addDoctorToCollectionIfMissing<IDoctor>(doctors, ...(this.department?.doctors ?? []))
        )
      )
      .subscribe((doctors: IDoctor[]) => (this.doctorsSharedCollection = doctors));
  }
}
