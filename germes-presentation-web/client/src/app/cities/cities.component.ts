import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-cities',
  templateUrl: './cities.component.html',
  styleUrls: ['./cities.component.css']
})
export class CitiesComponent implements OnInit {

  rowsPerPage = 10;
  cities: any;

  constructor() { }

  ngOnInit() {
  }

  isRegionCenter(city: any): boolean {
    if (city.district) {
      return false;
    }
    return true;
  }

}
