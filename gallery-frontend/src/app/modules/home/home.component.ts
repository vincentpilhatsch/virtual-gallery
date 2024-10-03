import {Component, OnInit, ViewEncapsulation} from '@angular/core';
import {MatButton} from "@angular/material/button";
import {NgOptimizedImage} from "@angular/common";
import {ArtworkCardComponent} from "../artwork/artwork-card/artwork-card.component";
import {TranslateModule} from "@ngx-translate/core";
import {HttpErrorResponse} from "@angular/common/http";
import {Artwork} from "../artwork/model/artwork";
import {UserService} from "../user/services/user.service";
import {ArtworkService} from "../artwork/services/artwork.service";
import {forkJoin} from "rxjs";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
  encapsulation: ViewEncapsulation.None,
  imports: [
    MatButton,
    NgOptimizedImage,
    ArtworkCardComponent,
    TranslateModule,
  ],
  standalone: true
})
export class HomeComponent implements OnInit {
  randomGroupedArtworks: Artwork[][] = [];
  favoriteIds: number[] = [];

  constructor(private userService: UserService,
              private artworkService: ArtworkService) {
  }

  ngOnInit() {
    this.loadFourArtworksAndFavoriteIds()
  }

  private loadFourArtworksAndFavoriteIds(): void {
    const email = this.userService.user?.email;
    if (email) {
      forkJoin([
        this.artworkService.getRandomArtworks(4),
        this.userService.getFavoriteArtworkIdsByEmail(email)
      ]).subscribe({
        next: ([artworks, favoriteArtworkIds]: [Artwork[], number[]]): void => {
          this.setRandomGroupedArtworks(artworks);
          this.favoriteIds = favoriteArtworkIds;
        },
        error: (err: HttpErrorResponse): void => {
          console.error(err);
        }
      });
    }
  }

  setRandomGroupedArtworks(artworks: Artwork[]) {
    for (let i = 0; i < artworks.length; i += 2) {
      this.randomGroupedArtworks.push(artworks.slice(i, i + 2));
    }
  }

}
