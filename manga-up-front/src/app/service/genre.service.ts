import { HttpClient, HttpHeaders, } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject, firstValueFrom, lastValueFrom, Observable } from 'rxjs';
import { GenreProjections, GenreProjection, GenreDto, GenreWithMangas, MangaWithImages, MangasWithImages } from '../type';


@Injectable({
    providedIn: 'root'
})


export class GenreService {
    url = "/api/public/genres/pagination";
    urlFour = "api/public/genres/four";
    urlGenre= "/api/public/genres/"
    urlAdd = "/api/genres/add"


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

    genresProjectionPaginations = new BehaviorSubject< GenreProjections | null>(null);
    currentGenresProjectionPaginations = this.genresProjectionPaginations.asObservable();
   
    genreFour = new BehaviorSubject<GenreDto[] >([]);
    currentGenreFour = this.genreFour.asObservable();

    genreSolo = new BehaviorSubject<GenreWithMangas | null>(null);
    curentGenreSolo = this.genreSolo.asObservable();


    genreDto = new BehaviorSubject<GenreDto | null>(null);


    async getAllGenreWithPagination(page: number = 0) {
        try {
            const r = await lastValueFrom(this.http.get<GenreProjections>(`${this.url}?page=${page}`));
            if (!r) return;
            this.genresProjectionPaginations.next(r);
            console.log('Genres récupérés avec succès :', r);
        } catch (err) {
            console.error('Erreur lors de la récupération des genres :', err);
        }
    }


    getFourGenre() {
       // console.log('Request to fetch four genres is being sent...');
        lastValueFrom(this.http.get<GenreDto[]>(this.urlFour))
            .then((r) => {
                if (!r) return;
                this.genreFour.next(r);
            })
            .catch((err) => {
                console.error('Erreur lors de la récupération des genres :', err);
            });
    }


    async getGenreManga(id: number, page: number = 0) {
        try {
            const r = await lastValueFrom(this.http.get<{ genre: GenreWithMangas, mangas: MangasWithImages }>(`${this.urlGenre}${id}/mangas?page=${page}`));
          if(r){
            const genreWithMangas: GenreWithMangas = {
                ...r.genre,
                mangasWithImages: r.mangas
            };
            
            this.genreSolo.next(genreWithMangas);
            console.log('Genre récupéré avec succès :', genreWithMangas);
          }

        } catch (err) {
            console.error('Erreur lors de la récupération des genres :', err);
        }
    }


    async addGenre(category: GenreDto): Promise<GenreDto> {
        const response = await firstValueFrom(
            this.http.post<GenreDto>(this.urlAdd, category,{ withCredentials: true } )
        );

        this.genreDto.next(response);
        return response; 
    }
      
    
}
