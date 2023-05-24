import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IQueueForDoctor } from '../queue-for-doctor.model';
import { QueueForDoctorService } from '../service/queue-for-doctor.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './queue-for-doctor-qrcode-dialog.html',
})
export class QueueForDoctorQrcodeDialog {
  queueForDoctor?: IQueueForDoctor;

  constructor(protected queueForDoctorService: QueueForDoctorService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.queueForDoctorService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
