import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ArtworkExtendedCardComponent } from './artwork-extended-card.component';

describe('ArtworkCardComponent', () => {
  let component: ArtworkExtendedCardComponent;
  let fixture: ComponentFixture<ArtworkExtendedCardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ArtworkExtendedCardComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ArtworkExtendedCardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
