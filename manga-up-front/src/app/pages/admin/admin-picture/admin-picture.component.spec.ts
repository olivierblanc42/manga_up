import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminPictureComponent } from './admin-picture.component';

describe('AdminPictureComponent', () => {
  let component: AdminPictureComponent;
  let fixture: ComponentFixture<AdminPictureComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AdminPictureComponent]
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
