import { PictureService } from './../../../service/picture.service';
import { Component, OnInit } from '@angular/core';
import { PictureProjections } from '../../../type';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-admin-pictures',
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './admin-pictures.component.html',
  styleUrl: './admin-pictures.component.scss'
})
export class AdminPicturesComponent  implements OnInit {
  pictures: PictureProjections | null = null;
  currentPage!: number;
  pages!: number[];
  lastPage!: number;

  constructor(

    private pictureService:  PictureService,
    private fb: FormBuilder


  ) {
    this.currentPage = 0;

  }
  ngOnInit(): void {

    this.pictureService.getAllpictures();
    this.pictureService.currentpicturePagination.subscribe((data) => {
      this.pictures =data;
      console.log(this.pictures)
    })

  }
  pagePicture(page: number) {
    console.log("dans pageMangas page : ", page);
    this.currentPage = page;
    console.log("dans pageMangas currentPage : ", this.currentPage);
    this.pictureService.getAllpictures(page);
    this.pages = this.convertNumberToArray(this.pictures?.totalPages!);
    this.lastPage = this.pictures?.totalPages!;
  }

  pagePrevious() {
    console.log("dans pagePrevious currentPage : ", this.currentPage);
    if (this.currentPage > 0) {
      this.pagePicture(this.currentPage - 1);
    }
  }
  pageNext() {
    console.log("dans pageNext currentPage : ", this.currentPage);
    if (this.currentPage < this.lastPage - 1) {
      this.pagePicture(this.currentPage + 1);
    }
  }
  convertNumberToArray(size: number) {
    const array = new Array<number>(size);
    for (let i = 0; i < array.length; i++) {
      array[i] = i;
    }
    return array;
  }



  loadPictures() {
    try {
      this.pictureService.getAllpictures();
      this.pictureService.currentpicturePagination.subscribe((data) => {
        this.pictures = data;
        console.log(this.pictures)
      })
    } catch (error) {
      console.error('Erreur lors du chargement des images', error);
    }
  }



  async deletePicture(id: number) {
    const confirmed = confirm('Voulez-vous vraiment supprimer l\'image ?');
    if (!confirmed) return;
    try {
      await this.pictureService.deletePicture(id);
      this.loadPictures();
    } catch (error) {
      console.error('Erreur lors de la suppression', error);
    }
  }




  
}
