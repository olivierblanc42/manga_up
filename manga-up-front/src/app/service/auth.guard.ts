import { inject, Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, CanActivateFn, GuardResult, MaybeAsync, Router, RouterStateSnapshot, UrlTree } from '@angular/router';
import { AuthService } from './auth.service';
import { map, Observable } from 'rxjs';

@Injectable({
    providedIn: 'root',
})


export class AuthGuard implements CanActivate {

    constructor(private authService: AuthService, private router: Router) {

    }
    
    canActivate(
        route: ActivatedRouteSnapshot,
        state: RouterStateSnapshot
    ): Observable<boolean | UrlTree> {
        return this.authService.isLoggedIn().pipe(
            map((loggedIn: boolean) => {
                if (!loggedIn) {
                
                    return this.router.createUrlTree(['/forbidden']);
                }
                return true;
            })
        );
    }

}


