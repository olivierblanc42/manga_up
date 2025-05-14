import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, Router, UrlTree } from '@angular/router';
import { Observable, of } from 'rxjs';
import { AuthService } from './auth.service';

@Injectable({
    providedIn: 'root'
})
export class RoleGuard implements CanActivate {

    constructor(private authService: AuthService, private router: Router) { }

    canActivate(
        route: ActivatedRouteSnapshot
    ): Observable<boolean | UrlTree> {
        const expectedRole = route.data['role'];
        const userRole = this.authService.getUserRole();

        if (userRole === expectedRole) {
            return of(true);
        } else {
            return of(this.router.createUrlTree(['/login'])); // ou une autre route d'erreur/accès refusé
        }
    }
}
