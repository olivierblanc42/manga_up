import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { catchError, map, Observable, of } from 'rxjs';

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


    isLoggedIn(): Observable<boolean> {
        return this.http.get('/api/auth/check', { withCredentials: true }).pipe(
            map(() => true),
            catchError(() => of(false))
        );
    }
}
