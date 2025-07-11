import { Component, OnInit } from '@angular/core';
import { Manga, UserTest, UserUpdate } from '../../type';
import { AppUserService } from '../../service/appUser.service';
import { LogoutComponent } from "../../components/logout/logout.component";
import { CardComponent } from '../../components/card/card.component';
import { RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { NgSelectModule } from '@ng-select/ng-select';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { noHtmlTagsValidator } from '../../validator';
import { MatExpansionModule } from '@angular/material/expansion';
@Component({
  selector: 'app-account',
  imports: [LogoutComponent, CardComponent, RouterModule, CommonModule, ReactiveFormsModule, RouterModule, NgSelectModule, MatProgressSpinnerModule, MatExpansionModule],
  templateUrl: './account.component.html',
  styleUrl: './account.component.scss'
})
export class AccountComponent implements OnInit {


  user: UserTest | null = null;
  mangas: Manga[] = [];
  userForm!: FormGroup;
  userUpdate: UserUpdate | null = null;

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
          firstname: this.user.firstname,
          lastname: this.user.lastname,
          email: this.user.email,
          phoneNumber: this.user.phoneNumber,
          url: this.user.url,
          idUserAddress: {
            line1: this.user.idUserAddress?.line1 || '',
            line2: this.user.idUserAddress?.line2 || '',
            line3: this.user.idUserAddress?.line3 || '',
            postalCode: this.user.idUserAddress?.postalCode || '',
            city: this.user.idUserAddress?.city || ''
          }
        });
      }
    });

    if (!this.mangas?.length) {
      this.mangas = [];
    }
  }



  initForm(): void {
    this.userForm = this.fb.group({
      firstname: ['', [noHtmlTagsValidator, Validators.maxLength(100)]],
      lastname: ['', [Validators.required, noHtmlTagsValidator, Validators.maxLength(1000)]],
      email: ['', [Validators.required, noHtmlTagsValidator, Validators.maxLength(1000)]],
      phoneNumber: ['', [
        Validators.required,
        Validators.pattern(/^\+?[0-9\s\-]{7,15}$/)
      ]],
      url: ['', [noHtmlTagsValidator, Validators.maxLength(100)]],
      idUserAddress: this.fb.group({
        line1: ['', [Validators.required, Validators.maxLength(255)]],
        line2: [''],
        line3: [''],
        postalCode: [''],
        city: ['']
      })
    });
  }


  async onSubmit() {
    if (this.userForm.valid) {
      const formValue = this.userForm.value;
      const updateUser = {
        ...formValue,
      };

      try {
        console.log(updateUser);

        await this.appUserService.updateCurrentUser(updateUser)


      } catch (error) {
        console.error('Erreur lors de la mise à jour', error);
      }
    } else {
      this.userForm.markAllAsTouched();
    }
  }
}
