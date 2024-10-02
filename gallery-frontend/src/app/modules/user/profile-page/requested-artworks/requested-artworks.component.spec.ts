import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RequestedArtworksComponent } from './requested-artworks.component';

describe('RequestedArtworksComponent', () => {
  let component: RequestedArtworksComponent;
  let fixture: ComponentFixture<RequestedArtworksComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RequestedArtworksComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(RequestedArtworksComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
