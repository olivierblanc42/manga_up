import { MangaProjections } from './../../type.d';
import { MangaService } from './../../service/manga.service';
import { Component, OnInit } from '@angular/core';
import { CardComponent } from '../../components/card/card.component';
import { RouterModule } from '@angular/router';
import { NgClass } from '@angular/common';

@Component({
  selector: 'app-mangas',
  imports: [CardComponent, RouterModule, NgClass],
  standalone: true,
  templateUrl: './mangas.component.html',
  styleUrl: './mangas.component.scss'
})
export class MangasComponent implements OnInit{

  mangas: MangaProjections | null = null;
  pages!: number[];
  lastPage!: number;
  currentPage!: number;
 constructor(
    private mangaservice : MangaService,
    
 ) {
   this.currentPage = 0;
}
  ngOnInit(): void { 
   
    this.mangaservice.getMangas();
    this.mangaservice.currentMangaPaginations.subscribe((data) => {
      this.mangas = data;
      console.log("manga récupérés :", this.mangas);
      this.pages = this.convertNumberToArray(this.mangas?.totalPages!)
      this.lastPage = this.mangas?.totalPages!;
    })
  }
  convertNumberToArray(size: number) {
    const array = new Array<number>(size);
    for (let i = 0; i < array.length; i++) {
      array[i] = i;
    }
    return array;
  }
  pageMangas(page: number) {
    console.log("dans pageMangas page : ", page);
    this.currentPage = page;
    console.log("dans pageMangas currentPage : ", this.currentPage);
    this.mangaservice.getMangas(page);
  }

  pagePrevious() {
    console.log("dans pagePrevious currentPage : ", this.currentPage);
    if (this.currentPage > 0) {
      this.pageMangas(this.currentPage - 1);
    }
  }
  pageNext() {
    console.log("dans pageNext currentPage : ", this.currentPage);
    if (this.currentPage < this.lastPage - 1) {
      this.pageMangas(this.currentPage + 1);
    }
  }
}
