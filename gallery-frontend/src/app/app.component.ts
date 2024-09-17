import {Component, OnInit, ViewEncapsulation} from '@angular/core';
import {RouterLink, RouterOutlet} from '@angular/router';
import {LandingPageComponent} from "./modules/event-pages/landing-page/landing-page.component";
import {HttpClientModule, HttpErrorResponse} from "@angular/common/http";
import {HeaderComponent} from "./modules/event-pages/misc/header/header.component";
import {FooterComponent} from "./modules/event-pages/misc/footer/footer.component";
import {UserService} from "./modules/event-pages/user/services/user.service";
import {User} from "./modules/event-pages/user/model/user";

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
    HttpClientModule
  ],
  standalone: true
})
export class AppComponent implements OnInit{

  constructor(private userHttpService: UserService) {
  }

  ngOnInit() {
    this.loadUserInService();
  }


  private loadUserInService() {
    this.userHttpService.getUserByEmail("mueller@bht.de").subscribe({
      next: (res: User): void => {
        this.userHttpService.user = res;
      },
      error: (err: HttpErrorResponse): void => {
        console.error(err)
      }
    });
  }
}
