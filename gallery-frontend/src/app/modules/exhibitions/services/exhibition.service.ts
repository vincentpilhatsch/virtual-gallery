import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {environment} from "../../environment";
import {Observable} from "rxjs";
import {Exhibition} from "../model/exhibition";

@Injectable({
  providedIn: 'root'
})
export class ExhibitionService {
  readonly endpoint: string = `${environment.apiUrl}/exhibition`;

  constructor(private readonly httpClient: HttpClient) {
  }

  getExhibitions(): Observable<Exhibition[]> {
    const url: string = `${this.endpoint}/all`;
    return this.httpClient.get<Exhibition[]>(url);
  }

  getLatestExhibitions(): Observable<Exhibition> {
    const url: string = `${this.endpoint}/latest`;
    return this.httpClient.get<Exhibition>(url);
  }
}
