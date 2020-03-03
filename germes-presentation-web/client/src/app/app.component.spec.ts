import { LoginDTO } from './login-dto';
import { AuthenticationService, jwtLoader } from './authentication.service';
import { JwtModule } from '@auth0/angular-jwt';
import { CityService } from './city.service';
import { TranslateModule, TranslateService } from '@ngx-translate/core';
import { LanguageComponent } from './language/language.component';
import { CitiesComponent } from './cities/cities.component';
import { TestBed, async } from '@angular/core/testing';
import { AppComponent } from './app.component';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { BASE_API_URL } from './core';

class MockAuthenticationService {

  getUserName(): string {
    return 'test';
  }

  login(loginDTO: LoginDTO) {
    // do nothhing
  }
}

describe('AppComponent', () => {

  let httpController: HttpTestingController;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [
        AppComponent, CitiesComponent, LanguageComponent
      ],
      imports: [
        TranslateModule.forRoot(),
        HttpClientTestingModule,
        JwtModule.forRoot({
          config: {
            whitelistedDomains: ['192.168.99.100:8040', 'localhost:8040'],
            tokenGetter: jwtLoader
          }
        })
      ],
      providers: [
        TranslateService,
        CityService,
        { provide: AuthenticationService, useClass: MockAuthenticationService }]
    }).compileComponents();

    httpController = TestBed.get(HttpTestingController);
  }));

  afterEach(() => {
    httpController.verify();
  });

  it('should create the app', async(() => {
    const fixture = TestBed.createComponent(AppComponent);
    const app = fixture.debugElement.componentInstance;

    // const authRequest = httpController.expectOne(`${BASE_API_URL}user/api/login`);
    const cityRequest = httpController.expectOne(`${BASE_API_URL}geography/api/cities`);
    expect(app).toBeTruthy();

    // authRequest.flush({ body: 'test', opts: { headers: { 'Authorization': 'test' } } });
    cityRequest.flush({});
  }));
});
