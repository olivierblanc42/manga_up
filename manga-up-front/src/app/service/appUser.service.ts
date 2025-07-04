import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { BehaviorSubject, firstValueFrom, Observable } from 'rxjs';
import { UserTest } from '../type';
import { environment } from '../../environments/environment.prod';

@Injectable({
    providedIn: 'root',
})
export class AppUserService {

    private apiUrl = `${environment.apiUrl}api/users/me`;
    

    options = {
        headers: new HttpHeaders({
            "Content-Type": "application/json",
        }),
        body: {
            id: '',
        },
    };
    constructor(
        private http: HttpClient
    ) { }


    userMe = new BehaviorSubject<UserTest | null>(null);
    currentUserMe = this.userMe.asObservable();

    async loadCurrentUser() {
        try {
            const user = await firstValueFrom(this.http.get<UserTest>(this.apiUrl));
            if (user) {
                console.log('Utilisateur chargé :', user);
                this.userMe.next(user);
            }
        } catch (error) {
            console.error('Erreur lors du chargement de l\'utilisateur :', error);
        }
    }

}


