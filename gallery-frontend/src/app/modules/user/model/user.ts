import {Language} from "./language";
import {Role} from "./role";

export interface User {
  id: string, // id notwendig?
  firstName: string,
  lastName: string,
  email: string,
  password: string,
  city?: string,
  phone?: string,
  language: Language,
  role: Role,
  favoriteArtworkIds?: number[],
  acceptedEventIds?: number[],
  notificationIds?: number[],
}
