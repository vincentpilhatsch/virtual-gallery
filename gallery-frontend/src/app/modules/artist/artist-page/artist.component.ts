import {Component, ViewEncapsulation} from '@angular/core';
import {TranslateModule} from "@ngx-translate/core";

@Component({
  selector: 'app-artist',
  standalone: true,
  imports: [
    TranslateModule
  ],
  templateUrl: './artist.component.html',
  styleUrl: './artist.component.scss',
  encapsulation: ViewEncapsulation.None
})
export class ArtistComponent {

}
