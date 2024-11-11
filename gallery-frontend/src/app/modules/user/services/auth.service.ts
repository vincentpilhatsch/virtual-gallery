import {Injectable} from '@angular/core';
import {CookieService} from "ngx-cookie-service";
import {UserService} from "./user.service";
import {Router} from "@angular/router";
import {jwtDecode} from "jwt-decode";

@Injectable({
  providedIn: 'root',
})
export class AuthService {

  constructor(private cookieService: CookieService,
              private userService: UserService,
              private router: Router,) {
  }

  getToken(): string {
    return this.cookieService.get('token');
  }

  setToken(token: string): void {
    this.cookieService.set('token', token, {path: '/'}); // httpOnly
  }

  isUserLoggedIn(): boolean {
    console.log(this.cookieService);
    return this.getToken() !== "" && !this.isTokenExpired();
  }

  isTokenExpired(): boolean {
    const token = this.getToken();
    if (!token) return true;
    try {
      const decodedToken: { exp: number } = jwtDecode(token);
      const expirationDate = new Date(decodedToken.exp * 1000);
      return expirationDate < new Date();
    } catch (e) {
      return true;
    }
  }

  logout(): void {
    this.deleteTokenFromCookie();
    this.userService.user = null;
    void this.router.navigate(["home"]);
  }

  deleteTokenFromCookie(): void {
    this.cookieService.delete('token', '/');
  }
}
