import { Component, OnInit } from '@angular/core';
import { AuthorProjections } from '../../type';
import { AuthorService } from '../../service/author.service';
import { CardComponent } from "../../components/card/card.component";
import { RouterModule } from '@angular/router';
import { CommonModule, NgClass } from '@angular/common';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';

@Component({
  selector: 'app-auteurs',
  imports: [CardComponent, RouterModule, NgClass, MatProgressSpinnerModule, CommonModule],
  standalone: true,
  templateUrl: './auteurs.component.html',
  styleUrl: './auteurs.component.scss'
})
export class AuteursComponent implements OnInit{
  pages!: number[];
  lastPage!: number;
  currentPage!: number;
  authors: AuthorProjections | null = null;
  isLoading = true;
  showEmptyMessage = false;



 constructor(
    private authorService : AuthorService,
    
 ) { this.currentPage = 0; }

  ngOnInit(): void {

   this.authorService.getAllAuthorWithPagination();
   this.authorService.currentauthorProjection.subscribe((data)=>{
     if (!data) {
       this.isLoading = true;
       setTimeout(() => {
         if (!this.authors) {
           this.showEmptyMessage = true;
           this.isLoading = false;
         }
       }, 10000);
       return;
     } else {
       this.authors = data;
       this.isLoading = false;
       this.showEmptyMessage = false;
     }
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
