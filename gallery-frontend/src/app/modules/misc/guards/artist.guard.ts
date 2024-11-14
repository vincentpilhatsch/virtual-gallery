import {ActivatedRouteSnapshot, CanActivateFn, Router, RouterStateSnapshot} from "@angular/router";
import {inject} from "@angular/core";
import {UserService} from "../../user/services/user.service";
import {RoleEnum} from "../../user/model/role.enum";
import {SnackBarService} from "../services/snack-bar.service";

export const artistGuard: CanActivateFn = (route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean => {
  const userService: UserService = inject(UserService);
  const router: Router = inject(Router);
  if (userService.user?.role === RoleEnum.ARTIST) {
    return true;
  }
  // void router.navigate(["anmelden"]);
  // openSnackBarByRouteState(state)
  return false;
}

function openSnackBarByRouteState(state: RouterStateSnapshot): void {
  const snackBarService: SnackBarService = inject(SnackBarService);
  const message: string = snackBarService.getMessage(state)
  snackBarService.openSnackBar("error", message);
}
