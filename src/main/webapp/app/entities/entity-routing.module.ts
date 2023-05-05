import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'region',
        data: { pageTitle: 'Regions' },
        loadChildren: () => import('./region/region.module').then(m => m.RegionModule),
      },
      {
        path: 'district',
        data: { pageTitle: 'Districts' },
        loadChildren: () => import('./district/district.module').then(m => m.DistrictModule),
      },
      {
        path: 'hospital',
        data: { pageTitle: 'Hospitals' },
        loadChildren: () => import('./hospital/hospital.module').then(m => m.HospitalModule),
      },
      {
        path: 'department',
        data: { pageTitle: 'Departments' },
        loadChildren: () => import('./department/department.module').then(m => m.DepartmentModule),
      },
      {
        path: 'doctor',
        data: { pageTitle: 'Doctors' },
        loadChildren: () => import('./doctor/doctor.module').then(m => m.DoctorModule),
      },
      {
        path: 'queue-for-doctor',
        data: { pageTitle: 'QueueForDoctors' },
        loadChildren: () => import('./queue-for-doctor/queue-for-doctor.module').then(m => m.QueueForDoctorModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
