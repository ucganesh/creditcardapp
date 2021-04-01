import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { environment } from '@environments/environment';
import { Creditcard, CreditcardPage } from '@app/_models';

@Injectable({ providedIn: 'root' })
export class CreditcardService {
    constructor(private http: HttpClient) { }

    getAll() {
        return this.http.get<Creditcard[]>(`${environment.apiUrl}/creditcards`);
    }

    getById(id: number) {
        return this.http.get<Creditcard>(`${environment.apiUrl}/creditcards/${id}`);
    }

    getAllByPage(page:number, size:number) {
        return this.http.get<CreditcardPage>(`${environment.apiUrl}/creditcards?page=${page}&size=${size}`);
    }

    addCard(card: Creditcard) {
        return this.http.post(`${environment.apiUrl}/creditcards`, card);
    }

    update(id:number, card: Creditcard) {
      return this.http.put(`${environment.apiUrl}/creditcards/${id}`, card);
    }

    delete(id: number) {
        return this.http.delete(`${environment.apiUrl}/creditcards/${id}`);
    }

    search(term: number) {
      return this.http.get<CreditcardPage>(`${environment.apiUrl}/creditcards/number/${term}`);
    }
}
