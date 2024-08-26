import {Component, ViewEncapsulation} from '@angular/core';
import {MatCard, MatCardActions, MatCardContent, MatCardImage} from "@angular/material/card";
import {MatIcon} from "@angular/material/icon";
import {FavoriteIconComponent} from "../favorite-icon/favorite-icon.component";
import {RouterLink} from "@angular/router";

@Component({
  selector: 'app-artwork-extended-card',
  standalone: true,
  imports: [
    MatCardActions,
    MatCardContent,
    MatCard,
    MatCardImage,
    MatIcon,
    FavoriteIconComponent,
    RouterLink
  ],
  templateUrl: './artwork-extended-card.component.html',
  styleUrl: './artwork-extended-card.component.scss',
  encapsulation: ViewEncapsulation.None
})
export class ArtworkExtendedCardComponent {

}
