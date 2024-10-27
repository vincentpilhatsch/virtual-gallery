import {Component, Input, OnInit, ViewEncapsulation} from '@angular/core';
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
  @Input() userFromParent: User | undefined;

  groupedArtworks: Artwork[][] = [];

  constructor(private userService: UserService, private router: Router) {
  }

  ngOnInit(): void {
    this.loadRequestedArtworks();
  }

  private loadRequestedArtworks() {
    const email: string | undefined = this.userFromParent?.email;
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
    // sendEmail(); hier dann
  }

  private updateOneRequestedArtwork(artworkId: number) {
    if (this.userFromParent?.email) {
      this.userService.updateOneRequestedArtwork(this.userFromParent.email, artworkId).subscribe({
        next: (res): void => {
          console.log(res);
        },
        error: (err: HttpErrorResponse): void => {
          console.error(err)
        }
      })
    }
  }
}
