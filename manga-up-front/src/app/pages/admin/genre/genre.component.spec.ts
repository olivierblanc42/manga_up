import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GenreAdminComponent } from './genre.component';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

describe('GenreAdminComponent', () => {
  let component: GenreAdminComponent;
  let fixture: ComponentFixture<GenreAdminComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [GenreAdminComponent],
      providers: [
        provideHttpClient(),
        provideHttpClientTesting(), {
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

    fixture = TestBed.createComponent(GenreAdminComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
