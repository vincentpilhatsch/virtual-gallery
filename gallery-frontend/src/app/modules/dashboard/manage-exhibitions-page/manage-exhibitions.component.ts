import {Component, OnDestroy, OnInit, ViewEncapsulation} from '@angular/core';
import {MatFormField, MatLabel} from "@angular/material/form-field";
import {MatInput} from "@angular/material/input";
import {MatIcon} from "@angular/material/icon";
import {Router, RouterLink} from "@angular/router";
import {Subscription} from "rxjs";
import {HttpErrorResponse} from "@angular/common/http";
import {NgClass} from "@angular/common";
import {FormsModule} from "@angular/forms";
import {ConvertNumberArrayToYearStringPipe} from "../../exhibitions/pipes/convert-number-array-to-year.pipe";
import {Exhibition} from "../../exhibitions/model/exhibition";
import {ExhibitionService} from "../../exhibitions/services/exhibition.service";

@Component({
  selector: 'app-manage-exhibitions',
  standalone: true,
  imports: [
    MatFormField,
    MatInput,
    MatLabel,
    MatIcon,
    RouterLink,
    NgClass,
    FormsModule,
    ConvertNumberArrayToYearStringPipe
  ],
  templateUrl: './manage-exhibitions.component.html',
  styleUrl: './manage-exhibitions.component.scss',
  encapsulation: ViewEncapsulation.None
})
export class ManageExhibitionsComponent implements OnInit, OnDestroy {
  exhibitionPerRow = 4;
  exhibitions: Exhibition[] = [];
  groupedExhibitions: Exhibition[][] = [];

  filteredExhibitions: Exhibition[] = [];
  searchQuery: string = '';

  subscriptions: Subscription[] = [];

  constructor(private readonly exhibitionService: ExhibitionService,
              private readonly router: Router) {
  }

  ngOnInit() {
    this.loadExhibition();
  }

  private loadExhibition() {
    this.exhibitionService.getExhibitions().subscribe({
      next: (res: Exhibition[]): void => {
        this.exhibitions = res;
        this.filteredExhibitions = res;
        this.setGroupedExhibition();
      },
      error: (err: HttpErrorResponse): void => {
        console.error(err);
      }
    })
  }

  onSearch(): void {
    if (this.searchQuery.trim()) {
      this.filteredExhibitions = this.exhibitions.filter((exhibition) =>
        exhibition.title.toLowerCase().includes(this.searchQuery.toLowerCase())
      );
    } else {
      this.filteredExhibitions = this.exhibitions;
    }
    this.setGroupedExhibition();
  }

  private setGroupedExhibition(): void {
    this.groupedExhibitions = [];
    for (let i = 0; i < this.filteredExhibitions.length; i += this.exhibitionPerRow) {
      this.groupedExhibitions.push(this.filteredExhibitions.slice(i, i + this.exhibitionPerRow));
    }
  }

  protected onCardClicked(exhibitionId: number): void {
    void this.router.navigate(["dashboard", "ausstellung-bearbeiten", exhibitionId]);
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach(sub => {
      if (sub) {
        sub.unsubscribe();
      }
    })
  }
}
