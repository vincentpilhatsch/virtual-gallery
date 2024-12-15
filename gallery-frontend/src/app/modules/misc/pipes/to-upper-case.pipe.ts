import {Pipe, PipeTransform} from '@angular/core';

@Pipe({
  name: 'toUpperCase',
  standalone: true
})
export class ToUpperCasePipe implements PipeTransform {

  transform(value: string): string {
    if (!value) {
      return "";
    }
    return value.toUpperCase();
  }

}
