import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../queue-for-doctor.test-samples';

import { QueueForDoctorFormService } from './queue-for-doctor-form.service';

describe('QueueForDoctor Form Service', () => {
  let service: QueueForDoctorFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(QueueForDoctorFormService);
  });

  describe('Service methods', () => {
    describe('createQueueForDoctorFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createQueueForDoctorFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            number: expect.any(Object),
            user: expect.any(Object),
            doctor: expect.any(Object),
            department: expect.any(Object),
            hospital: expect.any(Object),
          })
        );
      });

      it('passing IQueueForDoctor should create a new form with FormGroup', () => {
        const formGroup = service.createQueueForDoctorFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            number: expect.any(Object),
            user: expect.any(Object),
            doctor: expect.any(Object),
            department: expect.any(Object),
            hospital: expect.any(Object),
          })
        );
      });
    });

    describe('getQueueForDoctor', () => {
      it('should return NewQueueForDoctor for default QueueForDoctor initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createQueueForDoctorFormGroup(sampleWithNewData);

        const queueForDoctor = service.getQueueForDoctor(formGroup) as any;

        expect(queueForDoctor).toMatchObject(sampleWithNewData);
      });

      it('should return NewQueueForDoctor for empty QueueForDoctor initial value', () => {
        const formGroup = service.createQueueForDoctorFormGroup();

        const queueForDoctor = service.getQueueForDoctor(formGroup) as any;

        expect(queueForDoctor).toMatchObject({});
      });

      it('should return IQueueForDoctor', () => {
        const formGroup = service.createQueueForDoctorFormGroup(sampleWithRequiredData);

        const queueForDoctor = service.getQueueForDoctor(formGroup) as any;

        expect(queueForDoctor).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IQueueForDoctor should not enable id FormControl', () => {
        const formGroup = service.createQueueForDoctorFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewQueueForDoctor should disable id FormControl', () => {
        const formGroup = service.createQueueForDoctorFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
