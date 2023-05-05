import { IQueueForDoctor, NewQueueForDoctor } from './queue-for-doctor.model';

export const sampleWithRequiredData: IQueueForDoctor = {
  id: 22013,
};

export const sampleWithPartialData: IQueueForDoctor = {
  id: 73708,
};

export const sampleWithFullData: IQueueForDoctor = {
  id: 95383,
  number: 62122,
};

export const sampleWithNewData: NewQueueForDoctor = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
