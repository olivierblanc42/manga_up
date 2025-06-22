import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { GenreService } from '../../../service/genre.service';
import { GenreWithMangas } from '../../../type';

@Component({
  selector: 'app-genre',
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  standalone: true,
  templateUrl: './genre.component.html',
  styleUrl: './genre.component.scss'
})
export class GenreAdminComponent implements OnInit {
  idOfUrl!: number;
  genreForm!: FormGroup;
  genre: GenreWithMangas | null = null;

    constructor(
      private activatedRoute: ActivatedRoute,
      private router: Router,
      private genreService: GenreService,
      private fb: FormBuilder
    ) { }
  ngOnInit(): void {
    this.initForm();



    const id = this.activatedRoute.snapshot.paramMap.get('id');
    if (id) {
      this.idOfUrl = parseInt(id);
      if (!isNaN(this.idOfUrl)) {
        this.loadGenre(this.idOfUrl);
      }
    }

    this.genreService.curentGenreSolo.subscribe(data => {
      this.genre = data;
      if (this.genre) {
        this.genreForm.patchValue({
          label: this.genre.label,
          description: this.genre.description,
          url: this.genre.url
        });
      }
      console.log('genre', this.genre);
    });

    
  }



  initForm(): void {
    this.genreForm = this.fb.group({
      label: ['', Validators.required],
      description: ['', Validators.required],
      url: ['', Validators.required]
    });
  }

  loadGenre(id: number): void {
    this.genreService.getGenreManga(id);
  }



  async onSubmit() {
    if (this.genreForm.valid) {
      const updatedGenre = {
        id: this.idOfUrl,
        ...this.genreForm.value
      };
      try {
        console.log(updatedGenre);
        await this.genreService.updateGenre(updatedGenre);
        alert('Genre mis à jour avec succès !');
        this.router.navigate(['/admin/genresAdmin']); 
      } catch (error) {
        console.error('Erreur lors de la mise à jour', error);
        alert('Erreur lors de la mise à jour');
      }
    } else {
      this.genreForm.markAllAsTouched();
    }
  }


}
