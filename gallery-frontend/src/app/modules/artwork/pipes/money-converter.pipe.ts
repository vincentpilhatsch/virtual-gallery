import {Pipe, PipeTransform} from '@angular/core';

@Pipe({
  name: 'moneyConverter',
  standalone: true
})
export class MoneyConverterPipe implements PipeTransform {

  transform(value: number): string {
    const currency: string = 'EUR';
    const locale: string = 'de-DE'
    
    return new Intl.NumberFormat(locale, {
      style: 'currency',
      currency
    }).format(value);
  }

}
