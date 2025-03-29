import { AnyARecord } from "dns";
import { Car } from "./car";

export interface User {
    userId: String;
    userEmail: String;
    data?: Car
}
