import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IDoctor, NewDoctor } from '../doctor.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IDoctor for edit and NewDoctorFormGroupInput for create.
 */
type DoctorFormGroupInput = IDoctor | PartialWithRequiredKeyOf<NewDoctor>;

type DoctorFormDefaults = Pick<NewDoctor, 'id' | 'departments'>;

type DoctorFormGroupContent = {
  id: FormControl<IDoctor['id'] | NewDoctor['id']>;
  firstname: FormControl<IDoctor['firstname']>;
  lastname: FormControl<IDoctor['lastname']>;
  middleName: FormControl<IDoctor['middleName']>;
  phone: FormControl<IDoctor['phone']>;
  position: FormControl<IDoctor['position']>;
  qualification: FormControl<IDoctor['qualification']>;
  departments: FormControl<IDoctor['departments']>;
};

export type DoctorFormGroup = FormGroup<DoctorFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class DoctorFormService {
  createDoctorFormGroup(doctor: DoctorFormGroupInput = { id: null }): DoctorFormGroup {
    const doctorRawValue = {
      ...this.getFormDefaults(),
      ...doctor,
    };
    return new FormGroup<DoctorFormGroupContent>({
      id: new FormControl(
        { value: doctorRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      firstname: new FormControl(doctorRawValue.firstname, {
        validators: [Validators.required],
      }),
      lastname: new FormControl(doctorRawValue.lastname, {
        validators: [Validators.required],
      }),
      middleName: new FormControl(doctorRawValue.middleName, {
        validators: [Validators.required],
      }),
      phone: new FormControl(doctorRawValue.phone),
      position: new FormControl(doctorRawValue.position),
      qualification: new FormControl(doctorRawValue.qualification),
      departments: new FormControl(doctorRawValue.departments ?? []),
    });
  }

  getDoctor(form: DoctorFormGroup): IDoctor | NewDoctor {
    return form.getRawValue() as IDoctor | NewDoctor;
  }

  resetForm(form: DoctorFormGroup, doctor: DoctorFormGroupInput): void {
    const doctorRawValue = { ...this.getFormDefaults(), ...doctor };
    form.reset(
      {
        ...doctorRawValue,
        id: { value: doctorRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): DoctorFormDefaults {
    return {
      id: null,
      departments: [],
    };
  }
}
