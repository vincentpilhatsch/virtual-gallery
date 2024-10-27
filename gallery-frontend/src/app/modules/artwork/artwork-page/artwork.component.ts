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
    RouterLink
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

  constructor(private route: ActivatedRoute,
              private artworkService: ArtworkService,
              private userService: UserService) {
  }

  ngOnInit() {
    this.subscribeToUser();
    this.loadFourRandomArtworks();
  }

  private subscribeToUser() {
    this.subscriptions.push(
      this.userService.user$.subscribe((user) => {
        if (user) {
          this.loadArtworkAndFavoriteIds(user.email);
        }
      })
    );
  }

  private loadArtworkAndFavoriteIds(email: string): void {
    this.route.paramMap.subscribe(params => {
      const id: string | null = params.get('id');
      if (id && email) {
        const convertedId = Number(id);
        this.forkJoinArtworkAndFavoriteIds(convertedId, email);
      }
    });
  }

  private forkJoinArtworkAndFavoriteIds(id: number, email: string): void {
    forkJoin({
      artwork: this.artworkService.getArtworkById(id),
      favoriteIds: this.userService.getFavoriteArtworkIdsByEmail(email)
    }).subscribe({
      next: (res: { artwork: Artwork, favoriteIds: number[] }): void => {
        this.artwork = res.artwork;
        this.favoriteIds = res.favoriteIds;
        this.isFavorite = res.favoriteIds.includes(this.artwork.id);
      },
      error: (err: HttpErrorResponse): void => {
        console.error(err);
      }
    });
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
