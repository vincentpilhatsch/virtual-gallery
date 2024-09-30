import {Component, ViewEncapsulation} from '@angular/core';
import {MatIcon} from "@angular/material/icon";
import {RouterLink, RouterLinkActive} from "@angular/router";
import {MatFormField} from "@angular/material/form-field";
import {MatMenu, MatMenuModule} from "@angular/material/menu";
import {MatButtonModule} from "@angular/material/button";
import {MatInput} from "@angular/material/input";

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [
    MatIcon,
    RouterLink,
    MatMenu,
    MatButtonModule,
    MatMenuModule,
    MatFormField,
    MatInput,
    RouterLinkActive
  ],
  templateUrl: './header.component.html',
  styleUrl: './header.component.scss',
  encapsulation: ViewEncapsulation.None
})
export class HeaderComponent {
  selectedLanguage = 'DE';

}
