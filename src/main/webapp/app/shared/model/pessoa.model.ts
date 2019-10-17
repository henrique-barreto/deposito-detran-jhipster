import { Moment } from 'moment';

export interface IPessoa {
  id?: number;
  nome?: string;
  data?: Moment;
}

export const defaultValue: Readonly<IPessoa> = {};
