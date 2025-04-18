import { HttpClient, HttpHeaders, } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject, lastValueFrom, Observable } from 'rxjs';
import { MangaDtoRandom, MangaOne } from '../type';


@Injectable({
    providedIn: 'root'
})

export class MangaService{
    urlOne = "api/mangas/one";
    urlFourDate= "api/mangas/four";

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


    mangaOne = new BehaviorSubject<MangaOne[]>([])
    currentMangaOne = this.mangaOne.asObservable();

    mangaFour = new BehaviorSubject<MangaDtoRandom[]>([])
    currentfour = this.mangaFour.asObservable();


    async getMangaOne() {
        try {
            const r = await lastValueFrom(this.http.get<MangaOne[]>(this.urlOne));
            if (!r) return;
            this.mangaOne.next(r);
        } catch (err) {
            console.error('Erreur lors de la récupération du manga :', err);
        }
    }


    async getMangaFour() {
        try {
            const r = await lastValueFrom(this.http.get<MangaDtoRandom[]>(this.urlFourDate));
            if (!r) return;
            this.mangaFour.next(r);
        } catch (err) {
            console.error('Erreur lors de la récupération du manga :', err);
        }
    }



}