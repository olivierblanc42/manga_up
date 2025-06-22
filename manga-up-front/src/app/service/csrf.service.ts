import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject, firstValueFrom } from 'rxjs';
import { Csrf } from '../type';
import { environment } from '../../environments/environment.prod';

@Injectable({
    providedIn: 'root'
})
export class CsrfService {
    private apiUrl = `${environment.apiUrl}/api/csrf`;

    csrfProjections = new BehaviorSubject<Csrf | null>(null);
    currentCsrfProjection = this.csrfProjections.asObservable();

    constructor(private http: HttpClient) { }

    async fetchCsrfToken(): Promise<void> {
        try {
            const response = await firstValueFrom(this.http.get<Csrf>(this.apiUrl));
            if (!response) return;
            this.csrfProjections.next(response);
        } catch (error) {
            console.error('Erreur lors de la récupération du token CSRF:', error);
        }
    }

    getToken(): string | null {
        const csrf = this.csrfProjections.value;
        return csrf ? csrf.csrfToken : null;
    }
      


}
