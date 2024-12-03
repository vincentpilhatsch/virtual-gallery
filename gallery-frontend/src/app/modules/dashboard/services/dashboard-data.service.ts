import {Injectable} from '@angular/core';
import {ManageModesEnum} from "../models/manage-modes.enum";
import {Router} from "@angular/router";

@Injectable({
  providedIn: 'root'
})
export class DashboardDataService {

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
