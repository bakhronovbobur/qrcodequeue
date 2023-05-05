import { IDepartment } from 'app/entities/department/department.model';

export interface IDoctor {
  id: number;
  firstname?: string | null;
  lastname?: string | null;
  middleName?: string | null;
  phone?: string | null;
  position?: string | null;
  qualification?: string | null;
  departments?: Pick<IDepartment, 'id'>[] | null;
}

export type NewDoctor = Omit<IDoctor, 'id'> & { id: null };
