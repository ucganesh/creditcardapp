import { Injectable } from '@angular/core';
import { Router, CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';

import { AuthenticationService } from '@app/_services';

@Injectable({ providedIn: 'root' })
export class AuthGuard implements CanActivate {
    constructor(
        private router: Router,
        private authenticationService: AuthenticationService
    ) { }

    canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const identity = this.authenticationService.identityValue;
        if (identity) {
            // check if route is restricted by role
            if (route.data.roles) {
              for (let role of identity.roles) {
                if (route.data.roles.includes(role)) {
                    return true;
                }
              }
              // role not authorised so redirect to home page
              this.router.navigate(['/']);
              return false;
            }
            // authorised so return true
            return true;
        }

        // not logged in so redirect to login page with the return url
        this.router.navigate(['/login'], { queryParams: { returnUrl: state.url } });
        return false;
    }
}
