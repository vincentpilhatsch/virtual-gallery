import {Injectable} from '@angular/core';
import {Artwork} from "../../artwork/model/artwork";
import {ActivatedRoute, Router} from "@angular/router";
import {ManageModesEnum} from "../models/manage-modes.enum";
import {Exhibition} from "../../exhibitions/model/exhibition";
import {ArtworkRequest} from "../../artwork/model/artworkRequest";
import {ArtworkService} from "../../artwork/services/artwork.service";
import {HttpErrorResponse} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class DashboardDataService {
  // currentExhibition: Exhibition | undefined;
  // currentEvent: Event | undefined;
  // currentArtwork: Artwork | undefined;
  // currentArtworkRequest: ArtworkRequest | undefined;

  constructor(private readonly router: Router) {
  }

  getManageMode(): ManageModesEnum {
    const isAddingArtwork = this.router.url.includes('erstellen');
    if (isAddingArtwork) {
      return ManageModesEnum.ADD;
    }
    return ManageModesEnum.EDIT;
  }

}
