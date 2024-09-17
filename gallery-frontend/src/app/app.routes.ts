import {Routes} from '@angular/router';
import {LandingPageComponent} from "./modules/event-pages/landing-page/landing-page.component";
import {ArtworkComponent} from "./modules/event-pages/artwork/artwork-page/artwork.component";
import {ArtworksComponent} from "./modules/event-pages/artwork/artworks-page/artworks.component";
import {RequestArtworkComponent} from "./modules/event-pages/artwork/request-artwork/request-artwork.component";
import {ArtistComponent} from "./modules/event-pages/artist/artist-page/artist.component";
import {EventsComponent} from "./modules/event-pages/event/event-page/events.component";
import {LoginComponent} from "./modules/event-pages/user/login-page/login.component";
import {RegisterComponent} from "./modules/event-pages/user/register-page/register.component";
import {ProfileComponent} from "./modules/event-pages/user/profile-page/profile.component";
import {DashboardComponent} from "./modules/event-pages/dashboard/dashboard-page/dashboard.component";
import {ManageArtworksComponent} from "./modules/event-pages/dashboard/manage-artworks-page/manage-artworks.component";
import {
  AddEditArtworkComponent
} from "./modules/event-pages/dashboard/add-edit-artwork-page/add-edit-artwork.component";
import {ManageEventsComponent} from "./modules/event-pages/dashboard/manage-events-page/manage-events.component";
import {AddEditEventComponent} from "./modules/event-pages/dashboard/add-and-edit-event-page/add-edit-event.component";

export const routes: Routes = [
  // {path: '', redirectTo: '', pathMatch: 'full'},
  {path: '', component: LandingPageComponent},
  {path: 'kunstwerk/:id', component: ArtworkComponent},
  {path: 'kunstwerke', component: ArtworksComponent},
  {path: 'kunstwerk-anfragen', component: RequestArtworkComponent},
  {path: 'künstler', component: ArtistComponent},
  {path: 'events', component: EventsComponent},
  {path: 'einloggen', component: LoginComponent},
  {path: 'registrieren', component: RegisterComponent},
  {path: 'profil', component: ProfileComponent},
  {path: 'dashboard', component: DashboardComponent},
  {path: 'dashboard/kunstwerke-verwalten', component: ManageArtworksComponent},
  {path: 'dashboard/kunstwerk-bearbeiten', component: AddEditArtworkComponent},
  {path: 'dashboard/kunstwerk-erstellen', component: AddEditArtworkComponent},
  {path: 'dashboard/events-verwalten', component: ManageEventsComponent},
  {path: 'dashboard/event-bearbeiten', component: AddEditEventComponent},
  {path: 'dashboard/event-erstellen', component: AddEditEventComponent},
  // {path: '**', redirectTo: ''},
];
