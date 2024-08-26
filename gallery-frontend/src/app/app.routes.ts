import {Routes} from '@angular/router';
import {LandingPageComponent} from "./modules/event-pages/landing-page/landing-page.component";
import {ArtworkExtendedCardComponent} from "./modules/components/artwork-extended-card/artwork-extended-card.component";
import {ArtworksComponent} from "./modules/event-pages/artwork-pages/artworks/artworks.component";
import {ArtworkComponent} from "./modules/event-pages/artwork-pages/artwork/artwork.component";
import {ArtistComponent} from "./modules/event-pages/artist-pages/artist/artist.component";
import {LoginComponent} from "./modules/event-pages/user-pages/login/login.component";
import {RegisterComponent} from "./modules/event-pages/user-pages/register/register.component";
import {EventsComponent} from "./modules/event-pages/event-pages/events/events.component";
import {ProfileComponent} from "./modules/event-pages/user-pages/profile/profile.component";
import {RequestArtworkComponent} from "./modules/event-pages/artwork-pages/request-artwork/request-artwork.component";
import {DashboardComponent} from "./modules/event-pages/dashboard-pages/dashboard/dashboard.component";
import {ManageArtworksComponent} from "./modules/event-pages/dashboard-pages/manage-artworks/manage-artworks.component";
import {ManageEventsComponent} from "./modules/event-pages/dashboard-pages/manage-events/manage-events.component";
import {
  AddEditArtworkComponent
} from "./modules/event-pages/dashboard-pages/add-edit-artwork/add-edit-artwork.component";
import {AddEditEventComponent} from "./modules/event-pages/dashboard-pages/add-and-edit-event/add-edit-event.component";

export const routes: Routes = [
  {path: '', redirectTo: '', pathMatch: 'full'},
  {path: '', component: LandingPageComponent},
  {path: 'kunstwerk', component: ArtworkComponent},
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
