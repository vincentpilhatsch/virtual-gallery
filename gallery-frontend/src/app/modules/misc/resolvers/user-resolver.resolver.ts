import {ResolveFn, Router} from '@angular/router';
import {catchError, of} from "rxjs";
import {User} from "../../user/model/user";
import {inject} from "@angular/core";
import {UserService} from "../../user/services/user.service";

export const userResolver: ResolveFn<User | null> = () => {
  const userService = inject(UserService);
  const router = inject(Router);

  return userService.getCurrentUser().pipe(
    catchError(() => {
      void router.navigate(['anmelden']); // Navigate to login if there's an error
      return of(null);
    })
  );
};

