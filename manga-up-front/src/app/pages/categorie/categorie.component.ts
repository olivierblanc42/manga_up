import { Component, Inject, OnInit } from '@angular/core';
import { CategoryProjection } from '../../type';
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
  category: CategoryProjection = 
    {
      id: 0,
      label: "",
      description: "",
      createdAt: new Date(),
      mangas: []
    }
  ;

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

    this.categoriService.currentCategorieProjection.subscribe((data) => {
      this.category = data
    })

  }
}
