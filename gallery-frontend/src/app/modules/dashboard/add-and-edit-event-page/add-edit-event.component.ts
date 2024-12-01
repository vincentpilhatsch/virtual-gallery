import {Component, OnInit, ViewEncapsulation} from '@angular/core';
import {MatFormField, MatLabel} from "@angular/material/form-field";
import {MatCheckbox} from "@angular/material/checkbox";
import {MatInput} from "@angular/material/input";
import {MatButton} from "@angular/material/button";
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";

@Component({
  selector: 'app-add-edit-event',
  standalone: true,
  imports: [
    MatFormField,
    MatCheckbox,
    MatLabel,
    MatInput,
    MatButton,
    ReactiveFormsModule
  ],
  templateUrl: './add-edit-event.component.html',
  styleUrl: './add-edit-event.component.scss',
  encapsulation: ViewEncapsulation.None
})
export class AddEditEventComponent implements OnInit {
  form: FormGroup = this.fb.group({
    firstName: ['', [
      Validators.required,
    ]],
    lastName: ['', [
      Validators.required,
    ]],
    email: ['', [
      Validators.required,
      Validators.email
    ]],
    password: ['', [
      Validators.required,
      Validators.pattern("^(?=.*[A-Z])(?=.*[0-9])(?=.*[^a-zA-Z0-9]).{8,}$")
    ]],
  });

  constructor(private readonly fb: FormBuilder) {
  }

  ngOnInit() {
  }

}
