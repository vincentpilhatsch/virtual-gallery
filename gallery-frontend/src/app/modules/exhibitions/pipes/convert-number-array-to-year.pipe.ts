import {Pipe, PipeTransform} from '@angular/core';

@Pipe({
  name: 'convertNumberArrayToYearString',
  standalone: true
})
export class ConvertNumberArrayToYearStringPipe implements PipeTransform {
  transform(numberArray: number[]): number {
    if (!Array.isArray(numberArray)) {
      return -1;
    }
    const [year, month, day] = numberArray;
    const date = new Date(year, month - 1, day);
    return date.getFullYear();
  }
}
