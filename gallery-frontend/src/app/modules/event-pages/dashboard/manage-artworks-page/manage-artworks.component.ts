import {Component, ViewEncapsulation} from '@angular/core';
import {MatFormField, MatLabel} from "@angular/material/form-field";
import {MatInput} from "@angular/material/input";
import {MatIcon} from "@angular/material/icon";
import {RouterLink} from "@angular/router";
import {ArtworkCardComponent} from "../../artwork/artwork-card/artwork-card.component";

@Component({
  selector: 'app-manage-artworks',
  standalone: true,
  imports: [
    MatFormField,
    MatInput,
    MatLabel,
    MatIcon,
    RouterLink,
    ArtworkCardComponent
  ],
  templateUrl: './manage-artworks.component.html',
  styleUrl: './manage-artworks.component.scss',
  encapsulation: ViewEncapsulation.None
})
export class ManageArtworksComponent {

}
