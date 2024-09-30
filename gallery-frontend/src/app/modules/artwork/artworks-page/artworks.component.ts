import {Component, OnInit, ViewEncapsulation} from '@angular/core';
import {MatFormField, MatLabel} from "@angular/material/form-field";
import {MatInput} from "@angular/material/input";
import {ArtworkCardComponent} from "../artwork-card/artwork-card.component";
import {ArtworkService} from "../services/artwork.service";
import {Artwork} from "../model/artwork";
import {HttpClientModule, HttpErrorResponse} from "@angular/common/http";
import {forkJoin} from "rxjs";
import {UserService} from "../../user/services/user.service";

@Component({
  selector: 'app-artworks',
  standalone: true,
  imports: [
    MatFormField,
    MatInput,
    MatLabel,
    ArtworkCardComponent,
    HttpClientModule
  ],
  templateUrl: './artworks.component.html',
  styleUrl: './artworks.component.scss',
  encapsulation: ViewEncapsulation.None
})
export class ArtworksComponent implements OnInit {
  artworks: Artwork[] = [];
  groupedArtworks: Artwork[][] = [];

  favoriteIds: number[] = [];

  constructor(private artworkService: ArtworkService,
              private userService: UserService) {
  }

  ngOnInit() {
    this.loadArtworksAndFavoriteIds();
  }

  private loadArtworksAndFavoriteIds(): void {
    const email = this.userService.user?.email;
    if (email) {
      forkJoin([
        this.artworkService.getArtworks(),
        this.userService.getFavoriteArtworkIdsByEmail(email)
      ]).subscribe({
        next: ([artworks, favoriteArtworkIds]: [Artwork[], number[]]): void => {
          this.artworks = artworks;
          this.favoriteIds = favoriteArtworkIds;
          this.setGroupedArtworks();
        },
        error: (err: HttpErrorResponse): void => {
          console.error(err);
        }
      });
    }
  }

  private setGroupedArtworks(): void {
    for (let i = 0; i < this.artworks.length; i += 3) {
      this.groupedArtworks.push(this.artworks.slice(i, i + 3));
    }
  }
}
