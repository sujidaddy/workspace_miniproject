'use client'
import { useState, useEffect } from "react";
import GetJWTToken from "@/components/GetJWTToken";
import { useAtom } from "jotai";
import { type Member, loginMemberAtom } from "@/atoms/atoms";

export default function SystemMain() {

    const [loginMember, setLoginMember] = useAtom(loginMemberAtom);

    useEffect(() => {
        console.log("JWT Token in SystemMain:", loginMember);
    }, []);


    return (
        <div>
            로그인 후 첫 페이지
        </div>
    );
}
