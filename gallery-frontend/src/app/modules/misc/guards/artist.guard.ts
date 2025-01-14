import {ActivatedRouteSnapshot, CanActivateFn, Router, RouterStateSnapshot} from "@angular/router";
import {inject} from "@angular/core";
import {UserService} from "../../user/services/user.service";
import {RoleEnum} from "../../user/model/role.enum";
import {catchError, Observable, of, switchMap} from "rxjs";

export const artistGuard: CanActivateFn = (route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean> => {
  const userService: UserService = inject(UserService);
  const router: Router = inject(Router);

  return userService.getCurrentUser().pipe(
    switchMap((user) => {
      if (user?.role === RoleEnum.ARTIST) {
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

