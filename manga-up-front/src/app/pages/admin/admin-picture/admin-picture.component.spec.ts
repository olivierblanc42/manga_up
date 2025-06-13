import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminPictureComponent } from './admin-picture.component';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

describe('AdminPictureComponent', () => {
  let component: AdminPictureComponent;
  let fixture: ComponentFixture<AdminPictureComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AdminPictureComponent],
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
              }
            ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AdminPictureComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
