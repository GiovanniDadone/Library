import { ILibro } from 'app/shared/model/libro.model';
import { IUser } from 'app/shared/model/user.model';

export interface IRecensione {
  id?: number;
  descrizione?: string | null;
  libro?: ILibro;
  user?: IUser;
}

export const defaultValue: Readonly<IRecensione> = {};
