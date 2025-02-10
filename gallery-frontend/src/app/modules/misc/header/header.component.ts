import {Component, OnInit, ViewEncapsulation} from '@angular/core';
import {MatIcon} from "@angular/material/icon";
import {RouterLink, RouterLinkActive} from "@angular/router";
import {MatFormField, MatLabel} from "@angular/material/form-field";
import {MatMenu, MatMenuModule} from "@angular/material/menu";
import {MatButtonModule} from "@angular/material/button";
import {MatInput} from "@angular/material/input";
import {MatOption, MatSelect, MatSelectChange} from "@angular/material/select";
import {LanguageEnum} from "../../user/model/language.enum";
import {UserService} from "../../user/services/user.service";
import {FormsModule} from "@angular/forms";
import {LanguageService} from "../../user/services/language.service";
import {HttpErrorResponse} from "@angular/common/http";
import {LanguageStringTranslatorPipe} from "../pipes/language-string-translator.pipe";
import {TranslateModule} from "@ngx-translate/core";
import {ToUpperCasePipe} from "../pipes/to-upper-case.pipe";
import {RoleEnum} from "../../user/model/role.enum";

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [
    MatIcon,
    RouterLink,
    MatMenu,
    MatButtonModule,
    MatMenuModule,
    MatFormField,
    MatInput,
    RouterLinkActive,
    MatLabel,
    MatSelect,
    MatOption,
    FormsModule,
    LanguageStringTranslatorPipe,
    TranslateModule,
    ToUpperCasePipe
  ],
  templateUrl: './header.component.html',
  styleUrl: './header.component.scss',
  encapsulation: ViewEncapsulation.None
})
export class HeaderComponent implements OnInit {
  readonly TOUR_URL: string = "/assets/360-degree-tour/index.html";
  readonly languageArray: string[] = Object.values(LanguageEnum);
  selectedLanguage: LanguageEnum = LanguageEnum.DE;


  constructor(protected userService: UserService,
              private readonly languageService: LanguageService) {
  }

  ngOnInit(): void {
    this.subscribeToUser();
  }

  private subscribeToUser(): void {
    this.userService.user$.subscribe((user) => {
      if (user) {
        this.selectedLanguage = user.language;
      } else {
        this.setLanguageByLocalStorage();
      }
    });
  }

  onTourClicked(): void {
    window.open(this.TOUR_URL, '_blank', 'noopener,noreferrer');
  }

  onSelectChange($event: MatSelectChange): void {
    const language: LanguageEnum = $event.value;
    this.selectedLanguage = language;
    this.languageService.setLanguageInLocalStorage(language);
    this.languageService.setLanguageInTranslateService(language);
    this.languageService.updateLanguageForUser(language)?.subscribe({
      error: (err: HttpErrorResponse): void => {
        console.error(err);
      }
    });
  }

  private setLanguageByLocalStorage() {
    const localStorageLanguage: LanguageEnum | null = this.languageService.getLanguageFromLocalStorage();
    if (localStorageLanguage) {
      this.selectedLanguage = localStorageLanguage;
    }
  }

  protected readonly RoleEnum = RoleEnum;
}
