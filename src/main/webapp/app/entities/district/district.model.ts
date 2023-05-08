import { IRegion } from 'app/entities/region/region.model';

export interface IDistrict {
  id: number;
  name?: string | null;
  region?: Pick<IRegion, 'id' | 'name'> | null;
}

export type NewDistrict = Omit<IDistrict, 'id'> & { id: null };
