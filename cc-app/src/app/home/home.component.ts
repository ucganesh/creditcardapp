import { Component } from '@angular/core';
import { first } from 'rxjs/operators';

import { Identity, Creditcard } from '@app/_models';
import { CreditcardService, AuthenticationService } from '@app/_services';

@Component({ templateUrl: 'home.component.html' })
export class HomeComponent {
    loading = false;
    identity: Identity;

    constructor(
        private cardService: CreditcardService,
        private authenticationService: AuthenticationService
    ) {
        this.identity = this.authenticationService.identityValue;
    }

    ngOnInit() {

    }
}
