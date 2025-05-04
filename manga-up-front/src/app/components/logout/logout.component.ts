import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../service/auth.service';

@Component({
  selector: 'app-logout',
  template: `<button (click)="logout()">Se déconnecter</button>`,
})
export class LogoutComponent {
  constructor(private authService: AuthService, private router: Router) { }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']); // Redirige vers la page de login
  }
}
