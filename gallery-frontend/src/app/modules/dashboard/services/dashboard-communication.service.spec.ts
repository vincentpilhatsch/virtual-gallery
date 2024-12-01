import {TestBed} from '@angular/core/testing';

import {DashboardCommunicationService} from './dashboard-communication.service';

describe('ArtworkCommunicationService', () => {
  let service: DashboardCommunicationService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DashboardCommunicationService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
