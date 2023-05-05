import { IDepartment, NewDepartment } from './department.model';

export const sampleWithRequiredData: IDepartment = {
  id: 39095,
  name: 'neutral markets',
};

export const sampleWithPartialData: IDepartment = {
  id: 15133,
  name: 'Incredible Games Table',
};

export const sampleWithFullData: IDepartment = {
  id: 20843,
  name: 'Morocco',
};

export const sampleWithNewData: NewDepartment = {
  name: 'Facilitator',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
