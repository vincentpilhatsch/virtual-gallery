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
import {NgClass} from "@angular/common";
import {FormsModule} from "@angular/forms";
import {MatProgressSpinner} from "@angular/material/progress-spinner";

@Component({
  selector: 'app-artworks',
  standalone: true,
  imports: [
    MatFormField,
    MatInput,
    MatLabel,
    ArtworkCardComponent,
    HttpClientModule,
    NgClass,
    FormsModule,
    MatProgressSpinner
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

  filteredArtworks: Artwork[] = [];
  searchQuery: string = '';

  subscriptions: Subscription[] = [];

  constructor(protected userService: UserService,
              private readonly artworkService: ArtworkService,
              private readonly responsive: BreakpointObserver) {
  }

  ngOnInit() {
    this.subscribeToBreakpoints();
    this.subscribeToUser();
  }

  private subscribeToBreakpoints(): void {
    this.responsive
      .observe(['(min-width: 1500px)'])
      .subscribe((state: BreakpointState) => {
        this.artworksPerRow = state.matches ? 4 : 3;
        this.setGroupedArtworks();
      });
  }

  private subscribeToUser() {
    this.subscriptions.push(
      this.userService.user$.subscribe((user) => {
        if (user) {
          // TODO: When I'm in the artworks page and reload it, the fav artworks dont display in frontend
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
          this.filteredArtworks = artworks;
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
        // res.reverse();
        this.artworks = res;
        this.filteredArtworks = res;
        this.setGroupedArtworks();
      },
      error: (err: HttpErrorResponse): void => {
        console.error(err);
      }
    })
  }

  onSearch(): void {
    if (this.searchQuery.trim()) {
      this.filteredArtworks = this.artworks.filter((artwork) =>
        artwork.title.toLowerCase().includes(this.searchQuery.toLowerCase())
      );
    } else {
      this.filteredArtworks = this.artworks;
    }
    this.setGroupedArtworks();
  }

  private setGroupedArtworks(): void {
    this.groupedArtworks = [];
    for (let i = 0; i < this.filteredArtworks.length; i += this.artworksPerRow) {
      this.groupedArtworks.push(this.filteredArtworks.slice(i, i + this.artworksPerRow));
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
