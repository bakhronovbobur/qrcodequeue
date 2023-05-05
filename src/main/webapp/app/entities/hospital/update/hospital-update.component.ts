import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { HospitalFormService, HospitalFormGroup } from './hospital-form.service';
import { IHospital } from '../hospital.model';
import { HospitalService } from '../service/hospital.service';
import { IRegion } from 'app/entities/region/region.model';
import { RegionService } from 'app/entities/region/service/region.service';
import { IDistrict } from 'app/entities/district/district.model';
import { DistrictService } from 'app/entities/district/service/district.service';

@Component({
  selector: 'jhi-hospital-update',
  templateUrl: './hospital-update.component.html',
})
export class HospitalUpdateComponent implements OnInit {
  isSaving = false;
  hospital: IHospital | null = null;

  regionsSharedCollection: IRegion[] = [];
  districtsSharedCollection: IDistrict[] = [];

  editForm: HospitalFormGroup = this.hospitalFormService.createHospitalFormGroup();

  constructor(
    protected hospitalService: HospitalService,
    protected hospitalFormService: HospitalFormService,
    protected regionService: RegionService,
    protected districtService: DistrictService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareRegion = (o1: IRegion | null, o2: IRegion | null): boolean => this.regionService.compareRegion(o1, o2);

  compareDistrict = (o1: IDistrict | null, o2: IDistrict | null): boolean => this.districtService.compareDistrict(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ hospital }) => {
      this.hospital = hospital;
      if (hospital) {
        this.updateForm(hospital);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const hospital = this.hospitalFormService.getHospital(this.editForm);
    if (hospital.id !== null) {
      this.subscribeToSaveResponse(this.hospitalService.update(hospital));
    } else {
      this.subscribeToSaveResponse(this.hospitalService.create(hospital));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IHospital>>): void {
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

  protected updateForm(hospital: IHospital): void {
    this.hospital = hospital;
    this.hospitalFormService.resetForm(this.editForm, hospital);

    this.regionsSharedCollection = this.regionService.addRegionToCollectionIfMissing<IRegion>(
      this.regionsSharedCollection,
      hospital.region
    );
    this.districtsSharedCollection = this.districtService.addDistrictToCollectionIfMissing<IDistrict>(
      this.districtsSharedCollection,
      hospital.district
    );
  }

  protected loadRelationshipsOptions(): void {
    this.regionService
      .query()
      .pipe(map((res: HttpResponse<IRegion[]>) => res.body ?? []))
      .pipe(map((regions: IRegion[]) => this.regionService.addRegionToCollectionIfMissing<IRegion>(regions, this.hospital?.region)))
      .subscribe((regions: IRegion[]) => (this.regionsSharedCollection = regions));

    this.districtService
      .query()
      .pipe(map((res: HttpResponse<IDistrict[]>) => res.body ?? []))
      .pipe(
        map((districts: IDistrict[]) =>
          this.districtService.addDistrictToCollectionIfMissing<IDistrict>(districts, this.hospital?.district)
        )
      )
      .subscribe((districts: IDistrict[]) => (this.districtsSharedCollection = districts));
  }
}
