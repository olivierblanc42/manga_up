import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../service/auth.service';
import { NgIf } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  standalone: true,
  imports: [NgIf, FormsModule],
})
export class LoginComponent {
  credentials = { username: '', password: '' };
  errorMessage: string = '';

  constructor(private authService: AuthService, private router: Router) { }

  login(): void {
    this.authService.login(this.credentials).subscribe({
      next: (response) => {
        this.authService.saveToken(response.token); 
        console.log('Token JWT enregistré :', response.token); 
        console.log('Utilisateur connecté :', this.authService.isAuthenticated());
        console.log('Identifiants de l\'utilisateur :', this.credentials); 
        this.router.navigate(['/']); 
      },
      error: () => {
        this.errorMessage = 'Échec de la connexion. Vérifiez vos identifiants.';
      },
    });
  }
}