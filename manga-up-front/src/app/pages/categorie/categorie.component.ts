import { Component, Inject, OnInit } from '@angular/core';
import { CategoryProjection, CategoryWithMangas } from '../../type';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { CategoryService } from '../../service/category.service';
import { CardComponent } from "../../components/card/card.component";

@Component({
  selector: 'app-categorie',
  imports: [CardComponent, RouterModule],
  standalone: true,
  templateUrl: './categorie.component.html',
  styleUrl: './categorie.component.scss'
})
export class CategorieComponent implements OnInit {

  id: string | null = null; 
  idOfUrl!: number; 
  category: CategoryWithMangas | null = null;

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
      this.category = data
      console.log('category', this.category);
    })

  }
}
