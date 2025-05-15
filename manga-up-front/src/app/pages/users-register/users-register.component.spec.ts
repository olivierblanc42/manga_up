import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UsersRegisterComponent } from './users-register.component';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

describe('UsersRegisterComponent', () => {
  let component: UsersRegisterComponent;
  let fixture: ComponentFixture<UsersRegisterComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UsersRegisterComponent],
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

    fixture = TestBed.createComponent(UsersRegisterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
