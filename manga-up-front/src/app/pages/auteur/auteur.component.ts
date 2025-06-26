import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { Author, AuthorProjection, AuthorWithMangas } from './../../type.d';
import { Component, Inject, OnInit } from '@angular/core';
import { AuthorService } from '../../service/author.service';
import { CardComponent } from "../../components/card/card.component";

@Component({
  selector: 'app-auteur',
  imports: [CardComponent, RouterModule],
  standalone: true,
  templateUrl: './auteur.component.html',
  styleUrl: './auteur.component.scss'
})
export class AuteurComponent implements OnInit {

  id: string | null = null; 
  idOfUrl!: number; 
  author: AuthorWithMangas | null = null;

  constructor(
    @Inject(ActivatedRoute) private activatedRoute: ActivatedRoute,
    @Inject(Router) private router: Router,
    private authorService: AuthorService) { }
  ngOnInit(): void {
    const id = this.activatedRoute.snapshot.paramMap.get('id');

    if (id) {
      this.idOfUrl = parseInt(id);
      if (!isNaN(this.idOfUrl)) {
        this.authorService.getAuthor(this.idOfUrl);
      }
    }
    this.authorService.authorOneProjection.subscribe((data)=>{
      this.author =data;
      console.log('author récupérés avec succès :', data);
    })
  }

}
