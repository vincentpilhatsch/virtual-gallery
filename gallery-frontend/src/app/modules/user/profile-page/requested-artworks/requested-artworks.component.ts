import {Component, Input, OnInit, ViewEncapsulation} from '@angular/core';
import {Artwork} from "../../../artwork/model/artwork";
import {HttpErrorResponse} from "@angular/common/http";
import {UserService} from "../../services/user.service";
import {ArtworkCardComponent} from "../../../artwork/artwork-card/artwork-card.component";
import {FavoriteIconComponent} from "../../../misc/favorite-icon/favorite-icon.component";
import {Router} from "@angular/router";
import {MatButton} from "@angular/material/button";
import {User} from "../../model/user";
import {MoneyConverterPipe} from "../../../artwork/pipes/money-converter.pipe";
import {MatIcon} from "@angular/material/icon";
import {ArtworkAndArtworkRequest} from "../../../artwork/model/artworkAndArtworkRequest";
import {ArtworkRequestService} from "../../../artwork/services/artwork-request.service";
import {SnackBarService} from "../../../misc/services/snack-bar.service";

@Component({
  selector: 'app-requested-artworks',
  standalone: true,
  imports: [
    ArtworkCardComponent,
    FavoriteIconComponent,
    MatButton,
    MoneyConverterPipe,
    MatIcon
  ],
  templateUrl: './requested-artworks.component.html',
  styleUrls: ['./requested-artworks.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class RequestedArtworksComponent implements OnInit {
  @Input() userFromParent: User | undefined;

  artworksPerRow = 3;
  groupedArtworks: Artwork[][] = [];
  artworksAndArtworkRequests: ArtworkAndArtworkRequest[] = [];

  constructor(private readonly userService: UserService,
              private readonly artworkRequestService: ArtworkRequestService,
              private readonly snackBarService: SnackBarService,
              private readonly router: Router) {
  }

  ngOnInit(): void {
    this.loadRequestedArtworks();
  }

  private loadRequestedArtworks() {
    const email: string | undefined = this.userFromParent?.email;
    if (email) {
      this.userService.getArtworksAndArtworkRequestsByEmail(email).subscribe({
        next: (res: ArtworkAndArtworkRequest[]): void => {
          const artworks: Artwork[] = res.map(item => item.artwork);
          this.setGroupedArtworks(artworks);
          this.artworksAndArtworkRequests = res;
        },
        error: (err: HttpErrorResponse): void => {
          console.error(err);
        }
      });
    }
  }

  private setGroupedArtworks(favoriteArtworks: Artwork[]): void {
    for (let i = 0; i < favoriteArtworks.length; i += this.artworksPerRow) {
      this.groupedArtworks.push(favoriteArtworks.slice(i, i + this.artworksPerRow));
    }
  }

  handleCardClick(artworkId: number): void {
    void this.router.navigate(["kunstwerk", artworkId]);
  }

  handleWithdrawRequest(artworkId: number): void {
    this.deleteArtworkRequest(artworkId);
    this.updateGroupedArtworks(artworkId);
    // sendEmail(); hier dann
  }

  private deleteArtworkRequest(artworkId: number): void {
    const artworkRequestId: number | undefined = this.getArtworkRequestIdToDelete(artworkId);
    if (artworkRequestId && artworkRequestId !== -1) {
      this.snackBarService.openSnackBarByJSON("info", "snackbar.artwork.request.canceled");
      this.artworkRequestService.deleteArtworkRequestById(artworkRequestId).subscribe();
    }
  }

  private getArtworkRequestIdToDelete(artworkId: number): number | undefined {
    const match: ArtworkAndArtworkRequest | undefined =
      this.artworksAndArtworkRequests.find(
        (artworkAndRequest: ArtworkAndArtworkRequest) =>
          artworkAndRequest.artworkRequest.artworkId === artworkId
      );
    return match ? match.artworkRequest.id : -1;
  }

  private updateGroupedArtworks(artworkId: number) {
    for (const element of this.groupedArtworks) {
      const artworkGroup = element;
      const artworkIndex = artworkGroup.findIndex(artwork => artwork.id === artworkId);

      if (artworkIndex > -1) {
        artworkGroup.splice(artworkIndex, 1);
        const tempGroupedArtworks = this.groupedArtworks;
        this.groupedArtworks = [];
        this.setGroupedArtworks(tempGroupedArtworks.flat());
        return;
      }
    }
  }
}
