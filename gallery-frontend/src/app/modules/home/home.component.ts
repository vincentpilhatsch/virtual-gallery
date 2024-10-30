import {Component, OnDestroy, OnInit, ViewEncapsulation} from '@angular/core';
import {MatButton} from "@angular/material/button";
import {NgOptimizedImage} from "@angular/common";
import {ArtworkCardComponent} from "../artwork/artwork-card/artwork-card.component";
import {TranslateModule} from "@ngx-translate/core";
import {HttpErrorResponse} from "@angular/common/http";
import {Artwork} from "../artwork/model/artwork";
import {UserService} from "../user/services/user.service";
import {ArtworkService} from "../artwork/services/artwork.service";
import {forkJoin, Subscription} from "rxjs";
import {RouterLink, RouterLinkActive} from "@angular/router";

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
    RouterLink,
    RouterLinkActive,
  ],
  standalone: true
})
export class HomeComponent implements OnInit, OnDestroy {
  randomGroupedArtworks: Artwork[][] = [];
  favoriteIds: number[] = [];
  readonly numbersOfRandomArtworks: number = 4;

  subscriptions: Subscription[] = [];

  constructor(protected userService: UserService,
              private artworkService: ArtworkService) {
  }

  ngOnInit() {
    this.subscribeToUser();
  }

  private subscribeToUser() {
    this.subscriptions.push(this.userService.user$.subscribe((user) => {
        if (user) {
          this.loadFourRandomArtworksAndFavoriteIds(user.email);
        } else {
          this.loadFourRandomArtworks();
        }
      })
    );
  }

  private loadFourRandomArtworksAndFavoriteIds(email: string): void {
    forkJoin([
      this.artworkService.getRandomArtworks(this.numbersOfRandomArtworks),
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

  setRandomGroupedArtworks(artworks: Artwork[]) {
    this.randomGroupedArtworks = [];
    for (let i = 0; i < artworks.length; i += 2) {
      this.randomGroupedArtworks.push(artworks.slice(i, i + 2));
    }
  }

  private loadFourRandomArtworks() {
    this.artworkService.getRandomArtworks(this.numbersOfRandomArtworks).subscribe({
      next: (res: Artwork[]): void => {
        this.setRandomGroupedArtworks(res);
      },
      error: (err: HttpErrorResponse): void => {
        console.error(err);
      }
    })
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach(sub => {
      if (sub) {
        sub.unsubscribe();
      }
    })
  }
}
