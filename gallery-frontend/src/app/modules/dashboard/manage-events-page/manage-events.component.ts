import {Component, OnDestroy, OnInit, ViewEncapsulation} from '@angular/core';
import {MatFormField, MatLabel} from "@angular/material/form-field";
import {MatInput} from "@angular/material/input";
import {MatIcon} from "@angular/material/icon";
import {Router, RouterLink} from "@angular/router";
import {Subscription} from "rxjs";
import {HttpErrorResponse} from "@angular/common/http";
import {FormsModule} from "@angular/forms";
import {ConvertNumberArrayToYearStringPipe} from "../../exhibitions/pipes/convert-number-array-to-year.pipe";
import {Event} from "../../event/model/event";
import {EventService} from "../../event/services/event.service";
import {MatProgressSpinner} from "@angular/material/progress-spinner";

@Component({
  selector: 'app-manage-events',
  standalone: true,
  imports: [
    RouterLink,
    ConvertNumberArrayToYearStringPipe,
    FormsModule,
    MatFormField,
    MatIcon,
    MatInput,
    MatLabel,
    MatProgressSpinner
  ],
  templateUrl: './manage-events.component.html',
  styleUrl: './manage-events.component.scss',
  encapsulation: ViewEncapsulation.None
})
export class ManageEventsComponent implements OnInit, OnDestroy {
  eventsPerRow = 3;
  events: Event[] = [];
  groupedEvents: Event[][] = [];

  filteredEvents: Event[] = [];
  searchQuery: string = '';

  subscriptions: Subscription[] = [];

  constructor(private readonly eventService: EventService,
              private readonly router: Router) {
  }

  ngOnInit() {
    this.loadEvents();
  }

  private loadEvents() {
    this.eventService.getEvents().subscribe({
      next: (res: Event[]): void => {
        this.events = res;
        this.filteredEvents = res;
        this.setGroupedEvents();
      },
      error: (err: HttpErrorResponse): void => {
        console.error(err);
      }
    })
  }

  onSearch(): void {
    if (this.searchQuery.trim()) {
      this.filteredEvents = this.events.filter((event: Event) =>
        event.title.toLowerCase().includes(this.searchQuery.toLowerCase())
      );
    } else {
      this.filteredEvents = this.events;
    }
    this.setGroupedEvents();
  }

  private setGroupedEvents(): void {
    this.groupedEvents = [];
    for (let i = 0; i < this.filteredEvents.length; i += this.eventsPerRow) {
      this.groupedEvents.push(this.filteredEvents.slice(i, i + this.eventsPerRow));
    }
  }

  protected onCardClicked(eventId: number): void {
    void this.router.navigate(["dashboard", "veranstaltung-bearbeiten", eventId]);
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach(sub => {
      if (sub) {
        sub.unsubscribe();
      }
    })
  }
}
