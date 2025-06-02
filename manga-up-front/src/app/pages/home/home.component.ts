import { Component,CUSTOM_ELEMENTS_SCHEMA, OnInit } from '@angular/core';
import { CardComponent } from '../../components/card/card.component';
import { GenreProjection, GenreDto, MangaOne, MangaDtoRandom  } from '../../type';
import { GenreService } from '../../service/genre.service';
import { MangaService } from '../../service/manga.service';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-home',
  imports: [CommonModule,CardComponent, RouterModule],
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss',
  schemas : [CUSTOM_ELEMENTS_SCHEMA,],
  standalone: true
})

export class HomeComponent implements OnInit{

  genres!: GenreDto[];
  mangaOne: MangaOne[] = [];
  mangaDtoRandom: MangaDtoRandom[] = [];
  mangaDtoRandomFour: MangaDtoRandom[] = [];
  trackByManga: any;
 

  constructor(
    private genreService : GenreService,
    private mangaService : MangaService,
    private activatedRoute: ActivatedRoute,

  ) { }

  ngOnInit(): void {

    this.genreService.getFourGenre();
    this.genreService.currentGenreFour.subscribe((data) => {
      this.genres = data;
    //  console.log("Genres récupérés :", this.genres);
    });
    
   
    this.mangaService.getMangaOne();
    this.mangaService.currentMangaOne.subscribe((data) =>{
      this.mangaOne = data;
      console.log("manga récupérés :", this.mangaOne);

    })


    this.mangaService.getMangaFour()
    this.mangaService.currentfour.subscribe((data)=>{
      this.mangaDtoRandom = data ;
      //console.log("mangas récupérés :", this.mangaDtoRandom);

    })
    this.mangaService.getMangaFourRandom()
    this.mangaService.currentfourRandom.subscribe((data) => {
      this.mangaDtoRandomFour = data;
     // console.log("Random récupérés :", this.mangaDtoRandom);

    })

  }

  logMangaUrl(mangaId: number): void {
    console.log('/manga/' + mangaId);
  }

}
