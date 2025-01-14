import {Routes} from '@angular/router';
import {ArtworkComponent} from "./modules/artwork/artwork-page/artwork.component";
import {ArtworksComponent} from "./modules/artwork/artworks-page/artworks.component";
import {ArtistComponent} from "./modules/artist/artist-page/artist.component";
import {EventsComponent} from "./modules/event/event-page/events.component";
import {LoginComponent} from "./modules/user/login-page/login.component";
import {RegisterComponent} from "./modules/user/register-page/register.component";
import {ProfileComponent} from "./modules/user/profile-page/profile.component";
import {DashboardComponent} from "./modules/dashboard/dashboard-page/dashboard.component";
import {ManageArtworksComponent} from "./modules/dashboard/manage-artworks-page/manage-artworks.component";
import {AddEditArtworkComponent} from "./modules/dashboard/add-edit-artwork-page/add-edit-artwork.component";
import {ManageEventsComponent} from "./modules/dashboard/manage-events-page/manage-events.component";
import {AddEditEventComponent} from "./modules/dashboard/add-edit-event-page/add-edit-event.component";
import {HomeComponent} from "./modules/home/home.component";
import {ExhibitionsComponent} from "./modules/exhibitions/exhibitions-page/exhibitions.component";
import {userAndArtistGuard} from "./modules/misc/guards/user-and-artist.guard";
import {authGuard} from "./modules/misc/guards/auth.guard";
import {TourComponent} from "./modules/tour/tour.component";
import {ManageExhibitionsComponent} from "./modules/dashboard/manage-exhibitions-page/manage-exhibitions.component";
import {AddEditExhibitionComponent} from "./modules/dashboard/add-edit-exhibition-page/add-edit-exhibition.component";
import {
  ManageArtworkRequestComponent
} from "./modules/dashboard/manage-artwork-request/manage-artwork-request.component";
import {artistGuard} from "./modules/misc/guards/artist.guard";

export const routes: Routes = [
  {
    path: '',
    redirectTo: '/anmelden',
    pathMatch: 'full'
  },
  {
    path: 'startseite',
    component: HomeComponent
  },
  {
    path: 'kunstwerk/:id',
    component: ArtworkComponent
  },
  {
    path: 'kunstwerke',
    component: ArtworksComponent
  },
  {
    path: 'künstler',
    component: ArtistComponent
  },
  {
    path: 'veranstaltungen',
    component: EventsComponent
  },
  {
    path: 'ausstellungen',
    component: ExhibitionsComponent
  },
  {
    path: 'tour',
    component: TourComponent
  },
  {
    path: 'anmelden',
    component: LoginComponent,
    canActivate: [authGuard]
  },
  {
    path: 'registrieren',
    component: RegisterComponent,
    canActivate: [authGuard]
  },
  {
    path: 'profil',
    component: ProfileComponent,
    canActivate: [userAndArtistGuard]
  },
  {
    path: 'dashboard',
    component: DashboardComponent,
    canActivate: [artistGuard]
  },
  {
    path: 'dashboard/kunstwerke-verwalten',
    component: ManageArtworksComponent,
    canActivate: [artistGuard]
  },
  {
    path: 'dashboard/kunstwerk-bearbeiten/:id',
    component: AddEditArtworkComponent,
    canActivate: [artistGuard]
  },
  {
    path: 'dashboard/kunstwerk-erstellen',
    component: AddEditArtworkComponent,
    canActivate: [artistGuard]
  },
  {
    path: 'dashboard/veranstaltungen-verwalten',
    component: ManageEventsComponent,
    canActivate: [artistGuard]
  },
  {
    path: 'dashboard/veranstaltung-bearbeiten/:id',
    component: AddEditEventComponent,
    canActivate: [artistGuard]
  },
  {
    path: 'dashboard/veranstaltung-erstellen',
    component: AddEditEventComponent,
    canActivate: [artistGuard]
  },
  {
    path: 'dashboard/ausstellungen-verwalten',
    component: ManageExhibitionsComponent,
    canActivate: [artistGuard]
  },
  {
    path: 'dashboard/ausstellung-bearbeiten/:id',
    component: AddEditExhibitionComponent,
    canActivate: [artistGuard]
  },
  {
    path: 'dashboard/ausstellung-erstellen',
    component: AddEditExhibitionComponent,
    canActivate: [artistGuard]
  },
  {
    path: 'dashboard/kunstwerk-anfragen-verwalten',
    component: ManageArtworkRequestComponent,
    canActivate: [artistGuard]
  },
];

