import {Component, Input, OnDestroy, OnInit, ViewEncapsulation} from '@angular/core';
import {Artwork} from "../../../artwork/model/artwork";
import {UserService} from "../../services/user.service";
import {FavoriteIconService} from "../../../misc/services/favorite-icon.service";
import {HttpErrorResponse} from "@angular/common/http";
import {ArtworkCardComponent} from "../../../artwork/artwork-card/artwork-card.component";
import {Subscription} from "rxjs";
import {User} from "../../model/user";
import {MatIcon} from "@angular/material/icon";

@Component({
  selector: 'app-favorite-artworks',
  standalone: true,
  imports: [
    ArtworkCardComponent,
    MatIcon
  ],
  templateUrl: './favorite-artworks.component.html',
  styleUrl: './favorite-artworks.component.scss',
  encapsulation: ViewEncapsulation.None
})
export class FavoriteArtworksComponent implements OnInit, OnDestroy {
  @Input() userFromParent: User | undefined;
  groupedArtworks: Artwork[][] = [];
  favoriteArtworkIds: number[] = [];

  subscriptions: Subscription[] = [];

  constructor(private readonly userService: UserService,
              private readonly favoriteIconService: FavoriteIconService) {
  }

  ngOnInit(): void {
    this.loadFavoriteArtworks();
    this.subscribeToFavoriteArtworkObserver();
  }

  private loadFavoriteArtworks() {
    const email: string | undefined = this.userFromParent?.email;
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
      if (artwork.id) {
        this.favoriteArtworkIds.push(artwork.id);
      }
    }
  }

  private subscribeToFavoriteArtworkObserver() {
    this.subscriptions.push(
      this.favoriteIconService.getFavoriteArtworkObserver().subscribe(artworkId => {
        this.updateFavoriteArtworkIds(artworkId);
        this.updateGroupedArtworks(artworkId);
      })
    );
  }

  private updateFavoriteArtworkIds(artworkId: number) {
    const index: number = this.favoriteArtworkIds.indexOf(artworkId);
    if (index > -1) {
      this.favoriteArtworkIds.splice(index, 1);
    }
  }

  private updateGroupedArtworks(artworkId: number) {
    for (const artworkGroup of this.groupedArtworks) {
      const artworkIndex = artworkGroup.findIndex(artwork => artwork.id === artworkId);
      if (artworkIndex > -1) {
        artworkGroup.splice(artworkIndex, 1);
        this.groupedArtworks = this.groupedArtworks.filter(group => group.length > 0);
        return;
      }
    }
  }


  ngOnDestroy(): void {
    this.subscriptions.forEach(sub => {
      if (sub) {
        sub.unsubscribe();
      }
    })
  }
}
