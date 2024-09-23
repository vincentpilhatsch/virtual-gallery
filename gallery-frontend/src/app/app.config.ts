import {ApplicationConfig} from '@angular/core';
import {provideRouter} from '@angular/router';

import {routes} from './app.routes';
import {provideClientHydration} from '@angular/platform-browser';
import {provideAnimationsAsync} from '@angular/platform-browser/animations/async';
import {HttpClient, provideHttpClient, withFetch} from "@angular/common/http";
import {HttpLoaderFactory} from "./app.component";
import {TranslateLoader, TranslateModule} from "@ngx-translate/core";

export const appConfig: ApplicationConfig = {
  providers: [
    provideRouter(routes),
    provideClientHydration(),
    provideAnimationsAsync(),
    provideHttpClient(withFetch())],
  TranslateModule.forRoot({  // Add TranslateModule configuration here
    loader: {
      provide: TranslateLoader,
      useFactory: HttpLoaderFactory,
      deps: [HttpClient]
    }
  }).providers  // Use the providers array from forRoot()
};
