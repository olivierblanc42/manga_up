import { register } from 'swiper/element/bundle';
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, catchError, lastValueFrom, map, Observable, of, switchMap } from 'rxjs';
import { AppUserRegister, AuthorProjections, GenderRegister } from '../type';
import { AuthUserInfo } from '../type'; 
import { environment } from '../../environments/environment.prod';

@Injectable({
    providedIn: 'root',
})
export class AuthService {
    private apiUrl = `${environment.apiUrl}api/auth`;
    private apiRegisterUrl = `${environment.apiUrl}api/auth/register`;
    private apiGender = `${environment.apiUrl}api/public/genderUser`;


       appUserRegister = new BehaviorSubject<AppUserRegister | null>(null)
       currentappUserRegisterProjection = this.appUserRegister.asObservable();

       appUserGender = new BehaviorSubject<GenderRegister[]>([])
       currentappUserGenderProjection = this.appUserGender.asObservable();

    userInfo = new BehaviorSubject<AuthUserInfo | null>(null);
    currentUserInfo$ = this.userInfo.asObservable();
       
    private userRoleSubject = new BehaviorSubject<string | null>(this.getUserRole());
    userRole$ = this.userRoleSubject.asObservable();
    


    constructor(private http: HttpClient) { }



    async getGender() {
        try {
            const r = await lastValueFrom(this.http.get<GenderRegister[]>(`${this.apiGender}`));
            if (!r) return;
            this.appUserGender.next(r);
            console.log('Genres récupérés avec succès :', r);
        } catch (err) {
            console.error('Erreur lors de la récupération des genres :', err);
        }
    }

    login(credentials: { username: string; password: string }): Observable<any> {
        return this.http.post(`${this.apiUrl}/login`, credentials, { withCredentials: true });  
    }

    logout(): void {
        this.http.post('http://localhost:8080/api/auth/logout', {}, { withCredentials: true })
            .subscribe({
                next: () => {
                    console.log('Déconnecté avec succès.');
                    this.clearAuthState(); 
                    
                },
                error: (err) => {
                    console.error('Erreur lors de la déconnexion :', err);
                }
            });
    }
      


    isLoggedIn(): Observable<boolean> {
        return this.http.get('/api/auth/check', { withCredentials: true }).pipe(
            map(() => true),
            catchError(() => of(false))
        );
    }


   register(user: AppUserRegister): Observable<any> {
        return this.http.post(this.apiRegisterUrl, user).pipe(
            map((response) => {
                this.appUserRegister.next(response as AppUserRegister); 
                return response;
            }),
            catchError((error) => {
                console.error('Erreur lors de l\'inscription :', error);
                throw error; 
            })
        );
    }

    registerUser(registerDto: AppUserRegister): Observable<AppUserRegister> {
        return this.http.post<AppUserRegister>(this.apiRegisterUrl, registerDto);
    }


    async getRegisterTest(registerDto: AppUserRegister) {
        try {
            const r = await lastValueFrom(this.http.post<AppUserRegister>(`${this.apiRegisterUrl}`, registerDto));
            if (!r) return;
            this.appUserRegister.next(r);
            console.log('Genres récupérés avec succès :', r);
        } catch (err) {
            console.error('Erreur lors de la récupération des genres :', err);
        }
    }

    
    clearAuthState(): void {
        this.userInfo.next(null);
        this.setUserRole(null); 
        localStorage.removeItem('username');
    }
      
      


    loginAfterLogout(credentials: { username: string; password: string }): Observable<any> {
        return this.http.post(`${this.apiUrl}/login`, credentials, { withCredentials: true }).pipe(
            map((response: any) => {
                const userInfo: AuthUserInfo = {
                    username: response.username,
                    role: response.role
                };
                this.userInfo.next(userInfo);
                localStorage.setItem('username', userInfo.username);
                this.setUserRole(userInfo.role); 
                return response;
            })
        );
    }
      

    getUserRole(): string | null {
        const user = this.userInfo.value;
        return user?.role ?? localStorage.getItem('userRole');
    }
      
    setUserRole(role: string | null): void {
        if (role) {
            localStorage.setItem('userRole', role);
        } else {
            localStorage.removeItem('userRole');
        }
        this.userRoleSubject.next(role);
    }
      
    
}
