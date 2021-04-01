import { Component, OnInit } from '@angular/core';
import { first } from 'rxjs/operators';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

import { Identity, Creditcard } from '@app/_models';
import { CreditcardService, AuthenticationService } from '@app/_services';

@Component({ templateUrl: 'list.component.html' })
export class ListComponent implements OnInit {
    form: FormGroup;
    loading = false;
    identity: Identity;
    cards = null;
    searchTerm: string;
    page = 0;
    pageSize = 10;
    collectionSize: number;
    isAdmin : boolean;
    submitted = false;

    constructor(
        private formBuilder: FormBuilder,
        private cardService: CreditcardService,
        private authenticationService: AuthenticationService
    ) {
        this.identity = this.authenticationService.identityValue;
        this.isAdmin = this.authenticationService.isAdmin;

    }

    ngOnInit() {
        console.log(this.isAdmin);
        this.form = this.formBuilder.group({
          searchTerm: ['', Validators.required]
        });
        this.getCardsForPage(this.page);
    }

    // convenience getter for easy access to form fields
    get f() { return this.form.controls; }

    getCardsForPage(selection) {
         this.loading = true;
         this.cardService.getAllByPage(selection, this.pageSize).pipe(first()).subscribe(response => {
             this.loading = false;
             this.cards = response.content;
             this.page = response.pageNumber;
             this.pageSize = response.size;
             this.collectionSize = response.totalSize;
         });
    }

    deleteCard(id: number) {
        const card = this.cards.find(x => x.id === id);
        card.isDeleting = true;
        this.cardService.delete(id)
            .pipe(first())
            .subscribe(() => {});
    }


    onSearch() {
        this.submitted = true;
        console.log(this.form);

        // stop here if form is invalid
        if (this.form.invalid) {
            return;
        }

        this.loading = true;
         this.cardService.search(this.form.value.searchTerm).pipe(first()).subscribe(response => {
             this.loading = false;
             this.cards = response.content;
             this.page = response.pageNumber;
             this.pageSize = response.size;
             this.collectionSize = response.totalSize;
         });
    }
}
