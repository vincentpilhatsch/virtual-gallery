import {Injectable} from '@angular/core';
import {MatSnackBar, MatSnackBarHorizontalPosition, MatSnackBarVerticalPosition} from "@angular/material/snack-bar";
import {SnackBarComponent} from "../snack-bar/snack-bar.component";
import {RouterStateSnapshot} from "@angular/router";
import {TranslateService} from "@ngx-translate/core";

@Injectable({
  providedIn: 'root'
})
export class SnackBarService {
  private readonly horizontalPosition: MatSnackBarHorizontalPosition = 'right';
  private readonly verticalPosition: MatSnackBarVerticalPosition = 'top';
  private readonly urlMessageMap: { [key: string]: string } = {
    "kunstwerk-anfragen": "Bitte melde dich an, um ein Kunstwerk anzufragen",
    "profil": "Bitte melde dich an, um auf dein Profil zuzugreifen",
    "dashboard": "Bitte melde dich an, um das Dashboard anzuzeigen",
    "anmelden": "Du bist bereits angemeldet",
    "registrieren": "Du bist bereits angemeldet",
  };

  constructor(private readonly matSnackBar: MatSnackBar,
              private readonly translateService: TranslateService) {
  }

  openSnackBar(icon: string, message: string) {
    this.matSnackBar.openFromComponent(SnackBarComponent, {
      data: {icon, message},
      horizontalPosition: this.horizontalPosition,
      verticalPosition: this.verticalPosition,
      duration: 3500,
    });
  }
 
  openSnackBarByJSON(icon: string, json: string) {
    const message: string = this.translateService.instant(json);
    this.matSnackBar.openFromComponent(SnackBarComponent, {
      data: {icon, message},
      horizontalPosition: this.horizontalPosition,
      verticalPosition: this.verticalPosition,
      duration: 3500,
    });
  }

  getMessage(state: RouterStateSnapshot | undefined) {
    const url: string | undefined = state?.url;
    if (!url) return "";
    return this.getMessageByUrl(url);
  }

  getMessageByUrl(url: string): string {
    for (const key in this.urlMessageMap) {
      if (url.includes(key)) {
        return this.urlMessageMap[key];
      }
    }
    return "Ein Fehler ist aufgetreten";
  }
}
