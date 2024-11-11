import {HttpInterceptorFn} from '@angular/common/http';

const excludedUrls = ["assets/i18n"]; // "user/login"

export const jwtInterceptor: HttpInterceptorFn = (req, next) => {
  // Check if the request URL includes any of the excluded URLs
  const shouldExclude = excludedUrls.some(url => req.url.includes(url));
  if (!shouldExclude) {
    const clonedRequest = req.clone({withCredentials: true});
    return next(clonedRequest);
  }
  return next(req);
};
