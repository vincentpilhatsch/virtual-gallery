import {Component, OnDestroy, OnInit, ViewEncapsulation} from '@angular/core';
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
import {UserService} from "../services/user.service";
import {User} from "../model/user";
import {Subscription} from "rxjs";
import {Router} from "@angular/router";

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
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.scss',
  encapsulation: ViewEncapsulation.None
})
export class ProfileComponent implements OnInit, OnDestroy { // TODO: CanActivate when user is logged in
  userFromParent: User | undefined;

  subscriptions: Subscription[] = [];

  constructor(private userService: UserService,
              private router: Router) {
  }

  ngOnInit(): void {
    this.subscribeToUser();
  }

  private subscribeToUser() {
    this.userService.user$.subscribe((user) => {
      if (user) {
        this.userFromParent = user;
      }
    });
  }

  logout() {
    this.userService.logout().subscribe({
      next: () => {
        this.userService.user = null;
        void this.router.navigate(["home"]);
      },
      error: (error) => console.error("Logout failed:", error),
    });
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach(sub => {
      if (sub) {
        sub.unsubscribe();
      }
    })
  }

}
