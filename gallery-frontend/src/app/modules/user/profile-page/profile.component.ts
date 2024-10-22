import {Component, ViewEncapsulation} from '@angular/core';
import {MatButton} from "@angular/material/button";
import {MatFormField, MatLabel} from "@angular/material/form-field";
import {MatInput} from "@angular/material/input";
import {ArtworkCardComponent} from "../../artwork/artwork-card/artwork-card.component";
import {MatTab, MatTabGroup} from "@angular/material/tabs";
import {FavoriteArtworksComponent} from "./favorite-artworks/favorite-artworks.component";
import {PersonalDataComponent} from "./personal-data/personal-data.component";
import {MatListOption, MatSelectionList} from "@angular/material/list";
import {FormsModule} from "@angular/forms";
import {RequestedArtworksComponent} from "./requested-artworks/requested-artworks.component";
import {AuthService} from "../services/auth.service";

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
    MatTab,
    FavoriteArtworksComponent,
    PersonalDataComponent,
    MatSelectionList,
    MatListOption,
    FormsModule,
    RequestedArtworksComponent
  ],
  // providers: [{provide: HTTP_INTERCEPTORS, useClass: JwtInterceptor, multi: true}],
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.scss',
  encapsulation: ViewEncapsulation.None
})
export class ProfileComponent {

  constructor(protected authService: AuthService) {
  }


  // selectedSubProfilePage: SubProfilePagesEnum = SubProfilePagesEnum.PERSONAL_DATA;
  // onSelectionChange(event: MatSelectionListChange) {
  //   this.selectedSubProfilePage = event.options[0].value;
  // }
  // protected readonly SubProfilePagesEnum = SubProfilePagesEnum;
}
