'use server'

import { cookies } from "next/headers";
import { jwtDecode } from "jwt-decode";

export interface TokenType {
    username: string;
    provider: string;
    email: string;
    exp: number;
}


export default async function getAuthHeaders(): Promise<TokenType | null> {
    try {
            const cookieStore = await cookies();
            const token = cookieStore.get("jwtToken");
            //console.log("token = ", token?.value);
            if(token == null)
                return null;
            const decodedToken = jwtDecode<TokenType>(token?.value);
            console.log("decodedToken = ", decodedToken);

            if (decodedToken.exp * 1000 < Date.now()) {
                console.warn("Token has expired.");
                return null;
            }

            return decodedToken;
        } catch (error) {
            console.error(error);
            return null;
        }
}