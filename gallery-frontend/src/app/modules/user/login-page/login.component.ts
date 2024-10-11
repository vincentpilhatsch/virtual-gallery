import {Component, signal, ViewEncapsulation} from '@angular/core';
import {MatFormField, MatFormFieldModule, MatLabel} from "@angular/material/form-field";
import {MatInput, MatInputModule} from "@angular/material/input";
import {NgOptimizedImage} from "@angular/common";
import {MatIcon} from "@angular/material/icon";
import {MatButtonModule} from "@angular/material/button";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {UserService} from "../services/user.service";
import {User} from "../model/user";
import {PersonalDataForm} from "../model/personalDataForm";

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    MatFormField,
    MatInput,
    MatLabel,
    NgOptimizedImage,
    MatButtonModule,
    MatInputModule,
    MatFormFieldModule,
    MatIcon
  ],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss',
  encapsulation: ViewEncapsulation.None
})
export class LoginComponent {
  hide = signal(true);
  errorMessage: string | null = null;
  form: FormGroup = this.fb.group({
    email: ['', [Validators.required, Validators.email]],
    password: ['', [
      Validators.required,
      Validators.minLength(2),
      Validators.maxLength(30),
      Validators.pattern()
    ]],
  });

  constructor(private fb: FormBuilder, private userService: UserService) {
  }

  ngOnInit(): void {
    this.initForm();
  }

  private initForm(): void {
    const user: User | undefined = this.userService.user;
    if (user) {
      const existingUser: PersonalDataForm = {
        email: user?.email || '',
        : user?. ?? ''
      }
      this.form.setValue(existingUser);
    }
  }

  showOrHidePassword(event: MouseEvent) {
    this.hide.set(!this.hide());
    event.stopPropagation();
  }
}
