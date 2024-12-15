import {Pipe, PipeTransform} from '@angular/core';
import {TranslateService} from "@ngx-translate/core";
import {LanguageEnum} from "../../user/model/language.enum";

@Pipe({
  name: 'convertNumberArrayToDateString',
  standalone: true,
  pure: false // TODO: pipe gets called on every change detection, can lead to bad website performance
})
export class ConvertNumberArrayToDateStringPipe implements PipeTransform {
  private readonly GERMAN_TIME_TRANSLATION = "Uhr";
  private readonly INVALID_DATE_JSON = "error.invalid.date";

  constructor(private readonly translate: TranslateService) {
  }

  transform(numberArray: number[]): string {
    if (!Array.isArray(numberArray) || numberArray.length < 5) {
      return this.translate.instant(this.INVALID_DATE_JSON);
    }
    const [year, month, day, hour, minute] = numberArray;
    const dateObject = new Date(year, month - 1, day, hour, minute);
    return this.getDateString(dateObject);
  }

  private getDateString(dateObject: Date) {
    const language: string = this.translate.currentLang;
    const dayIfTranslatedInGerman: string = language === LanguageEnum.DE.toLowerCase() ? this.GERMAN_TIME_TRANSLATION : "";
    const day = new Intl.DateTimeFormat(language, {weekday: 'long'}).format(dateObject);
    const date: string = dateObject.toLocaleDateString(language);
    const time: string = dateObject.toLocaleTimeString(language, {hour: '2-digit', minute: '2-digit'});
    return `${day} ${date}, ${time} ${dayIfTranslatedInGerman}`;
  }
}
