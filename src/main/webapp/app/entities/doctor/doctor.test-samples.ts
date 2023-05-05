import { IDoctor, NewDoctor } from './doctor.model';

export const sampleWithRequiredData: IDoctor = {
  id: 47308,
  firstname: 'Unions brand',
  lastname: 'EXE',
  middleName: 'hack mint',
};

export const sampleWithPartialData: IDoctor = {
  id: 23177,
  firstname: 'ivory Borders Clothing',
  lastname: 'Electronics Dollar payment',
  middleName: 'Supervisor Engineer',
  phone: '1-648-997-7170',
};

export const sampleWithFullData: IDoctor = {
  id: 43954,
  firstname: 'Trafficway',
  lastname: 'task-force',
  middleName: 'orange',
  phone: '(761) 960-3691 x92907',
  position: 'Loan',
  qualification: 'Trace',
};

export const sampleWithNewData: NewDoctor = {
  firstname: 'azure interactive Tennessee',
  lastname: 'Ergonomic',
  middleName: 'deposit',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
