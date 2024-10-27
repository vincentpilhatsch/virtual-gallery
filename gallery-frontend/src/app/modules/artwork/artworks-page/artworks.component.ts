import {Component, OnDestroy, OnInit, ViewEncapsulation} from '@angular/core';
import {MatFormField, MatLabel} from "@angular/material/form-field";
import {MatInput} from "@angular/material/input";
import {ArtworkCardComponent} from "../artwork-card/artwork-card.component";
import {ArtworkService} from "../services/artwork.service";
import {Artwork} from "../model/artwork";
import {HttpClientModule, HttpErrorResponse} from "@angular/common/http";
import {forkJoin, Subscription} from "rxjs";
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
export class ArtworksComponent implements OnInit, OnDestroy {
  artworks: Artwork[] = [];
  groupedArtworks: Artwork[][] = [];

  favoriteIds: number[] = [];

  subscriptions: Subscription[] = [];

  constructor(private artworkService: ArtworkService,
              private userService: UserService) {
  }

  ngOnInit() {
    this.subscribeToUser();
  }

  private subscribeToUser() {
    this.subscriptions.push(
      this.userService.user$.subscribe((user) => {
        if (user) {
          this.loadArtworksAndFavoriteIds(user.email);
        }
      })
    );
  }

  private loadArtworksAndFavoriteIds(email: string): void {
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

  ngOnDestroy(): void {
    this.subscriptions.forEach(sub => {
      if (sub) {
        sub.unsubscribe();
      }
    })
  }
}
