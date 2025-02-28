import { Component,CUSTOM_ELEMENTS_SCHEMA, OnInit } from '@angular/core';

@Component({
  selector: 'app-home',
  imports: [],
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss',
  schemas : [CUSTOM_ELEMENTS_SCHEMA],
  standalone: true
})

export class HomeComponent implements OnInit{


  ngOnInit(): void {
 

  }


}
