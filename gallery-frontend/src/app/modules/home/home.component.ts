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
import {ToUpperCasePipe} from "../misc/pipes/to-upper-case.pipe";
import {ExhibitionService} from "../exhibitions/services/exhibition.service";
import {Exhibition} from "../exhibitions/model/exhibition";
import {ConvertNumberArrayToDateStringPipe} from "../event/pipes/convert-number-array-to-date.pipe";
import {MatIcon} from "@angular/material/icon";


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
    ToUpperCasePipe,
    ConvertNumberArrayToDateStringPipe,
    MatIcon,
  ],
  standalone: true
})
export class HomeComponent implements OnInit, OnDestroy {
  artworks: Artwork[] = [];
  favoriteIds: number[] = [];
  latestExhibition: Exhibition | undefined;
  readonly NUMBERS_OF_RANDOM_ARTWORKS: number = 3;

  subscriptions: Subscription[] = [];

  constructor(protected userService: UserService,
              private readonly exhibitionService: ExhibitionService,
              private readonly artworkService: ArtworkService) {
  }

  ngOnInit() {
    this.subscribeToUser();
    // AOS.init({
    //   duration: 750, // Dauer der Animation in Millisekunden
    //   delay: 150,    // Verzögerung vor dem Start der Animation
    // });
  }

  private subscribeToUser() {
    this.loadLatestExhibition();
    this.subscriptions.push(this.userService.user$.subscribe((user) => {
        if (user) {
          this.loadRandomArtworksAndFavoriteIds(user.email);
        } else {
          this.loadRandomArtworks();
        }
      })
    );
  }

  private loadRandomArtworksAndFavoriteIds(email: string): void {
    forkJoin([
      this.artworkService.getRandomArtworks(this.NUMBERS_OF_RANDOM_ARTWORKS),
      this.userService.getFavoriteArtworkIdsByEmail(email)
    ]).subscribe({
      next: ([artworks, favoriteArtworkIds]: [Artwork[], number[]]): void => {
        this.artworks = artworks;
        this.favoriteIds = favoriteArtworkIds;
      },
      error: (err: HttpErrorResponse): void => {
        console.error(err);
      }
    });
  }

  private loadRandomArtworks() {
    this.artworkService.getRandomArtworks(this.NUMBERS_OF_RANDOM_ARTWORKS).subscribe({
      next: (res: Artwork[]): void => {
        this.artworks = res;
      },
      error: (err: HttpErrorResponse): void => {
        console.error(err);
      }
    })
  }

  private loadLatestExhibition() {
    this.exhibitionService.getLatestExhibitions().subscribe({
      next: (res: Exhibition): void => {
        this.latestExhibition = res;
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
