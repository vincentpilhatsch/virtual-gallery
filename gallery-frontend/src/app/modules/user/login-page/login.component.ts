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
import {Router, RouterLink} from "@angular/router";
import {ErrorTypeEnum} from "../model/errorType.enum";

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
    RouterLink,
  ],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss',
  encapsulation: ViewEncapsulation.None
})
export class LoginComponent implements OnInit {
  hide = signal(true);
  errorMessage: ErrorTypeEnum | null = null;
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
      const emailValidAndDirty: boolean | undefined =
        this.form.get("email")?.valid && this.form.get("email")?.dirty;
      const passwordValidAndDirty: boolean | undefined =
        this.form.get("password")?.valid && this.form.get("password")?.dirty;

      if (emailValidAndDirty && this.errorMessage === ErrorTypeEnum.EMAIL) {
        this.errorMessage = null;
      } else if (passwordValidAndDirty && this.errorMessage === ErrorTypeEnum.PASSWORD) {
        this.errorMessage = null;
      } else {
        this.handleErrorMessage();
      }
    });
  }

  onFocusOut() {
    this.handleErrorMessage();
  }

  private handleErrorMessage(): void {
    if (this.form.invalid) {
      this.errorMessage = null;
      const reversedControls = Object.values(this.form.controls).reverse();
      for (const controlName in reversedControls) {
        this.setMessageByErrorType(reversedControls[controlName]);
      }
    } else {
      this.errorMessage = null;
    }
  }

  /*
    Password must be at least 8 characters long, contain at least 1 uppercase letter,
    1 digit, and 1 special character (any non-alphanumeric character).
  */

  private setMessageByErrorType(control: AbstractControl): void {
    if (control.errors) {
      // if (control.errors['required']) {
      //   this.errorMessage = 'Bitte füllen Sie alle erforderlichen Felder aus.';
      // }
      if (control.errors['email']) {
        this.errorMessage = ErrorTypeEnum.EMAIL;
      }
      if (control.errors['pattern']) {
        this.errorMessage = ErrorTypeEnum.PASSWORD;
      }
    }
  }

  onFormSubmitted(): void {
    const formEmail: string = this.form.get("email")?.value;
    const formPassword: string = this.form.get("password")?.value;
    if (this.form.valid && formEmail && formPassword) {
      const user: LoginForm = {
        email: formEmail,
        password: formPassword
      }
      this.loginUser(user);
    }
  }

  loginUser(loginForm: LoginForm): void {
    this.userService.login(loginForm).subscribe({
      next: (res: AuthResponse): void => {
        this.userService.user = res.user
        this.languageService.setLanguageByLoggedInUser(res.user.language);
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
