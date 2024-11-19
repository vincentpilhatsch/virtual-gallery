import {HttpInterceptorFn, HttpRequest} from '@angular/common/http';
import {catchError, throwError} from "rxjs";
import {inject} from "@angular/core";
import {Router} from "@angular/router";
import {SnackBarService} from "../../misc/services/snack-bar.service";
import {UserService} from "../services/user.service";

const excludedUrls: string[] = ["assets/i18n"]; // "user/login"

export const jwtInterceptor: HttpInterceptorFn = (req, next) => {
  const router: Router = inject(Router);
  const userService: UserService = inject(UserService);
  const snackBarService: SnackBarService = inject(SnackBarService);

  const shouldExclude: boolean = excludedUrls.some(url => req.url.includes(url));
  const requestToSend: HttpRequest<unknown> = shouldExclude ? req : req.clone({withCredentials: true});

  return next(requestToSend).pipe(
    catchError((error) => {
      if (error.status === 403) {
        userService.user = null;
        void router.navigate(['/anmelden']);
        snackBarService.openSnackBarByJSON("error", "snackbar.login.expired");
      }
      return throwError(() => error);
    })
  );
};
