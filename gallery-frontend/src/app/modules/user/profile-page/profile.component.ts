import {Component, ViewEncapsulation} from '@angular/core';
import {MatButton} from "@angular/material/button";
import {MatFormField, MatLabel} from "@angular/material/form-field";
import {MatInput} from "@angular/material/input";
import {ArtworkCardComponent} from "../../artwork/artwork-card/artwork-card.component";
import {MatTab, MatTabGroup} from "@angular/material/tabs";

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [
    MatButton,
    MatFormField,
    MatInput,
    MatLabel,
    ArtworkCardComponent,
    MatTabGroup,
    MatTab
  ],
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.scss',
  encapsulation: ViewEncapsulation.None
})
export class ProfileComponent {

}
