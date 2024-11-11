import {Component, Input, OnDestroy, ViewEncapsulation} from '@angular/core';
import {MatIcon} from "@angular/material/icon";
import {HttpErrorResponse} from "@angular/common/http";
import {UserService} from "../../user/services/user.service";
import {FavoriteIconService} from "../services/favorite-icon.service";
import {Subscription} from "rxjs";

@Component({
  selector: 'app-favorite-icon',
  standalone: true,
  imports: [
    MatIcon
  ],
  templateUrl: './favorite-icon.component.html',
  styleUrl: './favorite-icon.component.scss',
  encapsulation: ViewEncapsulation.None
})
export class FavoriteIconComponent implements OnDestroy {
  @Input() artworkId: number | undefined;
  @Input() isFavorite: boolean | undefined;

  subscriptions: Subscription[] = [];

  constructor(private userService: UserService,
              private favoriteIconService: FavoriteIconService) {
  }

  handleFavoriteClicked(isFavorite: boolean) {
    this.isFavorite = isFavorite;
    this.updateFavoriteArtworkSubject();
    this.subscribeToUser();
  }

  private subscribeToUser() {
    this.subscriptions.push(
      this.userService.user$.subscribe((user) => {
        if (user) {
          this.updateFavoriteArtworksOfUser(user.email);
        }
      })
    );
  }

  private updateFavoriteArtworkSubject() {
    if (this.artworkId && !this.isFavorite) {
      this.favoriteIconService.removeArtworkFromFavorites(this.artworkId);
    }
  }

  private updateFavoriteArtworksOfUser(email: string): void {
    if (this.userService.user?.id && this.artworkId) {
      this.userService.updateFavoriteArtworksOfUser(email, this.artworkId).subscribe({
        error: (err: HttpErrorResponse): void => {
          console.error(err)
        }
      })
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
