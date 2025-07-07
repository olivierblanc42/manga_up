import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MangaAdminComponent } from './manga.component';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

describe('MangaAdminComponent', () => {
  let component: MangaAdminComponent;
  let fixture: ComponentFixture<MangaAdminComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MangaAdminComponent],
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

    fixture = TestBed.createComponent(MangaAdminComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
