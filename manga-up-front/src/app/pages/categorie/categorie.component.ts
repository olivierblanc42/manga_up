import { Component, Inject, OnInit } from '@angular/core';
import { CategoryProjection, CategoryWithMangas } from '../../type';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { CategoryService } from '../../service/category.service';
import { CardComponent } from "../../components/card/card.component";
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-categorie',
  imports: [CardComponent, RouterModule, MatProgressSpinnerModule, CommonModule],
  standalone: true,
  templateUrl: './categorie.component.html',
  styleUrl: './categorie.component.scss'
})
export class CategorieComponent implements OnInit {

  id: string | null = null; 
  idOfUrl!: number; 
  category: CategoryWithMangas | null = null;
  isLoading = true;
  showEmptyMessage = false;

  constructor(
    @Inject(ActivatedRoute) private activatedRoute: ActivatedRoute,
    @Inject(Router) private router: Router,
    private categoriService: CategoryService) { }

  ngOnInit(): void {
    const id = this.activatedRoute.snapshot.paramMap.get('id');

    if (id) {
      this.idOfUrl = parseInt(id);
      if (!isNaN(this.idOfUrl)) {
        this.categoriService.getCategory(this.idOfUrl);
      }
    }

    this.categoriService.currentcategoriesWithManga.subscribe((data) => {
      if (!data) {
        this.isLoading = true;
        setTimeout(() => {
          if (!this.category) {
            this.showEmptyMessage = true;
            this.isLoading = false;
          }
        }, 10000);
        return;
      } else {
        this.category = data;
        this.isLoading = false;
        this.showEmptyMessage = false;
      }
    })

  }
}
