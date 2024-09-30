import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RequestArtworkComponent } from './request-artwork.component';

describe('RequestArtworkComponent', () => {
  let component: RequestArtworkComponent;
  let fixture: ComponentFixture<RequestArtworkComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RequestArtworkComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(RequestArtworkComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
