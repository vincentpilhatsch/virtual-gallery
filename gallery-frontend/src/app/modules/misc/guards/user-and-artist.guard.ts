import {ActivatedRouteSnapshot, CanActivateFn, Router, RouterStateSnapshot} from "@angular/router";
import {inject} from "@angular/core";
import {UserService} from "../../user/services/user.service";

export const userAndArtistGuard: CanActivateFn = (route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean => {
  const userService: UserService = inject(UserService); // TODO: Wenn ich Page neulade, dann ist User noch nicht geladen
  const router: Router = inject(Router);
  if (userService.user?.role) {
    return true;
  }
  void router.navigate(["anmelden"]);
  return false;
}
