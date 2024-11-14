import {Injectable} from '@angular/core';
import {LanguageEnum} from "../model/language.enum";
import {UserService} from "./user.service";
import {TranslateService} from "@ngx-translate/core";
import {User} from "../model/user";
import {environment} from "../../environment";
import {HttpClient, HttpParams} from "@angular/common/http";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class LanguageService {
  readonly endpoint: string = `${environment.apiUrl}/user`;

  constructor(private userService: UserService,
              private translateService: TranslateService,
              private httpClient: HttpClient) {
    this.subscribeToUser();
  }

  private subscribeToUser() {
    this.userService.user$.subscribe((user) => {
      if (user) {
        this.setLanguageByLoggedInUser(user.language);
      } else {
        this.setDefaultLanguageByLocalStorage();
      }
    });
  }

  updateLanguageForUser(language: LanguageEnum): Observable<User> | null {
    const email: string | undefined = this.userService.user?.email;
    if (email) {
      const url: string = `${this.endpoint}/updateLanguage?`;
      const params: HttpParams = new HttpParams()
        .set("email", email)
        .set("language", language);
      return this.httpClient.put<User>(url + params, null);
    }
    return null;
  }

  setLanguageByLoggedInUser(usersLanguage: LanguageEnum): void {
    if (usersLanguage && usersLanguage === LanguageEnum.EN) {
      this.setLanguageInTranslateService(LanguageEnum.EN)
      return;
    }
    this.setLanguageInTranslateService(LanguageEnum.DE)
  }

  setDefaultLanguageByLocalStorage(): void {
    const localStorageLanguage: LanguageEnum | null = this.getLanguageFromLocalStorage();
    if (localStorageLanguage) {
      this.setLanguageInTranslateService(localStorageLanguage);
    } else {
      this.setLanguageInTranslateService(LanguageEnum.DE);
    }
  }

  setLanguageInTranslateService(language: LanguageEnum): void {
    const languageString: string = language.toString().toLowerCase();
    this.translateService.setDefaultLang(languageString);
    this.translateService.use(languageString);
  }

  setLanguageInLocalStorage(language: LanguageEnum): void {
    if (typeof window !== 'undefined') {
      localStorage.setItem("language", language);
    }
  }

  getLanguageFromLocalStorage(): LanguageEnum | null {
    if (typeof window !== 'undefined') {
      const localStorageLanguage: string | null = localStorage.getItem("language");
      if (localStorageLanguage != null) {
        return LanguageEnum[localStorageLanguage as keyof typeof LanguageEnum];
      }
    }
    return null;
  }
}
