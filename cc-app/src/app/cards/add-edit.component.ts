import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { first } from 'rxjs/operators';
import {RxwebValidators } from "@rxweb/reactive-form-validators"

import { CreditcardService, AlertService } from '@app/_services';

@Component({ templateUrl: 'add-edit.component.html' })
export class AddEditComponent implements OnInit {
    form: FormGroup;
    id: number;
    isAddMode: boolean;
    loading = false;
    submitted = false;
    currentYear: number;
    currentMonth: number;

      creditCardTypes = [
        "Visa",

        "AmericanExpress",

        "Maestro",

        "JCB",

        "Discover",

        "DinersClub",

        "MasterCard"
    ];
     months = [1,2,3,4,5,6,7,8,9,10,11,12];
     years= [2021, 2022, 2023, 2024, 2025, 2026, 2027, 2028, 2029, 2030];

    constructor(
        private formBuilder: FormBuilder,
        private route: ActivatedRoute,
        private router: Router,
        private creditcardService: CreditcardService,
        private alertService: AlertService
    ) {}

    ngOnInit() {
        this.id = this.route.snapshot.params['id'];
        this.isAddMode = !this.id;
        this.currentYear = new Date().getFullYear();
        this.currentMonth = new Date().getMonth() + 1;

        if (this.isAddMode) {
          this.form = this.formBuilder.group({
                    expiryMonth: [1, Validators.required],
                    expiryYear: [2021, Validators.required],
                    type: ['Visa', Validators.required],
                    number: ['', RxwebValidators.creditCard ({fieldName:'type'})],
                    expiryDate: [''],
                    name: ['', Validators.compose([Validators.required, Validators.minLength(2)])]
                  });
        } else {
          this.form = this.formBuilder.group({
                    expiryMonth: [1, Validators.required],
                    expiryYear: [2021, Validators.required],
                    type: ['Visa', Validators.required],
                    number: ['',Validators.required],
                    expiryDate: [''],
                    name: ['', Validators.compose([Validators.required, Validators.minLength(2)])]
                  });
        }


        if (!this.isAddMode) {
            this.creditcardService.getById(this.id)
                .pipe(first())
                .subscribe(x => {
                var obj = {
                  name: x.name,
                  number: x.number,
                  expiryMonth: new Date(x.expiryDate).getMonth()+1,
                  expiryYear: new Date(x.expiryDate).getFullYear(),
                  type:x.type,
                  expiryDate: x.expiryDate
                };
                console.log(obj);
                this.form.patchValue(obj);
                });
        }
    }

    // convenience getter for easy access to form fields
    get f() { return this.form.controls; }

    onSubmit() {
        this.submitted = true;
        console.log(this.form);
        // reset alerts on submit
        this.alertService.clear();

        // stop here if form is invalid
        if (this.form.invalid) {
            return;
        }

        this.loading = true;
        if (this.isAddMode) {
            this.createCreditcard();
        } else {
            this.updateCreditcard();
        }
    }

    private createCreditcard() {
        var input = this.form.value;
        input.expiryDate = new Date(input.expiryYear, input.expiryMonth, 0);
        input.number = this.maskNumber(input.number);

        this.creditcardService.addCard(input)
            .pipe(first())
            .subscribe({
                next: () => {
                    this.alertService.success('Creditcard added successfully', { keepAfterRouteChange: true });
                    this.router.navigate(['../../'], { relativeTo: this.route });
                },
                error: error => {
                    this.alertService.error(error);
                    this.loading = false;
                }
            });
    }

    private updateCreditcard() {
        var input = this.form.value;
        input.id = this.id;
        input.expiryDate = new Date(this.form.value.expiryYear, this.form.value.expiryMonth, 0);
        console.log(input);
        this.creditcardService.update(this.id, input)
            .pipe(first())
            .subscribe({
                next: () => {
                    this.alertService.success('Update successful', { keepAfterRouteChange: true });
                    this.router.navigate(['../../'], { relativeTo: this.route });
                },
                error: error => {
                    this.alertService.error(error);
                    this.loading = false;
                }
            });
    }

    private maskNumber(cardnumber: string) {
      var length = cardnumber.length;
      var toMaskStr = cardnumber.slice(6,length-4);
      var mask = 'x'.repeat(toMaskStr.length);
      return cardnumber.replace(toMaskStr, mask);

    }
}
