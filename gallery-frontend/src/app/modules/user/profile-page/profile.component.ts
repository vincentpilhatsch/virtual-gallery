import {Component, OnInit, ViewEncapsulation} from '@angular/core';
import {MatButton} from "@angular/material/button";
import {MatFormField, MatLabel} from "@angular/material/form-field";
import {MatInput} from "@angular/material/input";
import {ArtworkCardComponent} from "../../artwork/artwork-card/artwork-card.component";
import {MatTab, MatTabGroup} from "@angular/material/tabs";
import {UserService} from "../services/user.service";
import {HttpErrorResponse} from "@angular/common/http";
import {Artwork} from "../../artwork/model/artwork";
import {FavoriteIconService} from "../../misc/services/favorite-icon.service";

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [
    MatButton,
    MatFormField,
    MatInput,
    MatLabel,
    ArtworkCardComponent,
    MatTabGroup,
    MatTab
  ],
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.scss',
  encapsulation: ViewEncapsulation.None
})
export class ProfileComponent implements OnInit {
  groupedArtworks: Artwork[][] = [];
  favoriteArtworkIds: number[] = [];

  constructor(private userService: UserService, private favoriteIconService: FavoriteIconService) {
  }

  ngOnInit(): void {
    this.loadFavoriteArtworks();
    this.subscribeToFavoriteArtworkObserver();
  }

  private loadFavoriteArtworks() {
    const email = this.userService.user?.email;
    if (email) {
      this.userService.getFavoriteArtworksByEmail(email).subscribe({
        next: (res: Artwork[]): void => {
          this.setGroupedArtworks(res);
          this.setFavoriteArtworkIds(res)
        },
        error: (err: HttpErrorResponse): void => {
          console.error(err);
        }
      });
    }
  }

  private setGroupedArtworks(favoriteArtworks: Artwork[]): void {
    for (let i = 0; i < favoriteArtworks.length; i += 3) {
      this.groupedArtworks.push(favoriteArtworks.slice(i, i + 3));
    }
  }

  private setFavoriteArtworkIds(favoriteArtworks: Artwork[]): void {
    for (const artwork of favoriteArtworks) {
      this.favoriteArtworkIds.push(artwork.id);
    }
  }

  private subscribeToFavoriteArtworkObserver() {
    this.favoriteIconService.getFavoriteArtworkObserver().subscribe(artworkId => {
      this.updateFavoriteArtworkIds(artworkId);
      this.updateGroupedArtworks(artworkId);
    });
  }

  private updateFavoriteArtworkIds(artworkId: number) {
    const index: number = this.favoriteArtworkIds.indexOf(artworkId);
    if (index > -1) {
      this.favoriteArtworkIds.splice(index, 1);
    }
  }

  private updateGroupedArtworks(artworkId: number) {
    for (let i = 0; i < this.groupedArtworks.length; i++) {
      const artworkGroup = this.groupedArtworks[i];
      const artworkIndex = artworkGroup.findIndex(artwork => artwork.id === artworkId);

      if (artworkIndex > -1) {
        artworkGroup.splice(artworkIndex, 1);

        if (artworkGroup.length === 0) {
          this.groupedArtworks.splice(i, 1);
        }
        break;
      }
    }
    this.setGroupedArtworks(this.groupedArtworks.flat());
  }

}
