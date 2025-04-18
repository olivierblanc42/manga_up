import { Component, OnInit } from '@angular/core';
import { CategoriesProjections } from '../../type';
import { CategoryService } from '../../service/category.service';
import { CardComponent } from "../../components/card/card.component";

@Component({
  selector: 'app-categories',
  imports: [CardComponent],
  standalone: true,
  templateUrl: './categories.component.html',
  styleUrl: './categories.component.scss'
})
export class CategoriesComponent implements OnInit {
  categories: CategoriesProjections | null = null;


 constructor(
    private categoryService : CategoryService,
    
  ) { }


  ngOnInit(): void {
    this.categoryService.getAllGenreWithPagination();
    
    this.categoryService.currentCategoriesProjection.subscribe((data)=>{
      this.categories = data;
    })
  }

}
