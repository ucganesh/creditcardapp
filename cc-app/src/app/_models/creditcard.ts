import { User } from "./user";

export class Creditcard {
    id: number;
    name: string;
    number: string;
    type: string;
    expiryDate: Date;
    status: string;
    user: User;
}
