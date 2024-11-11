import {Injectable} from '@angular/core';
import {HttpClient, HttpErrorResponse, HttpParams} from "@angular/common/http";
import {BehaviorSubject, catchError, map, Observable, of} from "rxjs";
import {User} from "../model/user";
import {environment} from "../../environment";
import {Artwork} from "../../artwork/model/artwork";
import {LoginRequest} from "../model/loginRequest";
import {AuthResponse} from "../model/authResponse";
import {RegisterRequest} from "../model/registerRequest";

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
    return this.httpClient.get<User>(url).pipe(
      map((user: User) => user),
      catchError((error: HttpErrorResponse) => {
        if (error.status === 401) {
          console.warn('User is not authenticated');
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

  login(loginForm: LoginRequest) {
    const url: string = `${this.endpoint}/login`;
    return this.httpClient.post<AuthResponse>(url, loginForm);
  }

  register(registerRequest: RegisterRequest) {
    const url: string = `${this.endpoint}/register`;
    console.log(registerRequest);
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
