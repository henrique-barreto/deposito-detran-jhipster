export interface IPerfil {
  id?: number;
  nome?: string;
  ativo?: boolean;
}

export const defaultValue: Readonly<IPerfil> = {
  ativo: false
};
