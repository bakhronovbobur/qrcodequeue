import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IQueueForDoctor, NewQueueForDoctor } from '../queue-for-doctor.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IQueueForDoctor for edit and NewQueueForDoctorFormGroupInput for create.
 */
type QueueForDoctorFormGroupInput = IQueueForDoctor | PartialWithRequiredKeyOf<NewQueueForDoctor>;

type QueueForDoctorFormDefaults = Pick<NewQueueForDoctor, 'id'>;

type QueueForDoctorFormGroupContent = {
  id: FormControl<IQueueForDoctor['id'] | NewQueueForDoctor['id']>;
  number: FormControl<IQueueForDoctor['number']>;
  user: FormControl<IQueueForDoctor['user']>;
  doctor: FormControl<IQueueForDoctor['doctor']>;
  department: FormControl<IQueueForDoctor['department']>;
  hospital: FormControl<IQueueForDoctor['hospital']>;
};

export type QueueForDoctorFormGroup = FormGroup<QueueForDoctorFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class QueueForDoctorFormService {
  createQueueForDoctorFormGroup(queueForDoctor: QueueForDoctorFormGroupInput = { id: null }): QueueForDoctorFormGroup {
    const queueForDoctorRawValue = {
      ...this.getFormDefaults(),
      ...queueForDoctor,
    };
    return new FormGroup<QueueForDoctorFormGroupContent>({
      id: new FormControl(
        { value: queueForDoctorRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      number: new FormControl(queueForDoctorRawValue.number),
      user: new FormControl(queueForDoctorRawValue.user, {
        validators: [Validators.required],
      }),
      doctor: new FormControl(queueForDoctorRawValue.doctor, {
        validators: [Validators.required],
      }),
      department: new FormControl(queueForDoctorRawValue.department, {
        validators: [Validators.required],
      }),
      hospital: new FormControl(queueForDoctorRawValue.hospital, {
        validators: [Validators.required],
      }),
    });
  }

  getQueueForDoctor(form: QueueForDoctorFormGroup): IQueueForDoctor | NewQueueForDoctor {
    return form.getRawValue() as IQueueForDoctor | NewQueueForDoctor;
  }

  resetForm(form: QueueForDoctorFormGroup, queueForDoctor: QueueForDoctorFormGroupInput): void {
    const queueForDoctorRawValue = { ...this.getFormDefaults(), ...queueForDoctor };
    form.reset(
      {
        ...queueForDoctorRawValue,
        id: { value: queueForDoctorRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): QueueForDoctorFormDefaults {
    return {
      id: null,
    };
  }
}
