import {Injectable} from '@angular/core';
import {HttpClient, HttpErrorResponse, HttpParams} from "@angular/common/http";
import {BehaviorSubject, catchError, map, Observable, of} from "rxjs";
import {User} from "../model/user";
import {environment} from "../../environment";
import {Artwork} from "../../artwork/model/artwork";
import {LoginRequest} from "../model/loginRequest";
import {AuthResponse} from "../model/authResponse";
import {RegisterRequest} from "../model/registerRequest";
import {ArtworkAndArtworkRequest} from "../../artwork/model/artworkAndArtworkRequest";
import {ArtworkRequest} from "../../artwork/model/artworkRequest";

@Injectable({
  providedIn: 'root'
})
export class UserService {
  readonly endpoint: string = `${environment.apiUrl}/user`;

  private userSubject = new BehaviorSubject<User | null>(null);
  user$ = this.userSubject.asObservable();

  get user(): User | null {
    return this.userSubject.value;
  }

  set user(user: User | null) {
    this.userSubject.next(user);
  }

  constructor(private httpClient: HttpClient) {
  }

  getCurrentUser(): Observable<User | null> {
    const url: string = `${this.endpoint}/me`;
    return this.httpClient.get<User>(url)
      .pipe(
        map((user: User) => user),
        catchError((error: HttpErrorResponse) => {
          if (error.status === 401) {
            return of(null);
          }
          throw error;
        })
      );
  }

  getUserByEmail(email: string): Observable<User> {
    const url: string = `${this.endpoint}/${email}`;
    return this.httpClient.get<User>(url);
  }

  getUserByToken(token: string): Observable<User> {
    const url: string = `${this.endpoint}`;
    return this.httpClient.post<User>(url, token);
  }

  getFavoriteArtworkIdsByEmail(email: string): Observable<number[]> {
    const url: string = `${this.endpoint}/favoriteArtworkIds/${email}`;
    return this.httpClient.get<number[]>(url);
  }

  getAcceptedEventIdsByEmail(email: string): Observable<number[]> {
    const url: string = `${this.endpoint}/acceptedEventIds/${email}`;
    return this.httpClient.get<number[]>(url);
  }

  getFavoriteArtworksByEmail(email: string): Observable<Artwork[]> {
    const url: string = `${this.endpoint}/favoriteArtworks/${email}`;
    return this.httpClient.get<Artwork[]>(url);
  }

  getArtworksAndArtworkRequestsByEmail(email: string): Observable<ArtworkAndArtworkRequest[]> {
    const url: string = `${this.endpoint}/artworksAndArtworkRequests/${email}`;
    return this.httpClient.get<ArtworkAndArtworkRequest[]>(url);
  }

  getArtworkRequestsByEmail(email: string): Observable<ArtworkRequest[]> {
    const url: string = `${this.endpoint}/artworkRequests/${email}`;
    return this.httpClient.get<ArtworkRequest[]>(url);
  }

  login(loginForm: LoginRequest) {
    const url: string = `${this.endpoint}/login`;
    return this.httpClient.post<AuthResponse>(url, loginForm);
  }

  register(registerRequest: RegisterRequest) {
    const url: string = `${this.endpoint}/register`;
    return this.httpClient.post<AuthResponse>(url, registerRequest);
  }

  logout(): Observable<Object> {
    const url: string = `${this.endpoint}/logout`;
    return this.httpClient.post(url, {});
  }


  updateFavoriteArtworksOfUser(email: string, artworkId: number) {
    const url: string = `${this.endpoint}/updateFavoriteArtworks?`;
    const params: HttpParams = new HttpParams()
      .set("email", email)
      .set("artworkId", artworkId);
    return this.httpClient.post<{ email: string, eventId: number }>(url + params, null);
  }

  updateOneAcceptedEvent(email: string, eventId: number) {
    const url: string = `${this.endpoint}/updateAcceptedEvents?`;
    const params: HttpParams = new HttpParams()
      .set("email", email)
      .set("eventId", eventId);
    return this.httpClient.post<{ email: string, eventId: number }>(url + params, null);
  }

  updateUser(user: User) {
    const url: string = `${this.endpoint}`;
    return this.httpClient.put<User>(url, user);
  }


}
