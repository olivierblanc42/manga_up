import { AfterViewInit, Component,CUSTOM_ELEMENTS_SCHEMA, ElementRef, OnInit, ViewChild } from '@angular/core';
import { CardComponent } from '../../components/card/card.component';
import { GenreProjection, GenreDto, MangaOne, MangaDtoRandom  } from '../../type';
import { GenreService } from '../../service/genre.service';
import { MangaService } from '../../service/manga.service';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';

@Component({
  selector: 'app-home',
  imports: [CommonModule, CardComponent, RouterModule, MatProgressSpinnerModule],
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss',
  schemas : [CUSTOM_ELEMENTS_SCHEMA,],
  standalone: true
})

export class HomeComponent implements OnInit, AfterViewInit {

  genres: GenreDto[]= [];
  mangaOne: MangaOne | null = null;;
  mangaDtoFour: MangaDtoRandom[] = [];
  mangaDtoRandomFourDate: MangaDtoRandom[] = [];
  trackByManga: any;
  isLoadingMangaOne = true;
  isLoadingcurrentfour = true;
  isLoadingcurrentfourDate = true;
  isLoadingcurrentGenre = true;
  isloadingGenre = true;

  showEmptyMessage = false;
  @ViewChild('container') container!: ElementRef;

  constructor(
    private genreService : GenreService,
    private mangaService : MangaService,
    private activatedRoute: ActivatedRoute,

  ) { }


  ngOnInit(): void {

    this.genreService.getFourGenre();
    this.genreService.currentGenreFour.subscribe((data) => {
      this.genres = data;
      if (!data) {
        this.isloadingGenre = true;
        return;
      }
      if (this.genres.length === 0) {
        this.genres = [];

        setTimeout(() => {
          if (this.genres.length === 0) {
            this.showEmptyMessage = true;
            this.isloadingGenre = false;

          }
        }, 10000);

      } else {
        this.genres = data;
        this.showEmptyMessage = false;
      }
      this.isloadingGenre = false;


    });
    
   
    this.mangaService.getMangaOne();
    this.mangaService.currentMangaOne.subscribe((data) =>{
      if (!data) {
        this.isLoadingMangaOne = true;
        setTimeout(() => {
          if (!this.mangaOne) {
            this.showEmptyMessage = true;
            this.isLoadingMangaOne = false;
          }
        }, 10000);
        return;
      } else {
        this.mangaOne = data;
        this.isLoadingMangaOne = false;
        this.showEmptyMessage = false;
      }
    })


    this.mangaService.getMangaFour();
    this.mangaService.currentfour.subscribe((data) => {
      if (!data) {
        this.isLoadingcurrentfour = true;
        return;
      }
      if (data.length === 0) {
        this.mangaDtoFour = [];

        setTimeout(() => {
          if (this.mangaDtoFour.length === 0) {
            this.showEmptyMessage = true;
            this.isLoadingcurrentfour = false;

          }
        }, 10000);

      } else {
        this.mangaDtoFour = data;
        this.showEmptyMessage = false; 
      }
      this.isLoadingcurrentfour = false;

    });
    
    this.mangaService.getMangaFourRandom()
    this.mangaService.currentfourRandom.subscribe((data) => {
      if (!data) {
       this.isLoadingcurrentfourDate = true;
        return;
      }
      if (data.length === 0) {
        this.mangaDtoRandomFourDate = [];

        setTimeout(() => {
          if (this.mangaDtoRandomFourDate.length === 0) {
            this.showEmptyMessage = true;
            this.isLoadingcurrentfourDate = false;
          }
        }, 10000);

      } else {
        this.mangaDtoRandomFourDate = data;
        this.showEmptyMessage = false;
      }
      this.isLoadingcurrentfourDate = false;

    })

  }

  ngAfterViewInit() {
    if (this.container) {
      const shadowRoot = this.container.nativeElement.shadowRoot;
      const nextBtn = shadowRoot?.querySelector('.swiper-button-next');
      const nextBtnsvg = shadowRoot?.querySelector('.swiper-button-next >svg');
      const prevBtn = shadowRoot?.querySelector('.swiper-button-prev');
      const prevBtnsvg = shadowRoot?.querySelector('.swiper-button-prev >svg');
      if (prevBtn) {
        prevBtn.style.top = '90%';
        prevBtn.style.zIndex = '10'
      }
      if (prevBtnsvg) {
        prevBtnsvg.style.width = '60%';
      }
      if (nextBtn) {
        nextBtn.style.top = '90%';
        nextBtn.style.zIndex = '10'      }

      if (nextBtnsvg) {
        nextBtnsvg.style.width = '60%';
      }


    }
  }
  
  logMangaUrl(mangaId: number): void {
    console.log('/manga/' + mangaId);
  }

 


}
