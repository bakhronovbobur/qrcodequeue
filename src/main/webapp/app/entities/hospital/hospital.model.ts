import { IRegion } from 'app/entities/region/region.model';
import { IDistrict } from 'app/entities/district/district.model';

export interface IHospital {
  id: number;
  name?: string | null;
  longitude?: number | null;
  latitude?: number | null;
  description?: string | null;
  address?: string | null;
  region?: Pick<IRegion, 'id' | 'name'> | null;
  district?: Pick<IDistrict, 'id' | 'name'> | null;
}

export type NewHospital = Omit<IHospital, 'id'> & { id: null };
