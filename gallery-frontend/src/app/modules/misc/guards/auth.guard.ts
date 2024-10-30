import {ActivatedRouteSnapshot, CanActivateFn, Router, RouterStateSnapshot} from '@angular/router';
import {UserService} from "../../user/services/user.service";
import {inject} from "@angular/core";
import {Role} from "../../user/model/role";
import {SnackBarService} from "../services/snack-bar.service";

export const authGuard: CanActivateFn = (route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean => {
  const userService: UserService = inject(UserService);
  if (!userService.user?.role) {
    return true;
  }
  openSnackBarByRouteState(state)
  return false;
}

function openSnackBarByRouteState(state: RouterStateSnapshot): void {
  const snackBarService: SnackBarService = inject(SnackBarService);
  const message: string = snackBarService.getMessage(state)
  snackBarService.openSnackBar("error", message);
}
