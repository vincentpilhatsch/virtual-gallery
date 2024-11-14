import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {environment} from "../../environment";
import {Observable} from "rxjs";
import {User} from "../../user/model/user";
import {Event} from "../model/event";

@Injectable({
  providedIn: 'root'
})
export class EventService {
  readonly endpoint: string = `${environment.apiUrl}/event`;

  constructor(private httpClient: HttpClient) {
  }

  getEvents(): Observable<Event[]> {
    const url: string = `${this.endpoint}/all`;
    return this.httpClient.get<Event[]>(url);
  }
}
