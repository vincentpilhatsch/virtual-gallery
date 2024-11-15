import {Component, OnDestroy, OnInit, ViewEncapsulation} from '@angular/core';
import {MatFormField, MatLabel} from "@angular/material/form-field";
import {MatInput} from "@angular/material/input";
import {ArtworkCardComponent} from "../artwork-card/artwork-card.component";
import {ArtworkService} from "../services/artwork.service";
import {Artwork} from "../model/artwork";
import {HttpClientModule, HttpErrorResponse} from "@angular/common/http";
import {forkJoin, Subscription} from "rxjs";
import {UserService} from "../../user/services/user.service";
import {BreakpointObserver, BreakpointState} from "@angular/cdk/layout";

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
  artworksPerRow = 4;

  artworks: Artwork[] = [];
  groupedArtworks: Artwork[][] = [];
  favoriteIds: number[] = [];

  subscriptions: Subscription[] = [];

  constructor(protected userService: UserService,
              private artworkService: ArtworkService,
              private responsive: BreakpointObserver) {
  }

  ngOnInit() {
    this.subscribeToBreakpoints();
    this.subscribeToUser();
  }

  private subscribeToBreakpoints(): void {
    this.responsive
      .observe(['(min-width: 1200px)'])
      .subscribe((state: BreakpointState) => {
        if (state.matches) {
          this.artworksPerRow = 4;
          console.log(this.artworksPerRow);
        } else {
          this.artworksPerRow = 3;
          console.log(this.artworksPerRow);
        }
        this.setGroupedArtworks(); // TODO: NOT WORKING!!!
      });
  }

  private subscribeToUser() {
    this.subscriptions.push(
      this.userService.user$.subscribe((user) => {
        if (user) {
          this.loadArtworksAndFavoriteIds(user.email);
        } else {
          this.loadArtworks();
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

  private loadArtworks() {
    this.artworkService.getArtworks().subscribe({
      next: (res: Artwork[]): void => {
        this.artworks = res;
        this.setGroupedArtworks();
      },
      error: (err: HttpErrorResponse): void => {
        console.error(err);
      }
    })
  }

  private setGroupedArtworks(): void {
    for (let i = 0; i < this.artworks.length; i += this.artworksPerRow) {
      this.groupedArtworks.push(this.artworks.slice(i, i + this.artworksPerRow));
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
