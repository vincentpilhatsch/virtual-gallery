import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ManageArtworksComponent } from './manage-artworks.component';

describe('ManageArtworksComponent', () => {
  let component: ManageArtworksComponent;
  let fixture: ComponentFixture<ManageArtworksComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ManageArtworksComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ManageArtworksComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
