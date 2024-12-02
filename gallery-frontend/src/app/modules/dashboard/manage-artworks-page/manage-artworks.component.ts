import {Component, OnDestroy, OnInit, ViewEncapsulation} from '@angular/core';
import {MatFormField, MatLabel} from "@angular/material/form-field";
import {MatInput} from "@angular/material/input";
import {MatIcon} from "@angular/material/icon";
import {Router, RouterLink} from "@angular/router";
import {ArtworkCardComponent} from "../../artwork/artwork-card/artwork-card.component";
import {Artwork} from "../../artwork/model/artwork";
import {Subscription} from "rxjs";
import {ArtworkService} from "../../artwork/services/artwork.service";
import {BreakpointObserver, BreakpointState} from "@angular/cdk/layout";
import {HttpErrorResponse} from "@angular/common/http";
import {NgClass} from "@angular/common";
import {DashboardDataService} from "../services/dashboard-data.service";
import {FormsModule} from "@angular/forms";

@Component({
  selector: 'app-manage-artworks',
  standalone: true,
  imports: [
    MatFormField,
    MatInput,
    MatLabel,
    MatIcon,
    RouterLink,
    ArtworkCardComponent,
    NgClass,
    FormsModule
  ],
  templateUrl: './manage-artworks.component.html',
  styleUrl: './manage-artworks.component.scss',
  encapsulation: ViewEncapsulation.None
})
export class ManageArtworksComponent implements OnInit, OnDestroy {
  artworksPerRow = 4;
  artworks: Artwork[] = [];
  groupedArtworks: Artwork[][] = [];

  filteredArtworks: Artwork[] = [];
  searchQuery: string = '';

  subscriptions: Subscription[] = [];

  constructor(private readonly artworkService: ArtworkService,
              private readonly router: Router,
              private readonly responsive: BreakpointObserver,
              private readonly dashboardDataService: DashboardDataService) {
  }

  ngOnInit() {
    this.subscribeToBreakpoints();
    this.loadArtworks();
  }

  private subscribeToBreakpoints(): void {
    this.responsive
      .observe(['(min-width: 1500px)'])
      .subscribe((state: BreakpointState) => {
        this.artworksPerRow = state.matches ? 4 : 3;
        this.setGroupedArtworks();
      });
  }

  private loadArtworks() {
    this.artworkService.getArtworks().subscribe({
      next: (res: Artwork[]): void => {
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

  protected onCardClicked(artworkId: number) {
    void this.router.navigate(["dashboard/kunstwerk-bearbeiten", artworkId]);
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach(sub => {
      if (sub) {
        sub.unsubscribe();
      }
    })
  }
}
