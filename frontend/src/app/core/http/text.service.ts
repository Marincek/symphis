import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { BehaviorSubject, Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { error } from '@angular/compiler/src/util';

@Injectable({ providedIn: 'root' })
export class TextService {

    // proxy with CORS *
    proxyUrl = "https://cors-anywhere.herokuapp.com/";

    httpOptions = {
        headers: new HttpHeaders({ 'X-Requested-With': 'XMLHttpRequest', 'Content-Type': 'text/html', 'Accept': 'text/html' }),
        responseType: 'text' as 'text'
    };

    constructor(private http: HttpClient) {}

    getContent(url: string) {
        return this.http.get(this.proxyUrl + url, this.httpOptions);
    }

}