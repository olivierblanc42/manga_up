import { Component, OnInit } from '@angular/core';
import { GenreProjections } from '../../type';
import { GenreService } from '../../service/genre.service';
import { CardComponent } from '../../components/card/card.component';
import { RouterModule } from '@angular/router';
import { NgClass } from "@angular/common";

@Component({
  selector: 'app-genres',
  imports: [CardComponent, RouterModule, NgClass],
  standalone: true,
  templateUrl: './genres.component.html',
  styleUrl: './genres.component.scss'
})
export class GenresComponent implements OnInit {

  genres: GenreProjections | null = null;
  pages!: number[];
  lastPage!: number;
  currentPage!: number;
 constructor(
    private genreService : GenreService,
    
 ) {
   this.currentPage = 0;
 }

  ngOnInit(): void {
    this.genreService.getAllGenreWithPagination();

   this.genreService.currentGenresProjectionPaginations.subscribe((data)=>{
     this.genres = data; 
     this.pages = this.convertNumberToArray(this.genres?.totalPages!)
     this.lastPage = this.genres?.totalPages!;
     console.log("Genres récupérés :", this.genres);
   })

  }

  convertNumberToArray(size: number) {
    const array = new Array<number>(size);
    for (let i = 0; i < array.length; i++) {
      array[i] = i;
    }
    return array;
  }
   pageGenres(page: number){
    console.log("dans pageGenres page : ", page);
    this.currentPage=page;
    console.log("dans pageGenres currentPage : ", this.currentPage);
     this.genreService.getAllGenreWithPagination(page);
  }

  pagePrevious() {
    console.log("dans pagePrevious currentPage : ", this.currentPage);
    if (this.currentPage > 0) {
      this.pageGenres(this.currentPage - 1);
    }
  }
  pageNext() {
    console.log("dans pageNext currentPage : ", this.currentPage);
    if (this.currentPage < this.lastPage - 1) {
      this.pageGenres(this.currentPage + 1);
    }
  }
  



  
}
