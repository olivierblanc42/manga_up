import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminPicturesComponent } from './admin-pictures.component';

describe('AdminPicturesComponent', () => {
  let component: AdminPicturesComponent;
  let fixture: ComponentFixture<AdminPicturesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AdminPicturesComponent]
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
