import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { NavigationEnd, Router } from '@angular/router';
import { filter } from 'rxjs/operators';
import { BreadcrumbComponent } from 'xng-breadcrumb';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { RouterModule, RouterOutlet } from '@angular/router';
import { AuthService } from './service/auth.service';
import { CsrfService } from './service/csrf.service';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, RouterModule, BreadcrumbComponent, CommonModule, ReactiveFormsModule, FormsModule],
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'], 
  standalone: true
})
export class AppComponent implements OnInit {
  title = 'manga-up-front';

  @ViewChild('mySidenav') sidenav!: ElementRef<HTMLElement>;

  isAdmin: boolean = false;
  searchTerm = '';

  showBreadcrumb = true;  

  constructor(private router: Router, private authService: AuthService, private csrfService: CsrfService) {
    this.router.events.pipe(
      filter(event => event instanceof NavigationEnd)
    ).subscribe((event: NavigationEnd) => {
      if (event.urlAfterRedirects === '/' || event.urlAfterRedirects === '/home') {
        document.body.classList.add('home-page');
      } else {
        document.body.classList.remove('home-page');
      }

      // Mettre à jour la visibilité du breadcrumb
      this.showBreadcrumb = !event.urlAfterRedirects.startsWith('/manga/');
    });
  }

  ngOnInit(): void {
    this.csrfService.fetchCsrfToken();
    this.csrfService.currentCsrfProjection.subscribe(token => {
      console.log('Token CSRF actuel:', token);
    });

    this.authService.userRole$.subscribe(role => {
      this.isAdmin = role === 'ROLE_ADMIN';
    });
  }

  openNav() {
    this.sidenav.nativeElement.style.width = '100%';
  }

  closeNav() {
    this.sidenav.nativeElement.style.width = '0';
  }

  redirect() {
    if (this.searchTerm.trim()) {
      this.router.navigate(['/search', this.searchTerm.trim().toLowerCase()]);
    }
  }



  warning(e: Event){
    e.preventDefault();
    alert('Cette fonctionnalité est en cours de construction. Veuillez patienter.');
  }

}
