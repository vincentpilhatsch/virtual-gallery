import {Injectable} from '@angular/core';
import {CookieService} from "ngx-cookie-service";
import {UserService} from "./user.service";
import {Router} from "@angular/router";
import {LanguageService} from "./language.service";

@Injectable({
  providedIn: 'root',
})
export class AuthService {

  constructor(private cookieService: CookieService,
              private userService: UserService,
              private router: Router,) {
  }

  setToken(token: string): void {
    this.cookieService.set('token', token, {path: '/'}); // httpOnly
  }

  getToken(): string {
    return this.cookieService.get('token');
  }

  logout(): void {
    this.userService.user = null;
    // this.languageService.setDefaultLanguage();
    this.cookieService.delete('token', '/');
    void this.router.navigate(["home"]);
  }
}
