import { Component, OnInit } from '@angular/core';
import { CategoriesProjections } from '../../type';
import { CategoryService } from '../../service/category.service';
import { CardComponent } from "../../components/card/card.component";
import { RouterModule } from '@angular/router';
import { CommonModule, NgClass } from '@angular/common';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';

@Component({
  selector: 'app-categories',
  imports: [CardComponent, RouterModule, NgClass, MatProgressSpinnerModule, CommonModule],
  standalone: true,
  templateUrl: './categories.component.html',
  styleUrl: './categories.component.scss'
})
export class CategoriesComponent implements OnInit {
  categoriesProjection(categoriesProjection: any) {
    throw new Error('Method not implemented.');
  }
  categories: CategoriesProjections | null = null;
  pages!: number[];
  lastPage!: number;
  currentPage!: number;
  isLoading = true;
  showEmptyMessage = false;
 constructor(
   public categoryService : CategoryService,
    
 ) { this.currentPage = 0; }


  ngOnInit(): void {
    console.log("in ngOnInit");

    this.categoryService.getAllCategoriesWithPagination();
    
    this.categoryService.currentCategoriesProjection.subscribe((data)=>{
      if (!data) {
        this.categories = null;
        this.isLoading = true;
        setTimeout(() => {
          if (!this.categories) {
            this.showEmptyMessage = true;
            this.isLoading = false;
          }
        }, 10000);
        return;
      } else {
        this.categories = data;
        this.isLoading = false;
        this.showEmptyMessage = false;
      }
      this.pages = this.convertNumberToArray(this.categories?.totalPages!)
      this.lastPage = this.categories?.totalPages!;
      console.log("categories : ", this.categories);
    })
  }
  convertNumberToArray(size: number) {
    const array = new Array<number>(size);
    for (let i = 0; i < array.length; i++) {
      array[i] = i;
    }
    return array;
  }
  pageCategories(page: number) {
    console.log("dans pageGenres page : ", page);
    this.currentPage = page;
    console.log("dans pageGenres currentPage : ", this.currentPage);
    this.categoryService.getAllCategoriesWithPagination(page);
  }

  pagePrevious() {
    console.log("dans pagePrevious currentPage : ", this.currentPage);
    if (this.currentPage > 0) {
      this.pageCategories(this.currentPage - 1);
    }
  }
  pageNext() {
    console.log("dans pageNext currentPage : ", this.currentPage);
    if (this.currentPage < this.lastPage - 1) {
      this.pageCategories(this.currentPage + 1);
    }
  }
}
