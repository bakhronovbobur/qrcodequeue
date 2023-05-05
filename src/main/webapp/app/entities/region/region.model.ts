export interface IRegion {
  id: number;
  name?: string | null;
}

export type NewRegion = Omit<IRegion, 'id'> & { id: null };
