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
  constructor(private readonly userService: UserService,
              private readonly languageService: LanguageService) {
  }

  ngOnInit() {
    this.userService.getCurrentUser().subscribe({
      next: (res: User | null): void => {
        if (res) {
          this.userService.user = res;
          this.languageService.setLanguageByLoggedInUser(res.language);
        } else {
          this.languageService.setDefaultLanguageByLocalStorage();
        }
      },
      error: (res: HttpErrorResponse): void => {
        console.error(res);
      }
    });
  }

}

