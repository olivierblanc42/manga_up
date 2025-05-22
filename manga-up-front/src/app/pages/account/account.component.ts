import { Component, OnInit } from '@angular/core';
import { UserTest } from '../../type';
import { AppUserService } from '../../service/appUser.service';
import { LogoutComponent } from "../../components/logout/logout.component";

@Component({
  selector: 'app-account',
  imports: [LogoutComponent],
  templateUrl: './account.component.html',
  styleUrl: './account.component.scss'
})
export class AccountComponent implements OnInit {

  user: UserTest | null = null;

  constructor(
    private appUserService: AppUserService) { }


  ngOnInit(): void {

   this.appUserService.loadCurrentUser(); 
  this.appUserService.currentUserMe.subscribe((data) => {
      this.user = data;
      console.log('user récupérés avec succès :', data);
    } );  
  

  }

}
