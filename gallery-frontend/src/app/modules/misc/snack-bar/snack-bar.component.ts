import {Component, Inject, ViewEncapsulation} from '@angular/core';
import {MatIcon} from "@angular/material/icon";
import {MAT_SNACK_BAR_DATA} from "@angular/material/snack-bar";

@Component({
  selector: 'app-snack-bar',
  standalone: true,
  imports: [
    MatIcon
  ],
  templateUrl: './snack-bar.component.html',
  styleUrl: './snack-bar.component.scss',
  encapsulation: ViewEncapsulation.None
})
export class SnackBarComponent {
  constructor(@Inject(MAT_SNACK_BAR_DATA) public data: { message: string; icon: string }) {
  }
}
