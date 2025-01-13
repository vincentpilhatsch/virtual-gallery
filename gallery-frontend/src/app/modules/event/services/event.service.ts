import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {environment} from "../../environment";
import {Observable} from "rxjs";
import {User} from "../../user/model/user";
import {Event} from "../model/event";
import {Exhibition} from "../../exhibitions/model/exhibition";

@Injectable({
  providedIn: 'root'
})
export class EventService {
  readonly endpoint: string = `${environment.apiUrl}/event`;

  constructor(private httpClient: HttpClient) {
  }

  getEventById(id: number): Observable<Event> {
    const url: string = `${this.endpoint}/${id}`;
    return this.httpClient.get<Event>(url);
  }

  getEvents(): Observable<Event[]> {
    const url: string = `${this.endpoint}/all`;
    return this.httpClient.get<Event[]>(url);
  }

  createEvent(event: Event, file: File): Observable<Event> {
    const formData: FormData = new FormData();
    const eventBlob = new Blob([JSON.stringify(event)], {type: 'application/json'});
    formData.append('event', eventBlob);
    formData.append('file', file);
    const url: string = `${this.endpoint}`;
    return this.httpClient.post<Event>(url, formData);
  }

  updateEvent(event: Event): Observable<Event> {
    const url: string = `${this.endpoint}/update`;
    return this.httpClient.put<Event>(url, event);
  }

  updateEventWithImage(event: Event, file: File): Observable<Event> {
    const formData: FormData = new FormData();
    const exhibitionBlob = new Blob([JSON.stringify(event)], {type: 'application/json'});
    formData.append('event', exhibitionBlob);
    formData.append('file', file);
    const url: string = `${this.endpoint}/updateWithImage`;
    return this.httpClient.put<Event>(url, formData);
  }

  deleteEvent(id: number): Observable<void> {
    const url: string = `${this.endpoint}/${id}`;
    return this.httpClient.delete<void>(url);
  }
}
