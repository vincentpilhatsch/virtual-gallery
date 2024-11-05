import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class JsonReaderService {
  deTranslation: any;

  constructor(private http: HttpClient) {
    this.loadTranslations();
  }

  loadTranslations(): void {
    this.http.get('/assets/i18n/de.json').subscribe(data => {
      this.deTranslation = data;
    });
  }
}
