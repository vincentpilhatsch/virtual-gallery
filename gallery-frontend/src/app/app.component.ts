import {Component, ViewEncapsulation} from '@angular/core';
import {RouterLink, RouterOutlet} from '@angular/router';
import {LandingPageComponent} from "./modules/event-pages/landing-page/landing-page.component";
import {HeaderComponent} from "./modules/components/header/header.component";
import {FooterComponent} from "./modules/components/footer/footer.component";

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
    FooterComponent
  ],
  standalone: true
})
export class AppComponent {
  title = 'gallery-frontend';
}
