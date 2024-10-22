import {Injectable} from '@angular/core';
import {CookieService} from "ngx-cookie-service";

@Injectable({
  providedIn: 'root',
})
export class AuthService {

  constructor(private cookieService: CookieService) {
  }

  setToken(token: string): void {
    this.cookieService.set('token', token, {path: '/'}); // httpOnly
  }

  getToken(): string {
    return this.cookieService.get('token');
  }

  logout(): void {
    this.cookieService.delete('token', '/');
  }
}
