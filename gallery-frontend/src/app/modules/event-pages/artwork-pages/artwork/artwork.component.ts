import {Component, ViewEncapsulation} from '@angular/core';
import {NgOptimizedImage} from "@angular/common";
import {FavoriteIconComponent} from "../../../components/favorite-icon/favorite-icon.component";
import {MatList, MatListItem} from "@angular/material/list";
import {MatDivider} from "@angular/material/divider";
import {MatButton} from "@angular/material/button";
import {ArtworkCardComponent} from "../../../components/artwork-card/artwork-card.component";
import {RouterLink} from "@angular/router";

@Component({
  selector: 'app-artwork',
  standalone: true,
  imports: [
    NgOptimizedImage,
    FavoriteIconComponent,
    MatList,
    MatListItem,
    MatDivider,
    MatButton,
    ArtworkCardComponent,
    RouterLink
  ],
  templateUrl: './artwork.component.html',
  styleUrl: './artwork.component.scss',
  encapsulation: ViewEncapsulation.None
})
export class ArtworkComponent {

}
