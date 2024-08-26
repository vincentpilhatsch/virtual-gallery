import {Component, ViewEncapsulation} from '@angular/core';
import {MatSlideToggle} from "@angular/material/slide-toggle";
import {MatButton} from "@angular/material/button";
import {NgOptimizedImage} from "@angular/common";
import {ArtworkCardComponent} from "../../components/artwork-card/artwork-card.component";
import {ArtworkExtendedCardComponent} from "../../components/artwork-extended-card/artwork-extended-card.component";

@Component({
  selector: 'app-landing-page',
  templateUrl: './landing-page.component.html',
  styleUrls: ['./landing-page.component.scss'],
  encapsulation: ViewEncapsulation.None,
  imports: [
    MatButton,
    NgOptimizedImage,
    ArtworkCardComponent,
    ArtworkExtendedCardComponent
  ],
  standalone: true
})
export class LandingPageComponent {

}
