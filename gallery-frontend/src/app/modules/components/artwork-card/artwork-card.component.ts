import {Component, ViewEncapsulation} from '@angular/core';
import {MatCard, MatCardActions, MatCardContent, MatCardImage} from "@angular/material/card";

@Component({
  selector: 'app-artwork-card',
  standalone: true,
  imports: [
    MatCardActions,
    MatCardContent,
    MatCard,
    MatCardImage
  ],
  templateUrl: './artwork-card.component.html',
  styleUrl: './artwork-card.component.scss',
  encapsulation: ViewEncapsulation.None
})
export class ArtworkCardComponent {

}
