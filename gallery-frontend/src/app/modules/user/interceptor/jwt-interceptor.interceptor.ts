import {HttpInterceptorFn} from '@angular/common/http';

const excludedUrls = ["user/login", "assets/i18n"];

export const jwtInterceptor: HttpInterceptorFn = (req, next) => {
  // Check if the request URL includes any of the excluded URLs
  const shouldExclude = excludedUrls.some(url => req.url.includes(url));
  if (!shouldExclude) {
    const clonedRequest = req.clone({withCredentials: true});
    return next(clonedRequest);
  }
  return next(req);
};
