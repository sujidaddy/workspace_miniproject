'use client'
import { useState, useEffect } from "react";
import { useAtom } from "jotai";
import { type TokenType, loginTokenAtom } from "@/components/JWTToken";

export default function SystemMain() {

    const [loginToken, setLoginToken] = useAtom(loginTokenAtom);

    useEffect(() => {
        console.log("JWT Token in SystemMain:", loginToken);
    }, []);


    return (
        <div>
            로그인 후 첫 페이지
        </div>
    );
}
