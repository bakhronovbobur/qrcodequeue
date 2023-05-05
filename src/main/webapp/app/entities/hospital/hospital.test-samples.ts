import { IHospital, NewHospital } from './hospital.model';

export const sampleWithRequiredData: IHospital = {
  id: 63080,
  name: 'Islands Nebraska',
};

export const sampleWithPartialData: IHospital = {
  id: 4746,
  name: 'Legacy Nebraska functionalities',
  longitude: 90378,
  latitude: 29673,
  description: 'Factors Account',
  address: 'SMS parsing Innovative',
};

export const sampleWithFullData: IHospital = {
  id: 76540,
  name: 'collaboration deliver',
  longitude: 51706,
  latitude: 61034,
  description: 'Lithuania enable',
  address: 'lime card',
};

export const sampleWithNewData: NewHospital = {
  name: 'Books Fresh',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
