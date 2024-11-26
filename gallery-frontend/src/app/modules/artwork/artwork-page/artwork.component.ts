import {Component, OnDestroy, OnInit, ViewEncapsulation} from '@angular/core';
import {NgOptimizedImage} from "@angular/common";
import {FavoriteIconComponent} from "../../misc/favorite-icon/favorite-icon.component";
import {MatList, MatListItem} from "@angular/material/list";
import {MatDivider} from "@angular/material/divider";
import {MatButton} from "@angular/material/button";
import {ActivatedRoute, RouterLink} from "@angular/router";
import {ArtworkService} from "../services/artwork.service";
import {Artwork} from "../model/artwork";
import {HttpErrorResponse} from "@angular/common/http";
import {ArtworkCardComponent} from "../artwork-card/artwork-card.component";
import {UserService} from "../../user/services/user.service";
import {forkJoin, Subscription} from "rxjs";
import {MoneyConverterPipe} from "../pipes/money-converter.pipe";
import {ArtworkRequest} from "../model/artworkRequest";

@Component({
  selector: 'app-artwork',
  standalone: true,
  imports: [
    NgOptimizedImage,
    FavoriteIconComponent,
    ArtworkCardComponent,
    MatList,
    MatListItem,
    MatDivider,
    MatButton,
    RouterLink,
    MoneyConverterPipe
  ],
  templateUrl: './artwork.component.html',
  styleUrl: './artwork.component.scss',
  encapsulation: ViewEncapsulation.None
})
export class ArtworkComponent implements OnInit, OnDestroy {
  artwork: Artwork | undefined;
  randomGroupedArtworks: Artwork[][] = [];

  subscriptions: Subscription[] = [];

  favoriteIds: number[] = [];
  isFavorite: boolean = false;
  isRequested: boolean = false;

  constructor(protected userService: UserService,
              private readonly route: ActivatedRoute,
              private readonly artworkService: ArtworkService) {
  }

  ngOnInit() {
    this.subscribeToUser();
    this.loadFourRandomArtworks();
  }

  private subscribeToUser() {
    this.subscriptions.push(
      this.userService.user$.subscribe((user) => {
        if (user) {
          this.loadArtworkIdFromParam(user.email);
        } else {
          this.loadArtworkIdFromParam();
        }
      })
    );
  }

  private loadArtworkIdFromParam(email?: string): void {
    this.route.paramMap.subscribe(params => {
      const id: string | null = params.get('id');
      if (id) {
        const convertedId = Number(id);
        if (email) {
          this.loadArtworkAndFavoriteIdsAndArtworkRequests(convertedId, email);
        } else {
          this.loadArtwork(convertedId);
        }
      }
    });
  }

  private loadArtworkAndFavoriteIdsAndArtworkRequests(id: number, email: string): void {
    forkJoin({
      artwork: this.artworkService.getArtworkById(id),
      favoriteIds: this.userService.getFavoriteArtworkIdsByEmail(email),
      artworkRequests: this.userService.getArtworkRequestsByEmail(email)
    }).subscribe({
      next: (res: { artwork: Artwork, favoriteIds: number[], artworkRequests: ArtworkRequest[] }): void => {
        this.artwork = res.artwork;
        this.favoriteIds = res.favoriteIds;
        this.isFavorite = res.favoriteIds.includes(this.artwork.id);
        this.isRequested = res.artworkRequests.some((artworkRequest: ArtworkRequest) =>
          artworkRequest.artworkId === res.artwork.id
        );
      },
      error: (err: HttpErrorResponse): void => {
        console.error(err);
      }
    });
  }

  private loadArtwork(id: number): void {
    this.artworkService.getArtworkById(id).subscribe({
      next: (res: Artwork) => {
        this.artwork = res;
      },
      error: (err: HttpErrorResponse) => {
        console.error(err);
      }
    })
  }

  private loadFourRandomArtworks(): void {
    this.artworkService.getRandomArtworks(4).subscribe({
      next: (res: Artwork[]): void => {
        this.setRandomGroupedArtworks(res)
      },
      error: (err: HttpErrorResponse): void => {
        console.error(err)
      }
    })
  }

  private setRandomGroupedArtworks(artworks: Artwork[]): void {
    for (let i = 0; i < artworks.length; i += 2) {
      this.randomGroupedArtworks.push(artworks.slice(i, i + 2));
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
