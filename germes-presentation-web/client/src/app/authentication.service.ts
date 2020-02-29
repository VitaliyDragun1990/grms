import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { JwtHelperService } from '@auth0/angular-jwt';
import { BASE_API_URL } from './core';
import { LoginDTO } from './login-dto';
import { map } from 'rxjs/operators';
import { SHA256 } from 'crypto-js';

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {

  private userName: string;

  constructor(private jwtService: JwtHelperService, private http: HttpClient) {
    const jwtToken = jwtLoader();
    if (jwtToken) {
      this.changeUser(jwtToken, false);
    } else {
      this.login({userName: 'guest', hashedPassword: SHA256('guest').toString()});
    }
   }

  getUserName(): string {
    return this.userName;
  }

  changeUser(jwtToken: string, persist: boolean): void {
    const tokenJson: object = this.jwtService.decodeToken(jwtToken);
    this.userName = tokenJson['sub'];
    if (persist) {
      localStorage.setItem('jwt_token', jwtToken);
    }
  }

  login(loginDTO: LoginDTO): void {
    this.http.post<any>(`${BASE_API_URL}user/api/login`, loginDTO, { observe: 'response'})
    .pipe(map(response => response.headers.get('Authorization')), map(header => header.replace('Bearer ', '')))
    .subscribe(token => this.changeUser(token, true));
  }
}

export function jwtLoader(): string {
  return localStorage.getItem('jwt_token');
}
