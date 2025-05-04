import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { BehaviorSubject, lastValueFrom, Observable } from 'rxjs';
import { UserTest } from '../type';

@Injectable({
    providedIn: 'root',
})
export class AppUserService {

    private apiUrl = '/api/users/me';

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
            const user = await lastValueFrom(this.http.get<UserTest>(this.apiUrl));
            if (user) {
                console.log('Utilisateur chargé :', user);
                this.userMe.next(user);
            }
        } catch (error) {
            console.error('Erreur lors du chargement de l\'utilisateur :', error);
        }
    }

}


