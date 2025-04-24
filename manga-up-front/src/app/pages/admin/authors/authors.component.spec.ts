import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AuthorsAdminComponent } from './authors.component';

describe('AuthorsAdminComponent', () => {
  let component: AuthorsAdminComponent;
  let fixture: ComponentFixture<AuthorsAdminComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AuthorsAdminComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AuthorsAdminComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
