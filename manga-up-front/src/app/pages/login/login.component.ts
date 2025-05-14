import { Component } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../../service/auth.service';
import { NgIf } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  standalone: true,
  imports: [NgIf, FormsModule, RouterModule],
  styleUrl: './login.component.scss'
})
export class LoginComponent {
  credentials = { username: '', password: '' };
  errorMessage: string = '';

  constructor(private authService: AuthService, private router: Router) { }

  login(): void {
    this.authService.login(this.credentials).subscribe({
      next: (response) => {
        console.log('Utilisateur connecté avec succès :');
        this.router.navigate(['/']);  // Redirection après un login réussi
      },
      error: () => {
        this.errorMessage = 'Échec de la connexion. Vérifiez vos identifiants.';
      },
    });
  }


loginTest(): void{
  this.authService.loginAfterLogout(this.credentials).subscribe({
    next: (res) => {
      console.log('Connecté avec succès', res);
      this.router.navigate(['/']); 
    },
    error: (err) => {
      console.error('Erreur lors du login :', err);
    }
  });
}

  
}
