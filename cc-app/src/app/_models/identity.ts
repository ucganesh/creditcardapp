import { Role } from "./role";

export class Identity {
    userId: number;
    name: string;
    username: string;
    roles: Role[];
    access_token?: string;
}
