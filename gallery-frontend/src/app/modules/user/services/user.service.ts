import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from "@angular/common/http";
import {Observable} from "rxjs";
import {User} from "../model/user";
import {environment} from "../../environment";
import {Artwork} from "../../artwork/model/artwork";
import {LoginForm} from "../model/loginForm";

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private endpoint: string = `${environment.apiUrl}/user`;
  user: User | undefined;

  constructor(private httpClient: HttpClient) {
  }

  getUserByEmail(email: string): Observable<User> {
    const url: string = `${this.endpoint}/${email}`;
    return this.httpClient.get<User>(url);
  }

  getFavoriteArtworkIdsByEmail(email: string): Observable<number[]> {
    const url: string = `${this.endpoint}/favoriteArtworkIds/${email}`;
    return this.httpClient.get<number[]>(url);
  }

  getAcceptedEventIdsByEmail(email: string): Observable<number[]> {
    const url: string = `${this.endpoint}/acceptedEventIds/${email}`;
    return this.httpClient.get<number[]>(url);
  }

  getNotificationIdsByEmail(email: string): Observable<number[]> {
    const url: string = `${this.endpoint}/notificationIds/${email}`;
    return this.httpClient.get<number[]>(url);
  }

  getFavoriteArtworksByEmail(email: string): Observable<Artwork[]> {
    const url: string = `${this.endpoint}/favoriteArtworks/${email}`;
    return this.httpClient.get<Artwork[]>(url);
  }

  getRequestedArtworksByEmail(email: string): Observable<Artwork[]> {
    const url: string = `${this.endpoint}/requestedArtworks/${email}`;
    return this.httpClient.get<Artwork[]>(url);
  }

  login(loginForm: LoginForm) {
    const url: string = `${this.endpoint}/login`;
    return this.httpClient.post(url, loginForm, {responseType: 'text'});
  }

  updateFavoriteArtworksOfUser(email: string, artworkId: number) {
    const url: string = `${this.endpoint}/updateFavoriteArtworks?`;
    const params: HttpParams = new HttpParams()
      .set("email", email)
      .set("artworkId", artworkId);
    return this.httpClient.post<{ email: string, eventId: number }>(url + params, null);
  }

  updateOneRequestedArtwork(email: string, artworkId: number) {
    const url: string = `${this.endpoint}/updateOneRequestedArtwork?`;
    const params: HttpParams = new HttpParams()
      .set("email", email)
      .set("artworkId", artworkId);
    return this.httpClient.post<{ email: string, eventId: number }>(url + params, null);
  }

  updateUser(user: User) {
    const url: string = `${this.endpoint}`;
    return this.httpClient.put<User>(url, user);
  }

}
