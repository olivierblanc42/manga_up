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
        return this.http.post(`${this.apiUrl}/login`, credentials);
    }

    saveToken(token: string): void {
        localStorage.setItem('jwt_token', token);
    }

    getToken(): string | null {
        return localStorage.getItem('jwt_token');
    }

    logout(): void {
        localStorage.removeItem('jwt_token');
    }

    isAuthenticated(): boolean {
        const tokenExists = !!this.getToken();
        console.log('Utilisateur connecté :', tokenExists); 
        return tokenExists;

    }
}