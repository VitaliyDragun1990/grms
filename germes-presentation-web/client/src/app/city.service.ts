import { City } from './city';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable()
export class CityService {

  constructor(private http: HttpClient) { }

  getCities(): Observable<Array<City>> {
    return this.http.get<Array<City>>('http://192.168.99.100:8090/api/cities');
  }

}
