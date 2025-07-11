import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { BehaviorSubject, firstValueFrom, Observable } from 'rxjs';
import { UserTest, UserUpdate } from '../type';
import { environment } from '../../environments/environment';

@Injectable({
    providedIn: 'root',
})
export class AppUserService {

    private apiUrl = `api/users/me`;
    private apiUpdate = `api/users/update/me`;


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

    userMeUpdate = new BehaviorSubject<UserUpdate | null>(null);
    currentUserMeUpdate = this.userMeUpdate.asObservable();




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



    async updateCurrentUser(currentUser: UserUpdate): Promise<UserUpdate> {
        try {
            const urlWithId = `${this.apiUpdate}`;
            const updatedUser = await firstValueFrom(
                this.http.put<UserUpdate>(urlWithId, currentUser, { withCredentials: true })
            );
            this.userMeUpdate.next(updatedUser);
            return updatedUser;
        } catch (error) {
            console.error('Erreur lors de la mise à jour:', error);
            throw error;
        }
    }

}


