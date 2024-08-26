import {Component, ViewEncapsulation} from '@angular/core';
import {FavoriteIconComponent} from "../../../components/favorite-icon/favorite-icon.component";
import {MatButton} from "@angular/material/button";
import {MatDivider} from "@angular/material/divider";
import {MatList, MatListItem} from "@angular/material/list";
import {NgOptimizedImage} from "@angular/common";
import {MatCheckbox} from "@angular/material/checkbox";

@Component({
  selector: 'app-request-artwork',
  standalone: true,
  imports: [
    FavoriteIconComponent,
    MatButton,
    MatDivider,
    MatList,
    MatListItem,
    NgOptimizedImage,
    MatCheckbox,
  ],
  templateUrl: './request-artwork.component.html',
  styleUrl: './request-artwork.component.scss',
  encapsulation: ViewEncapsulation.None
})
export class RequestArtworkComponent {

}
