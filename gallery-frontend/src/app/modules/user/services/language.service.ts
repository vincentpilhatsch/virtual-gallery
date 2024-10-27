import {Injectable} from '@angular/core';
import {Language} from "../model/language";
import {UserService} from "./user.service";
import {TranslateService} from "@ngx-translate/core";

@Injectable({
  providedIn: 'root'
})
export class LanguageService {
  readonly german: string = "de";
  readonly english: string = "en";

  constructor(private userService: UserService,
              private translateService: TranslateService) {
    this.subscribeToUser();
  }

  private subscribeToUser() {
    this.userService.user$.subscribe((user) => {
      if (user) {
        this.setLanguageByLoginUser(user.language);
      }
    });
  }

  setLanguageByLoginUser(usersLanguage: string): void {
    if (usersLanguage && usersLanguage === Language.EN) {
      this.setLanguageInTranslateService(this.english)
      return;
    }
    this.setLanguageInTranslateService(this.german)
  }

  setDefaultLanguage(): void {
    this.setLanguageInTranslateService(this.german);
  }

  setLanguageInTranslateService(language: string): void {
    this.translateService.setDefaultLang(language);
    this.translateService.use(language);
  }

}
