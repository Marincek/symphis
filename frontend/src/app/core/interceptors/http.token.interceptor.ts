import { Injectable } from '@angular/core';
import { HttpRequest, HttpHandler, HttpHeaders, HttpEvent, HttpInterceptor } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { AuthenticationService } from '@/core/authentication';

@Injectable()
export class TokenInterceptor implements HttpInterceptor {
    constructor(private authenticationService: AuthenticationService) {}

    intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        // add authorization header with token if available
        let currentUser = this.authenticationService.currentUserValue;
        console.log("TokenInterceptor - url : " + request.url);
        if (request.url.startsWith(`${environment.apiUrl}`) && currentUser && currentUser.token) {
            request = request.clone({
                setHeaders: {
                  'x-auth-token': `${currentUser.token}`
                }
              });
        }

        return next.handle(request);
    }
}