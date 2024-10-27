import {Component, OnInit, ViewEncapsulation} from '@angular/core';
import {RouterLink, RouterOutlet} from '@angular/router';
import {HttpClientModule, HttpErrorResponse} from "@angular/common/http";
import {TranslateModule} from "@ngx-translate/core";
import {HeaderComponent} from "./modules/misc/header/header.component";
import {FooterComponent} from "./modules/misc/footer/footer.component";
import {HomeComponent} from "./modules/home/home.component";
import {UserService} from "./modules/user/services/user.service";
import {User} from "./modules/user/model/user";
import {LanguageService} from "./modules/user/services/language.service";
import {AuthService} from "./modules/user/services/auth.service";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
  encapsulation: ViewEncapsulation.None,
  imports: [
    HomeComponent,
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
  constructor(private userService: UserService,
              private languageService: LanguageService,
              private authService: AuthService) {
  }

  ngOnInit() {
    this.logUserInIfTokenIsPresent();
  }

  private logUserInIfTokenIsPresent() {
    const token: string = this.authService.getToken()
    if (token) {
      this.userService.getUserByToken(token).subscribe({
        next: (res: User): void => {
          this.userService.user = res;
          this.languageService.setLanguageByLoginUser(res.language);
        },
        error: (err: HttpErrorResponse): void => {
          console.error(err)
        }
      });
    } else {
      this.languageService.setDefaultLanguage();
    }
  }

}
