import {Component, ViewEncapsulation} from '@angular/core';
import {MatButton} from "@angular/material/button";
import {NgOptimizedImage} from "@angular/common";
import {ArtworkCardComponent} from "../artwork/artwork-card/artwork-card.component";

@Component({
  selector: 'app-landing-page',
  templateUrl: './landing-page.component.html',
  styleUrls: ['./landing-page.component.scss'],
  encapsulation: ViewEncapsulation.None,
  imports: [
    MatButton,
    NgOptimizedImage,
    ArtworkCardComponent
  ],
  standalone: true
})
export class LandingPageComponent {

}
