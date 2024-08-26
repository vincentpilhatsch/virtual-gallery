import {Component, ViewEncapsulation} from '@angular/core';
import {MatIcon} from "@angular/material/icon";

@Component({
  selector: 'app-favorite-icon',
  standalone: true,
    imports: [
        MatIcon
    ],
  templateUrl: './favorite-icon.component.html',
  styleUrl: './favorite-icon.component.scss',
  encapsulation: ViewEncapsulation.None
})
export class FavoriteIconComponent {
  isFavorite: boolean = false;

  handleFavoriteClicked(isFavorite: boolean) {
    this.isFavorite = isFavorite;
    console.log(this.isFavorite);
  }
}
