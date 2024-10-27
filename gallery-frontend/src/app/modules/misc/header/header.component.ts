import {Component, ViewEncapsulation} from '@angular/core';
import {MatIcon} from "@angular/material/icon";
import {RouterLink, RouterLinkActive} from "@angular/router";
import {MatFormField, MatLabel} from "@angular/material/form-field";
import {MatMenu, MatMenuModule} from "@angular/material/menu";
import {MatButtonModule} from "@angular/material/button";
import {MatInput} from "@angular/material/input";
import {MatOption, MatSelect} from "@angular/material/select";
import {Language} from "../../user/model/language";
import {UserService} from "../../user/services/user.service";
import {FormsModule} from "@angular/forms";

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
    RouterLinkActive,
    MatLabel,
    MatSelect,
    MatOption,
    FormsModule
  ],
  templateUrl: './header.component.html',
  styleUrl: './header.component.scss',
  encapsulation: ViewEncapsulation.None
})
export class HeaderComponent {
  readonly languageArray: string[] = Object.values(Language);
  selectedLanguage: string = Language.DE;


  constructor(protected userService: UserService) {
  }

}
