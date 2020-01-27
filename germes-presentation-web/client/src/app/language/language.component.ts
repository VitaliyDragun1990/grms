import { Component, OnInit } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';

@Component({
  selector: 'app-language',
  templateUrl: './language.component.html',
  styleUrls: ['./language.component.css']
})
export class LanguageComponent implements OnInit {

  constructor(private translateService: TranslateService) {
    this.translateService.setDefaultLang('en');
   }

  ngOnInit() {
  }

  changeLanguage(lang: string): void {
    this.translateService.use(lang);
  }

}
