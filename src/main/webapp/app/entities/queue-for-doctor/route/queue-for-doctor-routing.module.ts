import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { QueueForDoctorComponent } from '../list/queue-for-doctor.component';
import { QueueForDoctorDetailComponent } from '../detail/queue-for-doctor-detail.component';
import { QueueForDoctorUpdateComponent } from '../update/queue-for-doctor-update.component';
import { QueueForDoctorRoutingResolveService } from './queue-for-doctor-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const queueForDoctorRoute: Routes = [
  {
    path: '',
    component: QueueForDoctorComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: QueueForDoctorDetailComponent,
    resolve: {
      queueForDoctor: QueueForDoctorRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: QueueForDoctorUpdateComponent,
    resolve: {
      queueForDoctor: QueueForDoctorRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: QueueForDoctorUpdateComponent,
    resolve: {
      queueForDoctor: QueueForDoctorRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(queueForDoctorRoute)],
  exports: [RouterModule],
})
export class QueueForDoctorRoutingModule {}
