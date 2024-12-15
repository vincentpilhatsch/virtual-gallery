import {Component, OnInit, ViewEncapsulation} from '@angular/core';
import {MatButton} from "@angular/material/button";
import {EventService} from "../services/event.service";
import {HttpErrorResponse} from "@angular/common/http";
import {Event} from "../model/event";
import {NgClass} from "@angular/common";
import {UserService} from "../../user/services/user.service";
import {ConvertNumberArrayToDateStringPipe} from "../pipes/convert-number-array-to-date-time.pipe";
import {SnackBarService} from "../../misc/services/snack-bar.service";
import {ConvertNumberArrayToYearStringPipe} from "../../exhibitions/pipes/convert-number-array-to-year.pipe";

@Component({
  selector: 'app-events',
  standalone: true,
  imports: [
    MatButton,
    ConvertNumberArrayToDateStringPipe,
    NgClass,
    ConvertNumberArrayToYearStringPipe
  ],
  templateUrl: './events.component.html',
  styleUrl: './events.component.scss',
  encapsulation: ViewEncapsulation.None
})
export class EventsComponent implements OnInit {
  upcomingEvents: Event[] = [];
  pastEvents: Event[] = [];
  groupedPastEvents: Event[][] = [];

  constructor(protected userService: UserService,
              private readonly snackBarService: SnackBarService,
              private readonly eventService: EventService) {
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
    this.setGroupedEvents(this.pastEvents);
  }

  private convertNumberArrayToDate(numberArray: number[]): Date {
    const [year, month, day, hour, minute] = numberArray;
    return new Date(year, month - 1, day, hour, minute);
  }

  private setGroupedEvents(exhibitions: Event[]): void {
    for (let i = 0; i < exhibitions.length; i += 3) {
      this.groupedPastEvents.push(exhibitions.slice(i, i + 3));
    }
  }

  onButtonClicked(eventId: number) {
    this.updateFrontendEventArray(eventId);
    this.updateOneAcceptedEvent(eventId);
  }

  private updateOneAcceptedEvent(eventId: number): void {
    const email: string | undefined = this.userService.user?.email;
    if (email) {
      this.userService.updateOneAcceptedEvent(email, eventId).subscribe({
        error: (err: HttpErrorResponse): void => {
          console.error(err)
        }
      })
    }
  }

  private updateFrontendEventArray(eventId: number): void {
    const event: Event | null = this.findEventById(eventId);
    const userId: string | undefined = this.userService.user?.id;
    if (event !== null && userId !== undefined) {
      this.updateConfirmedUserInEvent(eventId, userId)
    }
  }

  private findEventById(eventId: number): Event | null {
    const eventInUpcoming = this.upcomingEvents.find(event => event.id === eventId);
    if (eventInUpcoming) {
      return eventInUpcoming;
    }
    const eventInPast = this.pastEvents.find(event => event.id === eventId);
    if (eventInPast) {
      return eventInPast;
    }
    return null;
  }

  private updateConfirmedUserInEvent(eventId: number, userId: string): void {
    const updateEventArray = (events: Event[]): boolean => {
      const event = events.find(event => event.id === eventId);
      if (event) {
        if (event.confirmedUserIds.includes(userId)) {
          this.snackBarService.openSnackBarByJSON("info", "snackbar.event.canceled")
          event.confirmedUserIds = event.confirmedUserIds.filter(id => id !== userId)
        } else {
          this.snackBarService.openSnackBarByJSON("info", "snackbar.event.accept")
          event.confirmedUserIds = [...event.confirmedUserIds, userId];
        }
        return true;
      }
      return false;
    };

    if (updateEventArray(this.upcomingEvents) || updateEventArray(this.pastEvents)) {
      this.upcomingEvents = [...this.upcomingEvents];
      this.pastEvents = [...this.pastEvents];
    }
  }

}
