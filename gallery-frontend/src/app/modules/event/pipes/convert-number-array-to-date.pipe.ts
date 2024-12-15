import {Pipe, PipeTransform} from '@angular/core';
import {TranslateService} from "@ngx-translate/core";

@Pipe({
  name: 'convertNumberArrayToDateString',
  standalone: true,
  pure: false // TODO: pipe gets called on every change detection, can lead to bad website performance
})
export class ConvertNumberArrayToDateStringPipe implements PipeTransform {
  private readonly INVALID_DATE_JSON = "error.invalid.date";

  constructor(private readonly translate: TranslateService) {
  }

  transform(numberArray: number[]): string {
    if (!Array.isArray(numberArray) || numberArray.length < 3) {
      return this.translate.instant(this.INVALID_DATE_JSON);
    }
    const [year, month, day] = numberArray;
    const dateObject = new Date(year, month - 1, day);
    return this.getDateString(dateObject);
  }

  private getDateString(dateObject: Date) {
    const language: string = this.translate.currentLang;
    return dateObject.toLocaleDateString(language);
  }
}
