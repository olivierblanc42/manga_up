import { AuthorService } from './../../../service/author.service';
import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { AuthorWithMangas } from '../../../type';
import { noHtmlTagsValidator, urlValidator } from '../../../validator';

@Component({
  selector: 'app-author',
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  standalone: true,
  templateUrl: './author.component.html',
  styleUrl: './author.component.scss'
})
export class AuthorAdminComponent implements OnInit {

  idOfUrl!: number;
  author: AuthorWithMangas | null = null;
  authorForm!: FormGroup;
  
  constructor(
    private activatedRoute: ActivatedRoute,
    private router: Router,
    private authorService: AuthorService,
    private fb: FormBuilder
  ) { }



  ngOnInit(): void {
    this.initForm();


    const id = this.activatedRoute.snapshot.paramMap.get('id');
    if (id) {
      this.idOfUrl = parseInt(id);
      if (!isNaN(this.idOfUrl)) {
        this.loadAuthor(this.idOfUrl);
      }
    }

    this.authorService.currentAuthorOneProjection.subscribe(data=>{
      this.author = data;
      if (this.author) {
        // On met à jour le formulaire avec les données existantes

        this.authorForm.patchValue({
          firstname: this.author.firstname,
          lastname: this.author.lastname,
          description: this.author.description,
          url: this.author.url,
          birthdate: this.author.birthdate,
          genre:this.author.genre
        });
      }
      console.log('category', this.author);
    })


  }
  initForm(): void {
    this.authorForm = this.fb.group({
      lastname: ['', [Validators.required, noHtmlTagsValidator, Validators.maxLength(100)]],
      firstname: ['', [Validators.required, noHtmlTagsValidator, Validators.maxLength(100)]],
      description: ['', [Validators.required, noHtmlTagsValidator, Validators.maxLength(1000)]],
      birthdate: [null, Validators.required],
      url: ['', [Validators.required, urlValidator]],
      genre: ['', [Validators.required]]
    });
  }

  loadAuthor(id: number): void {
    this.authorService.getAuthor(id);
  }




  async onSubmit() {
    if (this.authorForm.valid) {
      const updatedCategory = {
        id: this.idOfUrl,
        ...this.authorForm.value
      };

      try {
        console.log(updatedCategory);



        await this.authorService.updateAuthor(updatedCategory);
        alert('l\'auteur est mis jour avec succès !');
        this.router.navigate(['/admin/authorsAdmin']); 
      } catch (error) {
        console.error('Erreur lors de la mise à jour', error);
        alert('Erreur lors de la mise à jour');
      }
    } else {
      this.authorForm.markAllAsTouched();
    }
  }

}
