import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AuteurComponent } from './auteur.component';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

describe('AuteurComponent', () => {
  let component: AuteurComponent;
  let fixture: ComponentFixture<AuteurComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AuteurComponent],
      providers: [
              provideHttpClient(), 
              provideHttpClientTesting(),     {
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

    fixture = TestBed.createComponent(AuteurComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
