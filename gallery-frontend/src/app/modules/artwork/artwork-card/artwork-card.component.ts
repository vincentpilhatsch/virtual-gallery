import {Component, Input, OnInit, ViewEncapsulation} from '@angular/core';
import {MatCard, MatCardActions, MatCardContent, MatCardImage} from "@angular/material/card";
import {MatIcon} from "@angular/material/icon";
import {FavoriteIconComponent} from "../../misc/favorite-icon/favorite-icon.component";
import {Router, RouterLink} from "@angular/router";
import {Artwork} from "../model/artwork";

@Component({
  selector: 'app-artwork-card',
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
  templateUrl: './artwork-card.component.html',
  styleUrl: './artwork-card.component.scss',
  encapsulation: ViewEncapsulation.None
})
export class ArtworkCardComponent implements OnInit {
  @Input() artwork: Artwork | undefined;
  @Input() favoriteArtworkIds: number[] | undefined;

  isFavorite: boolean = false;

  constructor(private router: Router) {}

  ngOnInit() {
    this.checkIfArtworkIsFavorite();
  }

  checkIfArtworkIsFavorite(): void {
    if (this.artwork && this.favoriteArtworkIds) {
      this.isFavorite = this.favoriteArtworkIds.includes(this.artwork.id)
    }
  }

  handleCardClick(): void {
    if (this.artwork?.id) {
      void this.router.navigate(["kunstwerk", this.artwork.id]);
    }
  }

}
