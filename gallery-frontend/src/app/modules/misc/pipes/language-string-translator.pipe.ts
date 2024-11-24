import {Pipe, PipeTransform} from '@angular/core';
import {TranslateService} from "@ngx-translate/core";

@Pipe({
  name: 'languageStringTranslator',
  standalone: true
})
export class LanguageStringTranslatorPipe implements PipeTransform {
  private readonly languageJsonMapper: { [key: string]: string } = {
    "de": "language.german",
    "en": "language.english"
    // "FR": "language.french",
    // "IT": "language.italian",
  };

  constructor(private readonly translate: TranslateService) {
  }

  transform(value: string): string {
    if (!value) {
      return "";
    }
    const language: string = this.translate.currentLang;
    const json: string = this.languageJsonMapper[language];
    console.log(this.translate.instant(json));
    return this.translate.instant(json);
  }

}
