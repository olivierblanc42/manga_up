import { Component, OnInit } from '@angular/core';
import { Manga, UserTest } from '../../type';
import { AppUserService } from '../../service/appUser.service';
import { LogoutComponent } from "../../components/logout/logout.component";
import { CardComponent } from '../../components/card/card.component';
import { RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { NgSelectModule } from '@ng-select/ng-select';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { noHtmlTagsValidator } from '../../validator';

@Component({
  selector: 'app-account',
  imports: [LogoutComponent, CardComponent, RouterModule, CommonModule, ReactiveFormsModule, RouterModule, NgSelectModule, MatProgressSpinnerModule],
  templateUrl: './account.component.html',
  styleUrl: './account.component.scss'
})
export class AccountComponent implements OnInit {
  onSubmit() {
    throw new Error('Method not implemented.');
  }

  user: UserTest | null = null;
  mangas: Manga[] = [];
  userForm!: FormGroup;


  constructor(
    private appUserService: AppUserService
    , private fb: FormBuilder) { }


  ngOnInit(): void {
    this.initForm();




    this.appUserService.loadCurrentUser();
    this.appUserService.currentUserMe.subscribe((data) => {
      this.user = data;
      this.mangas = this.user?.mangas ?? [];

      console.log('user récupérés avec succès :', data);

      if (this.user) {
        this.userForm.patchValue({
          username: this.user.username,
          firstname: this.user.firstname,
          lastname: this.user.lastname,
          email: this.user.email,
          phoneNumber: this.user.phoneNumber,
        });
      }



    });

    if (this.mangas?.length) {
      this.mangas = this.mangas;
    } else {
      this.mangas = [];
    }
  }



  initForm(): void {
    this.userForm = this.fb.group({
      username: ['', [Validators.required, noHtmlTagsValidator, Validators.maxLength(100)]],
      firstname: ['', [noHtmlTagsValidator, Validators.maxLength(100)]],
      lastname: ['', [Validators.required, noHtmlTagsValidator, Validators.maxLength(1000)]],
      email: ['', [Validators.required, noHtmlTagsValidator, Validators.maxLength(1000)]],
      phoneNumber: ['', [
        Validators.required,
        Validators.pattern(/^\+?[0-9\s\-]{7,15}$/)
      ]],
    });
  }

  // username: string;
  // firstname: string;
  // lastname: string;
  // email: string;
  // phoneNumber: string;
  // userAddressLitle: UserAddressLitle;

}
