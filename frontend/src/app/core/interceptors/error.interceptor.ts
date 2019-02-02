import { Injectable } from '@angular/core';
import { HttpRequest, HttpHandler, HttpEvent, HttpInterceptor } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';

import { AuthenticationService } from '@/core/authentication';

@Injectable()
export class ErrorInterceptor implements HttpInterceptor {
    constructor(private authenticationService: AuthenticationService) {}

    // BACKEND ERROR FORMAT
    // {
    //     "status": "BAD_REQUEST",
    //     "message": "Validation failed for argument [1] in public org.springframework.http.ResponseEntity<com.marincek.sympis.controllers.response.LinkResponse> com.marincek.sympis.controllers.LinksController.addLink(java.security.Principal,com.marincek.sympis.controllers.request.LinkRequest): [Field error in object 'linkRequest' on field 'tags': rejected value [[tag1, tag2, <div>, tag4, tag5]]; codes [TagConstraint.linkRequest.tags,TagConstraint.tags,TagConstraint.java.util.List,TagConstraint]; arguments [org.springframework.context.support.DefaultMessageSourceResolvable: codes [linkRequest.tags,tags]; arguments []; default message [tags]]; default message [No HTML tags allowed]] ",
    //     "errors": [
    //         "No HTML tags allowed"
    //     ]
    // }

    intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        return next.handle(request).pipe(catchError(err => {
            if (err.status === 401 || err.status === 403) {
                // auto logout if 401 response returned from api
                this.authenticationService.logout();
                location.reload(true);
            }
            
            // const error = err.errors || err.error.text || err.statusText;

            return throwError(err.error.errors[0]);
        }))
    }
}