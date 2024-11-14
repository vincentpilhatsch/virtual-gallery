import {Pipe, PipeTransform} from '@angular/core';
import {HttpErrorResponse} from "@angular/common/http";
import {TranslateService} from "@ngx-translate/core";
import {ErrorResponse} from "../model/ErrorResponse";

@Pipe({
  name: 'errorMessageHandler',
  standalone: true
})
export class ErrorMessageHandlerPipe implements PipeTransform {
  private readonly defaultErrorMessage: string = "error.default.message"
  private readonly backendErrorMessages: { [key: string]: string } = {
    "Incorrect email or password": "error.incorrect.email.or.password",
  };

  constructor(private translate: TranslateService) {
  }


  transform(error: HttpErrorResponse | null): string {
    if (!error) {
      return "";
    }
    const errorResponse: ErrorResponse = error.error || {};
    const message: string = errorResponse.message || this.defaultErrorMessage;
    return this.translate.instant(this.backendErrorMessages[message]);
  }

}
