import {Component, OnInit, ViewEncapsulation} from '@angular/core';
import {MatButton} from "@angular/material/button";
import {HttpErrorResponse} from "@angular/common/http";
import {ConvertNumberArrayToDateStringPipe} from "../pipes/convert-number-array-to-date.pipe";
import {NgClass} from "@angular/common";
import {UserService} from "../../user/services/user.service";
import {Exhibition} from "../model/exhibition";
import {ExhibitionService} from "../services/exhibition.service";
import {ArtworkCardComponent} from "../../artwork/artwork-card/artwork-card.component";
import {ConvertNumberArrayToYearStringPipe} from "../pipes/convert-number-array-to-year.pipe";

@Component({
  selector: 'app-exhibitions',
  standalone: true,
  imports: [
    MatButton,
    ConvertNumberArrayToDateStringPipe,
    NgClass,
    ArtworkCardComponent,
    ConvertNumberArrayToYearStringPipe
  ],
  templateUrl: './exhibitions.component.html',
  styleUrl: './exhibitions.component.scss',
  encapsulation: ViewEncapsulation.None
})
export class ExhibitionsComponent implements OnInit {
  upcomingExhibitions: Exhibition[] = [];
  pastExhibitions: Exhibition[] = [];
  groupedPastExhibitions: Exhibition[][] = [];

  constructor(protected userService: UserService,
              private readonly exhibitionsService: ExhibitionService) {
  }

  ngOnInit(): void {
    this.loadExhibitions();
  }

  private loadExhibitions(): void {
    this.exhibitionsService.getExhibitions().subscribe({
      next: (res: Exhibition[]): void => {
        this.sortExhibitions(res);
      },
      error: (err: HttpErrorResponse): void => {
        console.error(err);
      }
    });
  }

  private sortExhibitions(exhibitions: Exhibition[]) {
    exhibitions.forEach(exhibition => {
      const exhibitionEndDate: Date = this.convertNumberArrayToDate(exhibition.startDate)
      const currentDate: Date = new Date();
      if (exhibitionEndDate > currentDate) {
        this.upcomingExhibitions.push(exhibition)
      } else {
        this.pastExhibitions.push(exhibition);
      }
    });
    this.setGroupedExhibitions(this.pastExhibitions);
  }

  private setGroupedExhibitions(exhibitions: Exhibition[]): void {
    for (let i = 0; i < exhibitions.length; i += 3) {
      this.groupedPastExhibitions.push(exhibitions.slice(i, i + 3));
    }
  }

  private convertNumberArrayToDate(numberArray: number[]): Date {
    const [year, month, day] = numberArray;
    return new Date(year, month - 1, day);
  }
}
