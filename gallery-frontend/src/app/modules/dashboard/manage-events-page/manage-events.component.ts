import {Component, ViewEncapsulation} from '@angular/core';
import {MatButton} from "@angular/material/button";
import {RouterLink} from "@angular/router";

@Component({
  selector: 'app-manage-events',
  standalone: true,
  imports: [
    MatButton,
    RouterLink
  ],
  templateUrl: './manage-events.component.html',
  styleUrl: './manage-events.component.scss',
  encapsulation: ViewEncapsulation.None
})
export class ManageEventsComponent {

}
