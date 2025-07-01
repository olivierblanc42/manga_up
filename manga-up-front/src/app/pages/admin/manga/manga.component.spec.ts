import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MangaComponent } from './manga.component';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

describe('MangaComponent', () => {
  let component: MangaComponent;
  let fixture: ComponentFixture<MangaComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MangaComponent],
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

    fixture = TestBed.createComponent(MangaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
