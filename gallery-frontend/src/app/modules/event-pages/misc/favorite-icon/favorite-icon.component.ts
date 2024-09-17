import {Component, Input, ViewEncapsulation} from '@angular/core';
import {MatIcon} from "@angular/material/icon";
import {HttpErrorResponse} from "@angular/common/http";
import {UserService} from "../../user/services/user.service";

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
export class FavoriteIconComponent {
  @Input() artworkId: number | undefined;
  @Input() isFavorite: boolean | undefined;

  constructor(private userService: UserService) {
  }

  handleFavoriteClicked(isFavorite: boolean) {
    this.isFavorite = isFavorite;
    this.updateFavoriteArtworksOfUser();
  }

  updateFavoriteArtworksOfUser(): void {
    if (this.userService.user?.id && this.artworkId) {
      this.userService.updateFavoriteArtworksOfUser(this.userService.user.email, this.artworkId).subscribe({
        error: (err: HttpErrorResponse): void => {
          console.error(err)
        }
      })
    }
  }
}
