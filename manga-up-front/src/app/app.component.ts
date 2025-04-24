import { Component, ElementRef, OnInit, viewChild } from '@angular/core';
import { NavigationEnd, RouterModule, RouterOutlet,Router } from '@angular/router';
import { BreadcrumbComponent, BreadcrumbItemDirective } from 'xng-breadcrumb';
import { CardComponent } from "./components/card/card.component";
import { AuthService } from './service/auth.service';
import { filter } from 'rxjs/operators';
@Component({
  selector: 'app-root',
  imports: [RouterOutlet, RouterModule, BreadcrumbComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss',
  standalone: true
})
export class AppComponent  implements OnInit {
  title = 'manga-up-front';
  sidenav = viewChild<ElementRef<HTMLElement>>('mySidenav');
  

  constructor(private router: Router) {
    this.router.events.pipe(
      filter(event => event instanceof NavigationEnd)
    ).subscribe((event: any) => {
      if (event.urlAfterRedirects === '/' || event.urlAfterRedirects === '/home') {
        document.body.classList.add('home-page');
      } else {
        document.body.classList.remove('home-page');
      }
    });
  }
    ngOnInit(): void {

  }

   openNav() {
    const element = this.sidenav();
    if (element) {
      element.nativeElement.style.width = '100%';
    }
  }
  
  closeNav() {
    const element = this.sidenav();
    if (element) {
      element.nativeElement.style.width = '0';
    }
  }

}


