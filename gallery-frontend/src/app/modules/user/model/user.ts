import {LanguageEnum} from "./language.enum";
import {RoleEnum} from "./role.enum";

export interface User {
  id: string,
  firstName: string,
  lastName: string,
  email: string,
  password: string,
  city?: string,
  phone?: string,
  language: LanguageEnum,
  role: RoleEnum,
  favoriteArtworkIds?: number[],
  acceptedEventIds?: number[],
}
