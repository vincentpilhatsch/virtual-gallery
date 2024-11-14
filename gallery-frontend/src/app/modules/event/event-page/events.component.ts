import {Component, OnInit, ViewEncapsulation} from '@angular/core';
import {MatButton} from "@angular/material/button";
import {EventService} from "../services/event.service";
import {AuthResponse} from "../../user/model/authResponse";
import {HttpErrorResponse} from "@angular/common/http";
import {Event} from "../model/event";
import {ConvertNumberArrayToDateStringPipe} from "../pipes/convert-number-array-to-date.pipe";
import {NgClass} from "@angular/common";
import {UserService} from "../../user/services/user.service";

@Component({
  selector: 'app-events',
  standalone: true,
  imports: [
    MatButton,
    ConvertNumberArrayToDateStringPipe,
    NgClass
  ],
  templateUrl: './events.component.html',
  styleUrl: './events.component.scss',
  encapsulation: ViewEncapsulation.None
})
export class EventsComponent implements OnInit {
  upcomingEvents: Event[] = [];
  pastEvents: Event[] = [];

  constructor(protected userService: UserService,
              private eventService: EventService) {
  }

  ngOnInit(): void {
    this.loadEvents();
  }

  private loadEvents(): void {
    this.eventService.getEvents().subscribe({
      next: (res: Event[]): void => {
        this.sortEvents(res);
      },
      error: (err: HttpErrorResponse): void => {
        console.error(err);
      }
    });
  }

  private sortEvents(events: Event[]) {
    events.map(event => {
      const eventDate: Date = this.convertNumberArrayToDate(event.dateTime);
      const currentDate: Date = new Date();
      if (eventDate > currentDate) {
        this.upcomingEvents.push(event)
      } else {
        this.pastEvents.push(event);
      }
    });
  }

  private convertNumberArrayToDate(numberArray: number[]): Date {
    const [year, month, day, hour, minute] = numberArray;
    return new Date(year, month - 1, day, hour, minute);
  }

  onButtonClicked(eventId: number) {
    // call backend with eventId and userId to set confirmedEvent
  }
}
