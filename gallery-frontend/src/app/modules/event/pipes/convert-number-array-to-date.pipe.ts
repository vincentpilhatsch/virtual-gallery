import {Pipe, PipeTransform} from '@angular/core';

@Pipe({
  name: 'convertNumberArrayToDateString',
  standalone: true
})
export class ConvertNumberArrayToDateStringPipe implements PipeTransform {

  transform(numberArray: number[]): string {
    const [year, month, day, hour, minute] = numberArray;
    const date = new Date(year, month - 1, day, hour, minute); // month is 0-indexed
    return date.toLocaleDateString("de-DE");
  }

}
