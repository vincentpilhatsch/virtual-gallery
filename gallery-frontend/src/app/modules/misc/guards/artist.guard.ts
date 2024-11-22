import {ActivatedRouteSnapshot, CanActivateFn, Router, RouterStateSnapshot} from "@angular/router";
import {inject} from "@angular/core";
import {UserService} from "../../user/services/user.service";
import {RoleEnum} from "../../user/model/role.enum";

export const artistGuard: CanActivateFn = (route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean => {
  const userService: UserService = inject(UserService);
  const router: Router = inject(Router);
  if (userService.user?.role === RoleEnum.ARTIST) {
    return true;
  }
  void router.navigate(["anmelden"]);
  return false;
}

