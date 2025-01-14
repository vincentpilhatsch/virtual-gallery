import {ActivatedRouteSnapshot, CanActivateFn, Router, RouterStateSnapshot} from "@angular/router";
import {inject} from "@angular/core";
import {UserService} from "../../user/services/user.service";
import {catchError, Observable, of, switchMap} from "rxjs";

export const userAndArtistGuard: CanActivateFn = (route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean> => {
  const userService: UserService = inject(UserService);
  const router: Router = inject(Router);

  return userService.getCurrentUser().pipe(
    switchMap((user) => {
      if (user?.role) {
        return of(true);
      }
      void router.navigate(["anmelden"]);
      return of(false);
    }),
    catchError(() => {
      void router.navigate(["anmelden"]);
      return of(false);
    })
  );
}
