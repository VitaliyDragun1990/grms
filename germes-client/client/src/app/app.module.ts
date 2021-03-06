import { CityService } from './city.service';
import { TranslateModule, TranslateService, TranslateLoader } from '@ngx-translate/core';
import { BrowserModule } from '@angular/platform-browser';
import { JwtModule } from '@auth0/angular-jwt';
import { NgModule } from '@angular/core';

import { AppComponent } from './app.component';
import { CitiesComponent } from './cities/cities.component';
import { LanguageComponent } from './language/language.component';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';
import { HttpClientModule, HttpClient } from '@angular/common/http';
import { jwtLoader, AuthenticationService } from './authentication.service';


@NgModule({
  declarations: [
    AppComponent,
    CitiesComponent,
    LanguageComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    TranslateModule.forRoot({
      loader: {
        provide: TranslateLoader,
        useFactory: HttpLoaderFactory,
        deps: [HttpClient]
      }
    }),
    JwtModule.forRoot({
      config: {
        whitelistedDomains: ['192.168.99.100:8040', 'localhost:8040'],
        tokenGetter: jwtLoader
      }
    })
  ],
  providers: [TranslateService, CityService, AuthenticationService],
  bootstrap: [AppComponent]
})
export class AppModule { }

export function HttpLoaderFactory(http: HttpClient) {
  return new TranslateHttpLoader(http);
}
