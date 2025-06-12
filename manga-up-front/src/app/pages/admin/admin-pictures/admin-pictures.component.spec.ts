import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminPicturesComponent } from './admin-pictures.component';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

describe('AdminPicturesComponent', () => {
  let component: AdminPicturesComponent;
  let fixture: ComponentFixture<AdminPicturesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AdminPicturesComponent],
                  providers: [
                    provideHttpClient(),
                    provideHttpClientTesting(),
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
                    }]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AdminPicturesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
