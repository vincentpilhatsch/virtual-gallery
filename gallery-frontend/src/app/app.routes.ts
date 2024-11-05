import {Routes} from '@angular/router';
import {ArtworkComponent} from "./modules/artwork/artwork-page/artwork.component";
import {ArtworksComponent} from "./modules/artwork/artworks-page/artworks.component";
import {RequestArtworkComponent} from "./modules/artwork/request-artwork/request-artwork.component";
import {ArtistComponent} from "./modules/artist/artist-page/artist.component";
import {EventsComponent} from "./modules/event/event-page/events.component";
import {LoginComponent} from "./modules/user/login-page/login.component";
import {RegisterComponent} from "./modules/user/register-page/register.component";
import {ProfileComponent} from "./modules/user/profile-page/profile.component";
import {DashboardComponent} from "./modules/dashboard/dashboard-page/dashboard.component";
import {ManageArtworksComponent} from "./modules/dashboard/manage-artworks-page/manage-artworks.component";
import {AddEditArtworkComponent} from "./modules/dashboard/add-edit-artwork-page/add-edit-artwork.component";
import {ManageEventsComponent} from "./modules/dashboard/manage-events-page/manage-events.component";
import {AddEditEventComponent} from "./modules/dashboard/add-and-edit-event-page/add-edit-event.component";
import {HomeComponent} from "./modules/home/home.component";

export const routes: Routes = [
  {path: '', redirectTo: '/anmelden', pathMatch: 'full'},
  {path: 'home', component: HomeComponent},
  {path: 'kunstwerk/:id', component: ArtworkComponent},
  {path: 'kunstwerke', component: ArtworksComponent},
  {path: 'kunstwerk-anfragen', component: RequestArtworkComponent}, //, canActivate: [userAndArtistGuard]},
  {path: 'künstler', component: ArtistComponent},
  {path: 'events', component: EventsComponent},
  {path: 'anmelden', component: LoginComponent}, // , canActivate: [authGuard]},
  {path: 'registrieren', component: RegisterComponent}, //, canActivate: [authGuard]},
  {path: 'profil', component: ProfileComponent}, //, canActivate: [userAndArtistGuard]},
  {path: 'dashboard', component: DashboardComponent}, //, canActivate: [artistGuard]},
  {path: 'dashboard/kunstwerke-verwalten', component: ManageArtworksComponent}, //, canActivate: [artistGuard]},
  {path: 'dashboard/kunstwerk-bearbeiten', component: AddEditArtworkComponent}, //, canActivate: [artistGuard]},
  {path: 'dashboard/kunstwerk-erstellen', component: AddEditArtworkComponent}, //, canActivate: [artistGuard]},
  {path: 'dashboard/events-verwalten', component: ManageEventsComponent}, //, canActivate: [artistGuard]},
  {path: 'dashboard/event-bearbeiten', component: AddEditEventComponent}, //, canActivate: [artistGuard]},
  {path: 'dashboard/event-erstellen', component: AddEditEventComponent}, //, canActivate: [artistGuard]},
  // {path: '**', redirectTo: ''},
];
