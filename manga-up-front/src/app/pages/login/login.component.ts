import { Component, OnInit } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../../service/auth.service';
import { NgIf } from '@angular/common';
import { FormsModule } from '@angular/forms';
declare global {
  interface Window {
    onCaptchaSuccess: (token: string) => void;
  }
}

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  standalone: true,
  imports: [NgIf, FormsModule, RouterModule],
  styleUrl: './login.component.scss'
})
export class LoginComponent implements OnInit {
  credentials = { username: '', password: '' };
  errorMessage: string = '';
  captchaToken: string = '';

  constructor(private authService: AuthService, private router: Router) { }

  ngOnInit(): void {
    window.onCaptchaSuccess = (token: string) => {
      this.captchaToken = token;
      console.log('hCaptcha token reçu :', token);
    };
  }

  login(): void {
    if (!this.captchaToken) {
      this.errorMessage = 'Veuillez valider le captcha avant de continuer.';
      return;
    }

    this.authService.login(this.credentials).subscribe({
      next: () => {
        console.log('Utilisateur connecté avec succès');
        this.router.navigate(['/']);
      },
      error: () => {
        this.errorMessage = 'Échec de la connexion. Vérifiez vos identifiants.';
      },
    });
  }

  loginTest(): void {
  

    const loginData = {
      ...this.credentials,
      captchaToken: this.captchaToken
    };

    this.authService.loginAfterLogout(loginData).subscribe({
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
