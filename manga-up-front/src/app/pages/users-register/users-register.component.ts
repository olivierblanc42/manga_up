import { GenderRegister } from './../../type.d';
import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../../service/auth.service';
import { AppUserRegister } from '../../type';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';

@Component({
  selector: 'app-users-register',
  imports: [CommonModule, RouterModule, ReactiveFormsModule],
  templateUrl: './users-register.component.html',
  styleUrl: './users-register.component.scss',
  standalone: true
})
export class UsersRegisterComponent implements OnInit {
  registerForm: any;
  genderUserId: GenderRegister[] | null = null;
  constructor(private fb: FormBuilder, private authService: AuthService,private router: Router) { }


  

  ngOnInit(): void {

    this.authService.getGender();
    this.authService.currentappUserGenderProjection.subscribe((data) => {
      this.genderUserId = data;
      console.log("Genres récupérés :", this.genderUserId);
    });
    this.registerForm = this.fb.group({
      username: ['', [Validators.required, Validators.minLength(3)]],
      password: ['', [Validators.required, Validators.minLength(8), Validators.maxLength(20),
      Validators.pattern('^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^\\w\\s]).{8,20}$')
      ]],
      firstname: ['', [Validators.required]],
      lastname: ['', [Validators.required]],
      email: ['', [Validators.required, Validators.email]],
      address: this.fb.group({
        line1: ['', [Validators.required]],
        line2: [''],
        line3: [''],
        city: ['', [Validators.required]],
        postalCode: ['', [Validators.required]],
        createdAt: [new Date()]
      }),
      genderUserId: this.fb.group({
        id: [''],
        label: ['']
      })
    });
  }


  onRegister(): void {
    if (this.registerForm.valid) {
      this.authService.registerUser(this.registerForm.value).subscribe({
        next: (res) => {
          this.router.navigate(['/']); 
        },
        error: (err) => {
        }
      });
    } else {
      this.registerForm.markAllAsTouched(); 
    }
  }
  }

