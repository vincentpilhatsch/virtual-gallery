import {Component, ViewEncapsulation} from '@angular/core';
import {MatFormField, MatLabel} from "@angular/material/form-field";
import {MatInput} from "@angular/material/input";
import {ArtworkExtendedCardComponent} from "../../../components/artwork-extended-card/artwork-extended-card.component";

@Component({
  selector: 'app-artworks',
  standalone: true,
  imports: [
    MatFormField,
    MatInput,
    MatLabel,
    ArtworkExtendedCardComponent
  ],
  templateUrl: './artworks.component.html',
  styleUrl: './artworks.component.scss',
  encapsulation: ViewEncapsulation.None
})
export class ArtworksComponent {

}
