import { HttpTestingController, HttpClientTestingModule } from '@angular/common/http/testing';
import { HttpClientModule } from '@angular/common/http';
import { TestBed, inject } from '@angular/core/testing';

import { CityService } from './route.service';

describe('CityService', () => {

  let httpController: HttpTestingController;
  let cityService: CityService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [CityService]
    });

    httpController = TestBed.get(HttpTestingController);
    cityService = TestBed.get(CityService);
  });

  afterEach(() => {
    httpController.verify();
  });

  it('should be created', () => {
    expect(cityService).toBeTruthy();
  });

  it('service should return two cities', () => {
    const dummyResponse = [{ name: 'Odessa', region: 'Odessa', district: '' },
    { name: 'Kyiv', region: 'Kyiv', district: '' }];

    cityService.getCities().subscribe(cities => {
      expect(cities.length).toBe(2);
      expect(cities).toEqual(dummyResponse);
    });

    let cityRequest = httpController.expectOne('api/cities');
    cityRequest.flush(dummyResponse);
  });
});
