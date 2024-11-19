import {Component, OnDestroy, OnInit, signal, ViewEncapsulation} from '@angular/core';
import {MatFormField, MatFormFieldModule, MatLabel} from "@angular/material/form-field";
import {MatInput, MatInputModule} from "@angular/material/input";
import {NgOptimizedImage} from "@angular/common";
import {MatIcon} from "@angular/material/icon";
import {MatButtonModule} from "@angular/material/button";
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {UserService} from "../services/user.service";
import {LoginRequest} from "../model/loginRequest";
import {AuthResponse} from "../model/authResponse";
import {LanguageService} from "../services/language.service";
import {Router, RouterLink} from "@angular/router";
import {Subscription} from "rxjs";
import {HttpErrorResponse} from "@angular/common/http";
import {ErrorMessageHandlerPipe} from "../pipe/error-message-handler.pipe";
import {TranslateModule} from "@ngx-translate/core";

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
    ErrorMessageHandlerPipe,
    TranslateModule,
  ],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss',
  encapsulation: ViewEncapsulation.None
})
export class LoginComponent implements OnInit, OnDestroy {
  hide = signal(true);

  backendErrorMessage: HttpErrorResponse | null = null;
  disableLoginButton: boolean = true; // TODO: Set false for Live
  form: FormGroup = this.fb.group({
    email: ['', [
      Validators.required,
    ]],
    password: ['', [
      Validators.required,
    ]],
  });

  subscriptions: Subscription[] = [];

  constructor(private readonly fb: FormBuilder,
              private readonly userService: UserService,
              private readonly languageService: LanguageService,
              private readonly router: Router) {
  }

  ngOnInit(): void {
    this.subscribeToFormValueChanges();
  }

  private subscribeToFormValueChanges(): void {
    this.subscriptions.push(
      this.form.valueChanges.subscribe(() => {
        this.disableLoginButton = !this.checkIfFormsAreValid();
      })
    );
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
        void this.router.navigate(["profil"]);
      },
      error: (err: HttpErrorResponse): void => {
        this.backendErrorMessage = err;
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
