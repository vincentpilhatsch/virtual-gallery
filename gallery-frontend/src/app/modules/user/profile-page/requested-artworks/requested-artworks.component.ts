import {Component, OnInit} from '@angular/core';
import {Artwork} from "../../../artwork/model/artwork";
import {HttpErrorResponse} from "@angular/common/http";
import {UserService} from "../../services/user.service";

@Component({
  selector: 'app-requested-artworks',
  standalone: true,
  imports: [],
  templateUrl: './requested-artworks.component.html',
  styleUrl: './requested-artworks.component.scss'
})
export class RequestedArtworksComponent implements OnInit {
  constructor(private userService: UserService) {
  }

  ngOnInit(): void {
    this.loadRequestedArtworks();
  }

  private loadRequestedArtworks() {
    const email = this.userService.user?.email;
    if (email) {
      this.userService.getFavoriteArtworksByEmail(email).subscribe({
        next: (res: Artwork[]): void => {
          console.log(res);
        },
        error: (err: HttpErrorResponse): void => {
          console.error(err);
        }
      });
    }
  }
}
