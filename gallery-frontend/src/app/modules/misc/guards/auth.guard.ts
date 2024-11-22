import {ActivatedRouteSnapshot, CanActivateFn, RouterStateSnapshot} from '@angular/router';
import {UserService} from "../../user/services/user.service";
import {inject} from "@angular/core";

export const authGuard: CanActivateFn = (route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean => {
  const userService: UserService = inject(UserService);
  if (!userService.user) {
    console.log("can activate");
    return true;
  }
  console.log("can NOT activate");
  return false;
}
