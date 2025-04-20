import { ActivatedRoute, Router } from '@angular/router';
import { Author, AuthorProjection } from './../../type.d';
import { Component, Inject, OnInit } from '@angular/core';
import { AuthorService } from '../../service/author.service';
import { CardComponent } from "../../components/card/card.component";

@Component({
  selector: 'app-auteur',
  imports: [CardComponent],
  standalone: true,
  templateUrl: './auteur.component.html',
  styleUrl: './auteur.component.scss'
})
export class AuteurComponent implements OnInit {

  id: string | null = null; 
  idOfUrl!: number; 
  author: AuthorProjection  = {
    id: 0,
    lastname: "",
    firstname: "",
    description: "",
    createdAt: new Date(),
    birthdate: new Date(),
    url: "",
    genre: "",
    mangas: []
  };
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
    })
  }

}
