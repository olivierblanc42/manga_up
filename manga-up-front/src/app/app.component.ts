import { Component, ElementRef, OnInit, viewChild } from '@angular/core';
import { RouterModule, RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, RouterModule],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss',
  standalone: true
})
export class AppComponent  implements OnInit {
  title = 'manga-up-front';
  sidenav = viewChild<ElementRef<HTMLElement>>('mySidenav');
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


