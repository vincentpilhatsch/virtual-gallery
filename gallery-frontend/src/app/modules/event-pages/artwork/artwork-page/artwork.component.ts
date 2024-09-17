import {Component, OnInit, ViewEncapsulation} from '@angular/core';
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
export class ArtworkComponent implements OnInit{
  artwork: Artwork | undefined;
  randomGroupedArtworks: Artwork[][] = [];

  favoriteIds: number[] = [];
  isFavorite: boolean = false;

  constructor(private route: ActivatedRoute,
              private artworkService: ArtworkService,
              private userService: UserService) {}

  ngOnInit() {
    this.loadArtwork();
    this.loadFourRandomArtworks();
  }

  private loadArtwork(): void {
    this.route.paramMap.subscribe(params => {
      const id: string | null = params.get('id');
      if (id) {
        const convertedId = Number(id);
        this.artworkService.getArtworkById(convertedId).subscribe({
          next: (res: Artwork): void => {
            this.artwork = res;
            this.checkIfIsFavoriteArtwork();
          },
          error: (err: HttpErrorResponse): void => {
            console.error(err)
          }
        });
      }
    });
  }

  private checkIfIsFavoriteArtwork(): void {
    const email: string | undefined = this.userService.user?.email;
    if (email) {
      this.userService.getFavoriteArtworkIdsByEmail(email).subscribe({
        next: (res: number[]): void => {
          if (this.artwork?.id) {
            this.isFavorite = res.includes(this.artwork?.id)
            this.favoriteIds = res;
          }
        },
        error: (err: HttpErrorResponse): void => {
          console.error(err)
        }
      })
    }
  }

  private loadFourRandomArtworks() {
    this.artworkService.getRandomArtworks(4).subscribe({
      next: (res: Artwork[]): void => {
        this.setRandomGroupedArtworks(res)
      },
      error: (err: HttpErrorResponse): void => {
        console.error(err)
      }
    })
  }

  setRandomGroupedArtworks(artworks: Artwork[]) {
    for (let i = 0; i < artworks.length; i += 2) {
      this.randomGroupedArtworks.push(artworks.slice(i, i + 2));
    }
  }
}
