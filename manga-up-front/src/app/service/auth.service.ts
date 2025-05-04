import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
    providedIn: 'root',
})
export class AuthService {
    private apiUrl = 'http://localhost:8080/api/auth';

    constructor(private http: HttpClient) { }

    login(credentials: { username: string; password: string }): Observable<any> {
        // Ajout de `withCredentials: true`
        return this.http.post(`${this.apiUrl}/login`, credentials, { withCredentials: true });  
    }

    logout(): void {
        // Appel HTTP vers l'endpoint de déconnexion du backend
        this.http.post('http://localhost:8080/api/auth/logout', {}, { withCredentials: true })
            .subscribe({
                next: () => {
                    console.log('Déconnecté avec succès.');
                    // Rediriger ou mettre à jour l'état du client si besoin
                },
           
            });
    }


    isAuthenticated(): boolean {
        // L'authentification dépend désormais des cookies, pas du localStorage
        // Si un cookie "jwt" est présent, l'utilisateur est authentifié
        return true;  // Le serveur gère l'authentification via cookies
    }
}
