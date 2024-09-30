import {Component, ViewEncapsulation} from '@angular/core';
import {MatButton} from "@angular/material/button";

@Component({
  selector: 'app-events',
  standalone: true,
  imports: [
    MatButton
  ],
  templateUrl: './events.component.html',
  styleUrl: './events.component.scss',
  encapsulation: ViewEncapsulation.None
})
export class EventsComponent {

}
