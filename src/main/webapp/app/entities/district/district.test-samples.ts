import { IDistrict, NewDistrict } from './district.model';

export const sampleWithRequiredData: IDistrict = {
  id: 85653,
  name: 'Soap invoice Dong',
};

export const sampleWithPartialData: IDistrict = {
  id: 49773,
  name: 'Fresh real-time gold',
};

export const sampleWithFullData: IDistrict = {
  id: 79437,
  name: 'compressing Concrete overriding',
};

export const sampleWithNewData: NewDistrict = {
  name: 'deposit',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
