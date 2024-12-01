import {Injectable} from '@angular/core';
import {Artwork} from "../../artwork/model/artwork";

@Injectable({
  providedIn: 'root'
})
export class DashboardCommunicationService {
  artwork: Artwork | undefined;
}
