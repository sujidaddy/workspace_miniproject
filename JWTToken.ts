'use server'
import {cookies} from "next/headers";
import { TokenType } from "@/types/TokenType";
import { jwtDecode } from "jwt-decode";


export async function saveJWTToken(token : string) {
    console.log('saveJWTToken : ', token)
    const cookieStore = await cookies();
    if(token === '') {
        cookieStore.delete('JWTToken');
        return;
    }
    else {
        cookieStore.set('JWTToken', token, {
            httpOnly:true,
            secure : false,
            sameSite : 'lax',
            path: '/',
            maxAge : 60 * 60 // 1시간
            //maxAge : 60 // 1분
        });
    }
}

export async function getJWTToken() : Promise<string> {
    const cookieStore = await cookies();
    const token = await cookieStore.get('JWTToken');
    if(!token)
        return ""
    return token.value;
}

export async function isJWTTokenExpired() : Promise<boolean> {
    const token = await getJWTToken();
    if(!token)
        return false;
    const decodedToken = await jwtDecode<TokenType>(token);
    if(!decodedToken)
        return true;

    try {
        const currentTime = Date.now() / 1000;
        return decodedToken.exp < currentTime;
    } catch(error) {
        return true;
    }
    return false;
}