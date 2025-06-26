import { Component, OnInit } from '@angular/core';
import { AuthorProjections } from '../../type';
import { AuthorService } from '../../service/author.service';
import { CardComponent } from "../../components/card/card.component";
import { RouterModule } from '@angular/router';
import { NgClass } from '@angular/common';

@Component({
  selector: 'app-auteurs',
  imports: [CardComponent, RouterModule, NgClass],
  standalone: true,
  templateUrl: './auteurs.component.html',
  styleUrl: './auteurs.component.scss'
})
export class AuteursComponent implements OnInit{
  pages!: number[];
  lastPage!: number;
  currentPage!: number;
  authors: AuthorProjections | null = null;
 constructor(
    private authorService : AuthorService,
    
 ) { this.currentPage = 0; }

  ngOnInit(): void {

   this.authorService.getAllAuthorWithPagination();
   this.authorService.currentauthorProjection.subscribe((data)=>{
     this.authors = data;
     console.log("Genres récupérés :", this.authors);
     this.pages = this.convertNumberToArray(this.authors?.totalPages!)
     this.lastPage = this.authors?.totalPages!;
   })


  }
  convertNumberToArray(size: number) {
    const array = new Array<number>(size);
    for (let i = 0; i < array.length; i++) {
      array[i] = i;
    }
    return array;
  }
  pageAuthor(page: number) {
    console.log("dans pageGenres page : ", page);
    this.currentPage = page;
    console.log("dans pageGenres currentPage : ", this.currentPage);
    this.authorService.getAllAuthorWithPagination(page);
  }

  pagePrevious() {
    console.log("dans pagePrevious currentPage : ", this.currentPage);
    if (this.currentPage > 0) {
      this.pageAuthor(this.currentPage - 1);
    }
  }
  pageNext() {
    console.log("dans pageNext currentPage : ", this.currentPage);
    if (this.currentPage < this.lastPage - 1) {
      this.pageAuthor(this.currentPage + 1);
    }
  }
}
