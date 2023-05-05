import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IQueueForDoctor } from '../queue-for-doctor.model';

@Component({
  selector: 'jhi-queue-for-doctor-detail',
  templateUrl: './queue-for-doctor-detail.component.html',
})
export class QueueForDoctorDetailComponent implements OnInit {
  queueForDoctor: IQueueForDoctor | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ queueForDoctor }) => {
      this.queueForDoctor = queueForDoctor;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
