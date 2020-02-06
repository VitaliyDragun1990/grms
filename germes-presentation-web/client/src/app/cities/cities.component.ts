import { CityService } from './../route.service';
import { Component, OnInit } from '@angular/core';
import { City } from '../route';

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

  isRegionCenter(route: City): boolean {
    if (route.district) {
      return false;
    }
    return true;
  }

}
