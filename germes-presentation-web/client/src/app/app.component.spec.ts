import { CityService } from './route.service';
import { TranslateModule, TranslateService } from '@ngx-translate/core';
import { LanguageComponent } from './language/language.component';
import { CitiesComponent } from './cities/cities.component';
import { TestBed, async } from '@angular/core/testing';
import { AppComponent } from './app.component';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

describe('AppComponent', () => {

  let httpController: HttpTestingController;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [
        AppComponent, CitiesComponent, LanguageComponent
      ],
      imports: [TranslateModule.forRoot(), HttpClientTestingModule],
      providers: [TranslateService, CityService]
    }).compileComponents();

    httpController = TestBed.get(HttpTestingController);
  }));

  afterEach(() => {
    httpController.verify();
  });

  it('should create the app', async(() => {
    const fixture = TestBed.createComponent(AppComponent);
    const app = fixture.debugElement.componentInstance;

    let cityRequest =httpController.expectOne('api/cities');
    expect(app).toBeTruthy();

    cityRequest.flush({});
  }));
});
