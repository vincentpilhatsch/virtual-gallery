import {TestBed} from '@angular/core/testing';

import {DashboardDataService} from './dashboard-data.service';

describe('ArtworkCommunicationService', () => {
    let service: DashboardDataService;

    beforeEach(() => {
        TestBed.configureTestingModule({});
        service = TestBed.inject(DashboardDataService);
    });

    it('should be created', () => {
        expect(service).toBeTruthy();
    });
});
