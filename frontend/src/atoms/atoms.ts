import {atom} from "jotai"

export interface Member {
    user_no : number;
    userid : string;
    username : string;
    email : string;
    Role : string;
    enabled : boolean;
    createTime : string;
    lastLoginTime : string;
    google : string | null;
    naver : string | null;
    kakao : string | null;
}

export const loginMemberAtom = atom<Member | null>(null);