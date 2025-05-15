import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AccountComponent } from './account.component';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';
import { AuthService } from '../../service/auth.service';

describe('AccountComponent', () => {
  let component: AccountComponent;
  let fixture: ComponentFixture<AccountComponent>;
  let authorService: Partial<AuthService>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AccountComponent],
         providers: [
              provideHttpClient(),
              provideHttpClientTesting(),
              // { provide: authorService, useValue: authorService },
              {
                provide: ActivatedRoute,
                useValue: {
                  snapshot: {
                    paramMap: {
                      get: (key: string) => '123',
                    },
                  },
                  params: of({ id: '123' }),
                },
              }
            ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AccountComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
