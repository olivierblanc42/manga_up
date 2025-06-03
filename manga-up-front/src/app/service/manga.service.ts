import { HttpClient, HttpHeaders, } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject, firstValueFrom, lastValueFrom, Observable } from 'rxjs';
import { Manga, MangaDto, MangaDtoRandom, MangaOne,MangaPaginations,MangaProjection,MangaProjections } from '../type';


@Injectable({
    providedIn: 'root'
})

export class MangaService{
    urlOne = "api/public/one";
    urlFourDate= "api/public/four";
    urlPagination = "api/public/mangas/paginations";
    url = "/api/public/manga/"
    urlRandom ="/api/public/randomFour"
    urlAdd = "/api/mangas/add"
    urldelete ="/api/mangas"

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


    mangaFourRandom = new BehaviorSubject<MangaDtoRandom[]>([])
    currentfourRandom = this.mangaFourRandom.asObservable();
    
    mangaPagination = new BehaviorSubject<MangaPaginations | null>(null);
    currentMangaPaginations = this.mangaPagination.asObservable();


    mangaProjection = new BehaviorSubject<MangaProjection | null>(null);
    currentMangaProjection = this.mangaProjection.asObservable();


       manga = new BehaviorSubject<Manga | null>(null);
       mangaDto = new BehaviorSubject<MangaDto | null>(null);
   



    async getMangas(page: number = 0) {
        try {
            const r = await lastValueFrom(this.http.get<MangaPaginations>(`${this.urlPagination}?page=${page}`));
            if (!r) return;
            this.mangaPagination.next(r);
        } catch (err) {
            console.error('Erreur lors de la récupération du manga :', err);
        }
    }



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
            console.log(r)
            this.mangaFour.next(r);
        } catch (err) {
            console.error('Erreur lors de la récupération du manga :', err);
        }
    }

    async getMangaFourRandom() {
        try {
            const r = await lastValueFrom(this.http.get<MangaDtoRandom[]>(this.urlRandom));
            if (!r) return;
            console.log(r)
            this.mangaFourRandom.next(r);
        } catch (err) {
            console.error('Erreur lors de la récupération du manga :', err);
        }
    }



    async  getManga(id:number){
         try {
             const r = await lastValueFrom(this.http.get<MangaProjection>(`${this.url}${id}`));
             if (!r) return;
             console.log(r)
             this.mangaProjection.next(r);
         } catch (err) {
             console.error('Erreur lors de la récupération du manga :', err);
         }
     }



    async deleteManga(id: number): Promise<Manga> {
        const response = await firstValueFrom(
            this.http.delete<Manga>(`${this.urldelete}/${id}`, {
                withCredentials: true
            })
        );
        this.manga.next(response);
        return response;
    }

    async addManga(category: MangaDto): Promise<MangaDto> {
        const response = await firstValueFrom(
            this.http.post<MangaDto>(this.urlAdd, category,{ withCredentials: true } )
        );

        this.mangaDto.next(response);
        return response; 
    }
     
}