import {Component, OnInit, ViewEncapsulation} from '@angular/core';
import {Artwork} from "../../../artwork/model/artwork";
import {HttpErrorResponse} from "@angular/common/http";
import {UserService} from "../../services/user.service";
import {ArtworkCardComponent} from "../../../artwork/artwork-card/artwork-card.component";
import {FavoriteIconComponent} from "../../../misc/favorite-icon/favorite-icon.component";
import {Router} from "@angular/router";
import {MatButton} from "@angular/material/button";
import {User} from "../../model/user";

@Component({
  selector: 'app-requested-artworks',
  standalone: true,
  imports: [
    ArtworkCardComponent,
    FavoriteIconComponent,
    MatButton
  ],
  templateUrl: './requested-artworks.component.html',
  styleUrls: ['./requested-artworks.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class RequestedArtworksComponent implements OnInit {
  groupedArtworks: Artwork[][] = [];

  constructor(private userService: UserService, private router: Router) {
  }

  ngOnInit(): void {
    this.loadRequestedArtworks();
  }

  private loadRequestedArtworks() {
    const email = this.userService.user?.email;
    if (email) {
      this.userService.getRequestedArtworksByEmail(email).subscribe({
        next: (res: Artwork[]): void => {
          this.setGroupedArtworks(res);
        },
        error: (err: HttpErrorResponse): void => {
          console.error(err);
        }
      });
    }
  }

  private setGroupedArtworks(favoriteArtworks: Artwork[]): void {
    for (let i = 0; i < favoriteArtworks.length; i += 3) {
      this.groupedArtworks.push(favoriteArtworks.slice(i, i + 3));
    }
  }

  handleCardClick(artworkId: number): void {
    void this.router.navigate(["kunstwerk", artworkId]);
  }

  handleWithdrawRequest(artworkId: number): void {
    this.updateOneRequestedArtwork(artworkId);
    // sendEmail();
  }

  private updateOneRequestedArtwork(artworkId: number) {
    const user: User | undefined = this.userService.user;
    if (user) {
      this.userService.updateOneRequestedArtwork(user.email, artworkId).subscribe({
        next: (res): void => {
          console.log(res);
          // get new groupedArray from res
        },
        error: (err: HttpErrorResponse): void => {
          console.error(err)
        }
      })
    }
  }
}
