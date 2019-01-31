import { Injectable } from '@angular/core';
import { Link } from '@/shared/models';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';

@Injectable({ providedIn: 'root' })
export class LinkService{

    API_URL = environment.apiUrl + '/space';

    constructor(private http: HttpClient) { }

    getAll() {
        return this.http.get<Link[]>(`${this.API_URL}/links`);
    }

    getById(id: number) {
        return this.http.get(`${this.API_URL}/links/${id}`);
    }

    add(link: Link) {
        return this.http.post(`${this.API_URL}/links`, link);
    }

    update(link: Link) {
        return this.http.put(`${this.API_URL}/links/${link.id}`, link);
    }

    delete(id: number) {
        return this.http.delete(`${this.API_URL}/links/${id}`);
    }
}