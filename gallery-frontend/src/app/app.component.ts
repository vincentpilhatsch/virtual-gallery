import {Component, OnInit, ViewEncapsulation} from '@angular/core';
import {RouterLink, RouterOutlet} from '@angular/router';
import {HttpClientModule, HttpErrorResponse} from "@angular/common/http";
import {TranslateModule, TranslateService} from "@ngx-translate/core";
import {LandingPageComponent} from "./modules/landing-page/landing-page.component";
import {HeaderComponent} from "./modules/misc/header/header.component";
import {FooterComponent} from "./modules/misc/footer/footer.component";
import {UserService} from "./modules/user/services/user.service";
import {User} from "./modules/user/model/user";
import {Language} from "./modules/user/model/language";

// export function HttpLoaderFactory(http: HttpClient) {
//   return new TranslateHttpLoader(http, './assets/i18n/', '.json');
// }

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
  encapsulation: ViewEncapsulation.None,
  imports: [
    LandingPageComponent,
    HeaderComponent,
    RouterOutlet,
    RouterLink,
    FooterComponent,
    HttpClientModule,
    TranslateModule
  ],
  standalone: true
})
export class AppComponent implements OnInit {
  readonly german: string = "de";
  readonly english: string = "en";

  constructor(private userHttpService: UserService,
              private translateService: TranslateService) {}

  ngOnInit() {
    this.loadUserInService();
  }


  private loadUserInService() {
    this.userHttpService.getUserByEmail("mueller@bht.de").subscribe({
      next: (res: User): void => {
        this.userHttpService.user = res;
        this.setLanguage();
      },
      error: (err: HttpErrorResponse): void => {
        console.error(err)
      }
    });
  }

  private setLanguage(): void {
    const usersLanguage: Language | undefined = this.userHttpService.user?.language;
    if (usersLanguage && usersLanguage === Language.EN) {
      this.setLanguageWithService(this.english)
      return;
    }
    this.setLanguageWithService(this.german)
  }

  private setLanguageWithService(language: string) {
    this.translateService.setDefaultLang(language);
    this.translateService.use(language);
  }
}
