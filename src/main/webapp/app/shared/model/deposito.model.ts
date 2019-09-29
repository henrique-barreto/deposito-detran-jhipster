export interface IDeposito {
  id?: number;
  nome?: string;
  ativo?: boolean;
}

export const defaultValue: Readonly<IDeposito> = {
  ativo: false
};
