import {Language} from "./language";

export interface User {
  id: string, // id notwendig?
  firstName: string,
  lastName: string,
  email: string,
  password: string,
  city: string,
  phone: string,
  language: Language, // enum?
  favoriteArtworkIds: number[],
  acceptedEventIds: number[],
  notificationIds: number[],
}
