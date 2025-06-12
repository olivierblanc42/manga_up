import { Component, OnInit } from '@angular/core';
import { Manga, UserTest } from '../../type';
import { AppUserService } from '../../service/appUser.service';
import { LogoutComponent } from "../../components/logout/logout.component";
import { CardComponent } from '../../components/card/card.component';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-account',
  imports: [LogoutComponent, CardComponent, RouterModule],
  templateUrl: './account.component.html',
  styleUrl: './account.component.scss'
})
export class AccountComponent implements OnInit {

  user: UserTest | null = null;
  mangas: Manga[] = [];
  constructor(
    private appUserService: AppUserService) { }


  ngOnInit(): void {




   this.appUserService.loadCurrentUser(); 
  this.appUserService.currentUserMe.subscribe((data) => {
      this.user = data;
      this.mangas = this.user?.mangas ?? [];

      console.log('user récupérés avec succès :', data);
    } );  
  
    if (this.mangas?.length) {
      this.mangas = this.mangas;
    } else {
      this.mangas = [];
    }
  }

}
