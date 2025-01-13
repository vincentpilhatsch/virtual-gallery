import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ManageArtworkRequestComponent } from './manage-artwork-request.component';

describe('ManageArtworkRequestComponent', () => {
  let component: ManageArtworkRequestComponent;
  let fixture: ComponentFixture<ManageArtworkRequestComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ManageArtworkRequestComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ManageArtworkRequestComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
