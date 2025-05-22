import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { AuthService } from './service/auth.service';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
    const authService = inject(AuthService);

    // On s'assure que le cookie est envoyé avec chaque requête
    const cloned = req.clone({
        withCredentials: true  // Envoie automatiquement les cookies avec la requête
    });

    return next(cloned);  // Passe la requête sans ajouter d'en-têtes supplémentaires
};