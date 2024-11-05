import {ActivatedRouteSnapshot, CanActivateFn, Router, RouterStateSnapshot} from '@angular/router';
import {UserService} from "../../user/services/user.service";
import {inject} from "@angular/core";
import {SnackBarService} from "../services/snack-bar.service";

export const authGuard: CanActivateFn = (route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean => {
  const userService: UserService = inject(UserService);
  const router: Router = inject(Router);
  if (!userService.user) {
    // void router.navigate(["anmelden"]);
    console.log("can activate");
    return true;
  }
  // openSnackBarByRouteState(state)
  console.log("can NOT activate");
  return false;
}

function openSnackBarByRouteState(state: RouterStateSnapshot): void {
  const snackBarService: SnackBarService = inject(SnackBarService);
  const message: string = snackBarService.getMessage(state)
  snackBarService.openSnackBar("error", message);
}
