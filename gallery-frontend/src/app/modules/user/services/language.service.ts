import {Injectable} from '@angular/core';
import {Language} from "../model/language";
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

  updateLanguageForUser(language: Language): Observable<User> | null {
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

  setLanguageByLoggedInUser(usersLanguage: Language): void {
    if (usersLanguage && usersLanguage === Language.EN) {
      this.setLanguageInTranslateService(Language.EN)
      return;
    }
    this.setLanguageInTranslateService(Language.DE)
  }

  setDefaultLanguageByLocalStorage(): void {
    const localStorageLanguage: Language | null = this.getLanguageFromLocalStorage();
    if (localStorageLanguage) {
      this.setLanguageInTranslateService(localStorageLanguage);
    } else {
      this.setLanguageInTranslateService(Language.DE);
    }
  }

  setLanguageInTranslateService(language: Language): void {
    const languageString: string = language.toString().toLowerCase();
    this.translateService.setDefaultLang(languageString);
    this.translateService.use(languageString);
  }

  setLanguageInLocalStorage(language: Language): void {
    localStorage.setItem("language", language);
  }

  getLanguageFromLocalStorage(): Language | null {
    const localStorageLanguage: string | null = localStorage.getItem("language");
    if (localStorageLanguage != null) {
      return Language[localStorageLanguage as keyof typeof Language];
    }
    return null;
  }
}
