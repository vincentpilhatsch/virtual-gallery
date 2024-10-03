import {Component, OnInit, ViewEncapsulation} from '@angular/core';
import {MatButton} from "@angular/material/button";
import {MatFormField, MatLabel} from "@angular/material/form-field";
import {MatInput} from "@angular/material/input";
import {AbstractControl, FormBuilder, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {UserService} from "../../services/user.service";
import {User} from "../../model/user";
import {PersonalDataForm} from "../../model/personalDataForm";

@Component({
  selector: 'app-personal-data',
  standalone: true,
  imports: [
    MatButton,
    MatFormField,
    MatInput,
    MatLabel,
    ReactiveFormsModule
  ],
  templateUrl: './personal-data.component.html',
  styleUrl: './personal-data.component.scss',
  encapsulation: ViewEncapsulation.None
})
export class PersonalDataComponent implements OnInit {
  showSubmitButton: boolean = false;
  errorMessage: string | null = null;
  form: FormGroup = this.fb.group({
    firstName: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(30)]],
    lastName: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(50)]],
    email: ['', [Validators.required, Validators.email]],
    phone: [''],
    city: ['']
  });

  constructor(private fb: FormBuilder, private userService: UserService) {
  }

  ngOnInit(): void {
    this.initForm();
    this.subscribeToFormValueChanges();
  }

  private initForm(): void {
    const user: User | undefined = this.userService.user;
    if (user) {
      const existingUser: PersonalDataForm = {
        firstName: user?.firstName || '',
        lastName: user?.lastName || '',
        email: user?.email || '',
        phone: user?.phone ?? '',
        city: user?.city ?? ''
      }
      this.form.setValue(existingUser);
    }
  }

  private subscribeToFormValueChanges(): void {
    this.form.valueChanges.subscribe(() => {
      if (this.form.dirty && this.form.invalid) {
        this.showSubmitButton = false;
        this.handleErrorMessage();
      } else {
        this.showSubmitButton = true;
        this.errorMessage = null;
      }
    });
  }

  private handleErrorMessage(): void {
    this.errorMessage = null;
    const controls = this.form.controls;

    for (const controlName in controls) {
      this.setMessageByErrorType(controls[controlName]);
    }
  }

  private setMessageByErrorType(control: AbstractControl): void {
    if (control.errors) {
      if (control.errors['required']) {
        this.errorMessage = 'Bitte füllen Sie alle erforderlichen Felder aus.';
      }
      if (control.errors['email']) {
        this.errorMessage = 'Bitte geben Sie eine gültige E-Mail-Adresse ein.';
      }
      if (control.errors['minlength']) {
        this.errorMessage = `Das Feld muss mindestens ${control.errors['minlength'].requiredLength} Zeichen lang sein.`;
      }
      if (control.errors['maxlength']) {
        this.errorMessage = `Das Feld darf maximal ${control.errors['maxlength'].requiredLength} Zeichen lang sein.`;
      }
    }
  }

  onFormSubmitted(): void {
    this.showSubmitButton = false;
    this.updateUserServiceValues();
    this.updateUser();
  }

  private updateUserServiceValues() {
    if (this.form.valid && this.userService.user) {
      const personalData: PersonalDataForm = this.form.value;
      this.userService.user.firstName = personalData.firstName;
      this.userService.user.lastName = personalData.lastName;
      this.userService.user.email = personalData.email;
      this.userService.user.phone = personalData.phone;
      this.userService.user.city = personalData.city;
    }
  }

  private updateUser() {
    const user: User | undefined = this.userService.user;
    if (user) {
      this.userService.updateUser(user).subscribe({
        next: (res): void => {
          console.log(res);
        },
        error: (err): void => {
          console.error(err);
        }
      })
    }
  }
}
