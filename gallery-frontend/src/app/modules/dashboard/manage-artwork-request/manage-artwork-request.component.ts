import {Component, OnDestroy, OnInit, ViewEncapsulation} from '@angular/core';
import {FormsModule} from "@angular/forms";
import {Router} from "@angular/router";
import {Subscription} from "rxjs";
import {HttpErrorResponse} from "@angular/common/http";
import {ArtworkRequestService} from "../../artwork/services/artwork-request.service";
import {CompleteArtworkRequest} from "../../artwork/model/completeArtworkRequest";
import {MatDivider} from "@angular/material/divider";
import {MatList, MatListItem} from "@angular/material/list";
import {MoneyConverterPipe} from "../../artwork/pipes/money-converter.pipe";
import {ConvertNumberArrayToYearStringPipe} from "../../exhibitions/pipes/convert-number-array-to-year.pipe";
import {ConvertNumberArrayToDateStringPipe} from "../../event/pipes/convert-number-array-to-date-time.pipe";
import {MatButton} from "@angular/material/button";
import {ArtworkRequest} from "../../artwork/model/artworkRequest";
import {MatProgressSpinner} from "@angular/material/progress-spinner";
import {SnackBarService} from "../../misc/services/snack-bar.service";
import {TranslateModule} from "@ngx-translate/core";

@Component({
  selector: 'app-manage-artwork-request',
  standalone: true,
  encapsulation: ViewEncapsulation.None,
  imports: [
    FormsModule,
    MatDivider,
    MatList,
    MatListItem,
    MoneyConverterPipe,
    ConvertNumberArrayToYearStringPipe,
    ConvertNumberArrayToDateStringPipe,
    MatButton,
    MatProgressSpinner,
    TranslateModule
  ],
  templateUrl: './manage-artwork-request.component.html',
  styleUrl: './manage-artwork-request.component.scss'
})
export class ManageArtworkRequestComponent implements OnInit, OnDestroy {
  completeArtworkRequestsPerRow = 3;
  completeArtworkRequests: CompleteArtworkRequest[] = [];
  groupedCompleteArtworkRequests: CompleteArtworkRequest[][] = [];

  filteredCompleteArtworkRequests: CompleteArtworkRequest[] = [];

  subscriptions: Subscription[] = [];

  constructor(private readonly artworkRequestService: ArtworkRequestService,
              private readonly router: Router,
              private readonly snackBarService: SnackBarService) {
  }

  ngOnInit() {
    this.loadArtworkRequests();
  }

  private loadArtworkRequests() {
    this.artworkRequestService.getCompleteArtworkRequests().subscribe({
      next: (res: CompleteArtworkRequest[]): void => {
        this.completeArtworkRequests = res;
        this.filteredCompleteArtworkRequests = res;
        this.setGroupedArtworkRequests();
      },
      error: (err: HttpErrorResponse): void => {
        console.error(err);
      }
    })
  }

  private setGroupedArtworkRequests(): void {
    this.groupedCompleteArtworkRequests = [];
    for (let i = 0; i < this.filteredCompleteArtworkRequests.length; i += this.completeArtworkRequestsPerRow) {
      this.groupedCompleteArtworkRequests.push(this.filteredCompleteArtworkRequests.slice(i, i + this.completeArtworkRequestsPerRow));
    }
  }

  acceptArtworkRequest(artworkRequest: ArtworkRequest): void {
    this.artworkRequestService.acceptArtworkRequest(artworkRequest).subscribe({
      next: (): void => {
        void this.router.navigate(["dashboard"]);
        const jsonString: string = "snackbar.dashboard.artwork.request.accepted";
        this.snackBarService.openSnackBarByJSON("info", jsonString);
      },
      error: (err: HttpErrorResponse): void => {
        console.error(err);
      }
    })
  }

  rejectArtworkRequest(artworkRequest: ArtworkRequest): void {
    this.artworkRequestService.rejectArtworkRequest(artworkRequest).subscribe({
      next: (): void => {
        void this.router.navigate(["dashboard"]);
        const jsonString: string = "snackbar.dashboard.artwork.request.rejected";
        this.snackBarService.openSnackBarByJSON("info", jsonString);
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
