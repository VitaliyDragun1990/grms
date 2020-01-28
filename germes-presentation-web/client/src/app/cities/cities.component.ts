import { CityService } from './../city.service';
import { Component, OnInit } from '@angular/core';
import { City } from '../city';

@Component({
  selector: 'app-cities',
  templateUrl: './cities.component.html',
  styleUrls: ['./cities.component.css']
})
export class CitiesComponent implements OnInit {

  rowsPerPage = 10;
  cities: City[];

  constructor(cityService: CityService) {
    cityService.getCities().subscribe(res => this.cities = res);
   }

  ngOnInit() {
  }

  isRegionCenter(city: City): boolean {
    if (city.district) {
      return false;
    }
    return true;
  }

}
