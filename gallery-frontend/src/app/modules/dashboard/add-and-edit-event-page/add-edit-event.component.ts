import {Component, ViewEncapsulation} from '@angular/core';
import {MatFormField, MatLabel} from "@angular/material/form-field";
import {MatCheckbox} from "@angular/material/checkbox";
import {MatInput} from "@angular/material/input";
import {MatButton} from "@angular/material/button";

@Component({
  selector: 'app-add-edit-event',
  standalone: true,
  imports: [
    MatFormField,
    MatCheckbox,
    MatLabel,
    MatInput,
    MatButton
  ],
  templateUrl: './add-edit-event.component.html',
  styleUrl: './add-edit-event.component.scss',
  encapsulation: ViewEncapsulation.None
})
export class AddEditEventComponent {

}
