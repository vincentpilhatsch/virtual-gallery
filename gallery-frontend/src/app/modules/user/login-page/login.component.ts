import {Component, OnDestroy, OnInit, signal, ViewEncapsulation} from '@angular/core';
import {MatFormField, MatFormFieldModule, MatLabel} from "@angular/material/form-field";
import {MatInput, MatInputModule} from "@angular/material/input";
import {NgOptimizedImage} from "@angular/common";
import {MatIcon} from "@angular/material/icon";
import {MatButtonModule} from "@angular/material/button";
import {AbstractControl, FormBuilder, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {UserService} from "../services/user.service";
import {LoginRequest} from "../model/loginRequest";
import {AuthService} from "../services/auth.service";
import {AuthResponse} from "../model/authResponse";
import {LanguageService} from "../services/language.service";
import {Router, RouterLink} from "@angular/router";
import {ErrorTypeEnum} from "../model/errorType.enum";
import {Subscription} from "rxjs";

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
export class LoginComponent implements OnInit, OnDestroy {
  hide = signal(true);

  errorMessage: ErrorTypeEnum | null = null;
  disableLoginButton: boolean = true; // TODO: Set false for Live
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

  subscriptions: Subscription[] = [];

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
      if (this.form.valid) {
        this.errorMessage = null;
      } else {
        this.handleErrorMessage();
      }
      this.disableLoginButton = !this.checkIfFormsAreValid();
    });
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
      if (control.errors['email']) {
        this.errorMessage = ErrorTypeEnum.EMAIL;
      }
      if (control.errors['pattern']) {
        this.errorMessage = ErrorTypeEnum.PASSWORD;
      }
    }
  }

  onFormSubmitted(): void {
    // if (this.checkIfFormsAreValid()) { // TODO: Comment in for Live
    const formEmail: string = this.form.get("email")?.value;
    const formPassword: string = this.form.get("password")?.value;
    const user: LoginRequest = {
      email: formEmail,
      password: formPassword
    }
    this.loginUser(user);
    // }
  }

  private checkIfFormsAreValid(): boolean {
    const formEmail: string = this.form.get("email")?.value;
    const formPassword: string = this.form.get("password")?.value;
    const formIsValid: boolean = this.form.valid;
    return formIsValid && !!formEmail && !!formPassword;
  }

  loginUser(loginForm: LoginRequest): void {
    this.userService.login(loginForm).subscribe({
      next: (res: AuthResponse): void => {
        this.userService.user = res.user
        this.languageService.setLanguageByLoggedInUser(res.user.language);
        this.authService.setToken(res.jwt)
        void this.router.navigate(["profil"]);
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

  ngOnDestroy(): void {
    this.subscriptions.forEach(sub => {
      if (sub) {
        sub.unsubscribe();
      }
    })
  }
}
