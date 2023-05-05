import { IHospital } from 'app/entities/hospital/hospital.model';
import { IDoctor } from 'app/entities/doctor/doctor.model';

export interface IDepartment {
  id: number;
  name?: string | null;
  hospital?: Pick<IHospital, 'id'> | null;
  doctors?: Pick<IDoctor, 'id'>[] | null;
}

export type NewDepartment = Omit<IDepartment, 'id'> & { id: null };
