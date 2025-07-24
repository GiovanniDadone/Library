import { IAutore } from 'app/shared/model/autore.model';

export interface ILibro {
  id?: number;
  titolo?: string;
  prezzo?: number;
  autore?: IAutore;
}

export const defaultValue: Readonly<ILibro> = {};
