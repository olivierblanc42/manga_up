import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { BehaviorSubject, firstValueFrom, Observable } from 'rxjs';
import { MangaBaseProjection, MangaBaseProjections, UserTest } from '../type';
import { environment } from '../../environments/environment.prod';

@Injectable({
    providedIn: 'root',
})
export class SearchService{
    urlSearch = `${environment.apiUrl}api/public/search`;

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
    mangasSearch = new BehaviorSubject<MangaBaseProjections| null>(null)
    currentmangasSearch = this.mangasSearch.asObservable();



    async getMangas(letter: string, page: number = 0) {
        try {
            const url = `${this.urlSearch}/${letter}?page=${page}`;
            const r = await firstValueFrom(this.http.get<MangaBaseProjections>(url));
            if (!r) return;
            this.mangasSearch.next(r);
            console.log(r)
        } catch (err) {
            console.error('Erreur lors de la recherche paginée :', err);
        }
      }



} 
