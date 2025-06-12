import { PictureService } from './../../../service/picture.service';
import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { noHtmlTagsValidator } from '../../../validator';
import { PictureProjection } from '../../../type';

@Component({
  selector: 'app-admin-picture',
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './admin-picture.component.html',
  styleUrl: './admin-picture.component.scss',
  standalone: true,

})
export class AdminPictureComponent implements OnInit {
  idOfUrl!: number;
  pictureForm!: FormGroup;
  picture: PictureProjection | null = null;

 constructor(
    private activatedRoute: ActivatedRoute,
    private router: Router,
    private pictureService : PictureService,
    private fb: FormBuilder
  ) { }
  ngOnInit(): void {
    this.initForm();
    const id = this.activatedRoute.snapshot.paramMap.get('id');
    if (id) {
      this.idOfUrl = parseInt(id);
      if (!isNaN(this.idOfUrl)) {
        this.loadPicture(this.idOfUrl);
      }
    }


     this.pictureService.currentpicturesProjection.subscribe(data=>{
      this.picture = data
if(this.picture){
  this.pictureForm.patchValue({
    url: this.picture.url,
    isMain: this.picture.isMain

  })
}


     })



  }

  loadPicture(id: number): void {
    this.pictureService.getPicture(id);
  }
  initForm(): void {
    this.pictureForm = this.fb.group({
      url: ['', [Validators.required, noHtmlTagsValidator, Validators.maxLength(100)]],
      isMain: [false]
    });
  }


  async onSubmit(): Promise<void> {
    if (!this.picture || this.pictureForm.invalid) {
      return;
    }

    const updatedPicture = {
      id: this.picture.id,
      url: this.pictureForm.value.url,
      isMain: this.pictureForm.value.isMain
    };

    try {
      await this.pictureService.updatePicture(updatedPicture);
      alert('Image mise à jour avec succès');
      this.router.navigate(['/admin/picturesAdmin']); 
    } catch (err) {
      console.error('Erreur lors de la mise à jour', err);
      alert('Erreur lors de la mise à jour');
    }
  }
  


}
