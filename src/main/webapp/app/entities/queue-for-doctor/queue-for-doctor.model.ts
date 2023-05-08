import { IUser } from 'app/entities/user/user.model';
import { IDoctor } from 'app/entities/doctor/doctor.model';
import { IDepartment } from 'app/entities/department/department.model';
import { IHospital } from 'app/entities/hospital/hospital.model';

export interface IQueueForDoctor {
  id: number;
  number?: number | null;
  user?: Pick<IUser, 'id' | 'login'> | null;
  doctor?: Pick<IDoctor, 'id' | 'firstname'> | null;
  department?: Pick<IDepartment, 'id' | 'name'> | null;
  hospital?: Pick<IHospital, 'id' | 'name'> | null;
}

export type NewQueueForDoctor = Omit<IQueueForDoctor, 'id'> & { id: null };
