import {Component, OnDestroy, OnInit, signal, ViewEncapsulation} from '@angular/core';
import {MatButton, MatIconButton} from "@angular/material/button";
import {MatFormField, MatLabel, MatSuffix} from "@angular/material/form-field";
import {MatIcon} from "@angular/material/icon";
import {MatInput} from "@angular/material/input";
import {NgOptimizedImage} from "@angular/common";
import {ErrorTypeEnum} from "../model/errorType.enum";
import {AbstractControl, FormBuilder, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {UserService} from "../services/user.service";
import {LanguageService} from "../services/language.service";
import {Router} from "@angular/router";
import {AuthResponse} from "../model/authResponse";
import {RegisterRequest} from "../model/registerRequest";
import {Subscription} from "rxjs";

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [
    MatButton,
    MatFormField,
    MatIcon,
    MatIconButton,
    MatInput,
    MatLabel,
    MatSuffix,
    NgOptimizedImage,
    ReactiveFormsModule
  ],
  templateUrl: './register.component.html',
  styleUrl: './register.component.scss',
  encapsulation: ViewEncapsulation.None
})
export class RegisterComponent implements OnInit, OnDestroy {
  hide = signal(true);

  disableLoginButton: boolean = true; // TODO: Set false for Live
  errorMessage: ErrorTypeEnum | null = null;
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

  subscriptions: Subscription[] = [];

  constructor(private fb: FormBuilder,
              private userService: UserService,
              private languageService: LanguageService,
              private router: Router) {
  }

  ngOnInit(): void {
    this.subscribeToFormValueChanges();
  }

  private subscribeToFormValueChanges(): void {
    this.subscriptions.push(
      this.form.valueChanges.subscribe(() => {
        if (this.form.valid) {
          this.errorMessage = null;
        } else {
          this.handleErrorMessage();
        }
        this.disableLoginButton = !this.checkIfFormsAreValid();
      })
    );
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
      if (control.errors['firstName']) {
        this.errorMessage = ErrorTypeEnum.FIRST_NAME;
      }
      if (control.errors['lastName']) {
        this.errorMessage = ErrorTypeEnum.LAST_NAME;
      }
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
    const formFirstName: string = this.form.get("firstName")?.value;
    const formLastName: string = this.form.get("lastName")?.value;
    const formEmail: string = this.form.get("email")?.value;
    const formPassword: string = this.form.get("password")?.value;

    const registerRequest: RegisterRequest = {
      firstName: formFirstName,
      lastName: formLastName,
      email: formEmail,
      password: formPassword
    }
    this.registerUser(registerRequest);
    // }
  }

  private checkIfFormsAreValid(): boolean {
    const formEmail: string = this.form.get("email")?.value;
    const formPassword: string = this.form.get("password")?.value;
    const formIsValid: boolean = this.form.valid;
    return formIsValid && !!formEmail && !!formPassword;
  }

  registerUser(registerRequest: RegisterRequest): void {
    this.userService.register(registerRequest).subscribe({
      next: (res: AuthResponse): void => {
        this.userService.user = res.user
        this.languageService.setLanguageByLoggedInUser(res.user.language);
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
