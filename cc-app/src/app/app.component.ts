import { Component } from '@angular/core';
import { Router } from '@angular/router';

import { AuthenticationService } from './_services';
import { Identity, Role } from './_models';

@Component({ selector: 'app', templateUrl: 'app.component.html' })
export class AppComponent {
    identity: Identity;

    constructor(private authenticationService: AuthenticationService) {
        this.authenticationService.identity.subscribe(x => this.identity = x);
    }

    get isAdmin() {
        return this.identity && this.identity.roles.includes(Role.Admin) ;
    }

    logout() {
        this.authenticationService.logout();
    }
}
