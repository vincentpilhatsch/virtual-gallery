import {Component, OnInit, signal, ViewEncapsulation} from '@angular/core';
import {MatFormField, MatFormFieldModule, MatLabel} from "@angular/material/form-field";
import {MatInput, MatInputModule} from "@angular/material/input";
import {NgOptimizedImage} from "@angular/common";
import {MatIcon} from "@angular/material/icon";
import {MatButtonModule} from "@angular/material/button";
import {AbstractControl, FormBuilder, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {UserService} from "../services/user.service";
import {LoginForm} from "../model/loginForm";
import {AuthService} from "../services/auth.service";
import {AuthResponse} from "../model/authResponse";
import {LanguageService} from "../services/language.service";
import {Router} from "@angular/router";

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
    MatIcon,
    ReactiveFormsModule,
  ],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss',
  encapsulation: ViewEncapsulation.None
})
export class LoginComponent implements OnInit {
  hide = signal(true);
  errorMessage: string | null = null;
  form: FormGroup = this.fb.group({
    email: ['', [
      Validators.required,
      Validators.email
    ]],
    password: ['', [
      Validators.required,
      Validators.pattern("^(?=.*[A-Z])(?=.*[0-9])(?=.*[^a-zA-Z0-9]).{8,}$")
    ]],
  });

  constructor(private fb: FormBuilder,
              private userService: UserService,
              private authService: AuthService,
              private languageService: LanguageService,
              private router: Router) {
  }

  ngOnInit(): void {
    this.subscribeToFormValueChanges();
  }

  private subscribeToFormValueChanges(): void {
    this.form.valueChanges.subscribe(() => {
      if (this.form.dirty && this.form.invalid) {
        this.handleErrorMessage();
      } else {
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

  /*
    Password must be at least 8 characters long, contain at least 1 uppercase letter,
    1 digit, and 1 special character (any non-alphanumeric character).
  */

  private setMessageByErrorType(control: AbstractControl): void {
    if (control.errors) {
      if (control.errors['required']) {
        this.errorMessage = 'Bitte füllen Sie alle erforderlichen Felder aus.';
      }
      if (control.errors['email']) {
        this.errorMessage = 'Bitte geben Sie eine gültige E-Mail-Adresse ein.';
      }
      if (control.errors['password']) {
        this.errorMessage =
          'Bitte geben Sie eine gültige Passwort an (mind. 8 Zeichen, 1 Großbuchstabe, 1 Zahl, 1 Sonderzeichen';
      }
    }
  }

  onFormSubmitted(): void {
    const formEmail: string = this.form.get("email")?.value;
    const formPassword: string = this.form.get("password")?.value;
    // if (this.form.valid && formEmail && formPassword) {
    const user: LoginForm = {
      email: formEmail,
      password: formPassword
    }
    this.loginUser(user);
    // }
  }

  loginUser(loginForm: LoginForm): void {
    this.userService.login(loginForm).subscribe({
      next: (res: AuthResponse): void => {
        this.userService.user = res.user
        this.languageService.setLanguageByLoginUser(res.user.language);
        this.authService.setToken(res.jwt)
        void this.router.navigate(["home"]);
      },
      error: (err): void => {
        console.error(err);
      }
    });
  }

  showOrHidePassword(event: MouseEvent) {
    this.hide.set(!this.hide());
    event.stopPropagation();
  }
}
