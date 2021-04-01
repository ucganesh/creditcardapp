import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { environment } from '@environments/environment';
import { Identity, Role } from '@app/_models';

@Injectable({ providedIn: 'root' })
export class AuthenticationService {
    private identitySubject: BehaviorSubject<Identity>;
    public identity: Observable<Identity>;

    constructor(
        private router: Router,
        private http: HttpClient
    ) {
        this.identitySubject = new BehaviorSubject<Identity>(JSON.parse(localStorage.getItem('user')));
        this.identity = this.identitySubject.asObservable();
    }

    public get identityValue(): Identity {
        return this.identitySubject.value;
    }

    public get isAdmin() {
        return this.identityValue && this.identityValue.roles.includes(Role.Admin) ;
    }

    login(username: string, password: string) {
        return this.http.post<any>(`${environment.apiUrl}/authenticate`, { username, password })
            .pipe(map(identity => {
                // store identity details and jwt token in local storage to keep user logged in between page refreshes
                localStorage.setItem('identity', JSON.stringify(identity));
                this.identitySubject.next(identity);
                return identity;
            }));
    }

    logout() {
        // remove identity from local storage to log user out
        localStorage.removeItem('identity');
        this.identitySubject.next(null);
        this.router.navigate(['/login']);
    }
}
