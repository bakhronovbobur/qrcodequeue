import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { QueueForDoctorComponent } from './list/queue-for-doctor.component';
import { QueueForDoctorDetailComponent } from './detail/queue-for-doctor-detail.component';
import { QueueForDoctorUpdateComponent } from './update/queue-for-doctor-update.component';
import { QueueForDoctorDeleteDialogComponent } from './delete/queue-for-doctor-delete-dialog.component';
import { QueueForDoctorRoutingModule } from './route/queue-for-doctor-routing.module';
import { QueueForDoctorQrcodeDialog } from './qrcode/queue-for-doctor-qrcode-dialog';

@NgModule({
  imports: [SharedModule, QueueForDoctorRoutingModule],
  declarations: [
    QueueForDoctorComponent,
    QueueForDoctorDetailComponent,
    QueueForDoctorUpdateComponent,
    QueueForDoctorDeleteDialogComponent,
    QueueForDoctorQrcodeDialog,
  ],
})
export class QueueForDoctorModule {}
