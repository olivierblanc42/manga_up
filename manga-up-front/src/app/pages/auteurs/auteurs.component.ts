import { Component, OnInit } from '@angular/core';
import { AuthorProjections } from '../../type';
import { AuthorService } from '../../service/author.service';
import { CardComponent } from "../../components/card/card.component";
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-auteurs',
  imports: [CardComponent, RouterModule],
  standalone: true,
  templateUrl: './auteurs.component.html',
  styleUrl: './auteurs.component.scss'
})
export class AuteursComponent implements OnInit{

  authors: AuthorProjections | null = null;
 constructor(
    private authorService : AuthorService,
    
  ) { }

  ngOnInit(): void {

   this.authorService.getAllAuthorWithPagination();
   this.authorService.currentauthorProjection.subscribe((data)=>{
     this.authors = data;
     console.log("Genres récupérés :", this.authors);

   })


  }

}
