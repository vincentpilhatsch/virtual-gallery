import {Component, ViewEncapsulation} from '@angular/core';
import {MatButton} from "@angular/material/button";
import {ArtworkCardComponent} from "../../../components/artwork-card/artwork-card.component";
import {RouterLink} from "@angular/router";

@Component({
  selector: 'app-manage-events',
  standalone: true,
  imports: [
    MatButton,
    ArtworkCardComponent,
    RouterLink
  ],
  templateUrl: './manage-events.component.html',
  styleUrl: './manage-events.component.scss',
  encapsulation: ViewEncapsulation.None
})
export class ManageEventsComponent {

}
