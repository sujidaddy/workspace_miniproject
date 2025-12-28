'use client'
import { KeyboardEvent, FormEvent, useState, useRef, useEffect } from "react";
import { useRouter } from "next/navigation";
import { signIn, useSession, signOut, getSession} from "next-auth/react";
import { useAtom } from "jotai";
import { type Member, loginMemberAtom } from "@/atoms/atoms";


export default function SystemLogin() {
    const idRef = useRef<HTMLInputElement>(null);
    const passwordRef = useRef<HTMLInputElement>(null);
    const router = useRouter();

    const { data: session, status, update} = useSession();
    const [loginMember, setLoginMember] = useAtom(loginMemberAtom);

    useEffect(() => {
        fetchSession();
    }, []);
    
    const fetchSession = async () => {
        try {
            const session = await getSession();
            //console.log('session:', session);
            if(session) {
                await signOut({ redirect: false, callbackUrl: "/" })
                const url = `http://localhost:8080/api/v1/login${session.provider}`;
                const provider = `${session.provider}*${session.user?.name}*${session.user?.email}`;
                //console.log("provider : ", session.provider);
                const response = await fetch(url!, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    body: JSON.stringify({ 'provider' : provider }),
                });

                const result = await response.json();
                //console.log('result:', result);
                setLoginMember(result);
                router.push('/system/main');
            }
        } catch (error) {
            console.error('Error fetching session:', error);
        } finally {
            await signOut({ redirect: false, callbackUrl: "/" })
        }
    }

    const validateId = (id: string) => {
        const regex = /^[a-zA-Z0-9_]{7,16}$/;
        return regex.test(id);
    }

    const validatePassword = (password: string) => {
        const regex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&#])[A-Za-z\d@$!%*?&#]{10,20}$/;
        return regex.test(password);
    }

    const onHandleLogin = async(e? : FormEvent<HTMLFormElement | HTMLButtonElement>) => {
        //console.log("onHandleLogin");
        e?.preventDefault();

        const id = idRef.current?.value;
        const password = passwordRef.current?.value;

        if(!id) {
            alert('아이디를 입력해주세요.');
            idRef.current?.focus();
            return;
        }

        if(!password) {
            alert('비밀번호를 입력해주세요.');
            passwordRef.current?.focus();
            return;
        }

        if (!validateId(id)) {
            alert('아이디는 7~16자의 영문, 숫자, 밑줄(_)만 사용 가능합니다.');
            alert('아이디는 7~16자의 영문, 숫자, 밑줄(_)만 사용 가능합니다.');
            idRef.current?.focus();
            return;
        }

        if (!validatePassword(password)) {
            alert('비밀번호는 10~20자이며, 영문 대/소문자, 숫자, 특수문자를 각각 1개 이상 포함해야 합니다.');
            passwordRef.current?.focus();
            return;
        }

        try {
            const response = await fetch('http://localhost:8080/api/v1/loginid', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ 'id' : id, 'password' :password }),
            });

            if (!response.ok) {
                alert('로그인에 실패했습니다. 아이디와 비밀번호를 확인해주세요.');
                passwordRef.current!.value = '';
                passwordRef.current?.focus();
                return;
            }

            const result = await response.json();
            //console.log('fetchLogin response:', result);
            setLoginMember(result);
            
            router.push('/system/main');
        } catch (error) {
            console.error('Error during login:', error);
            alert('로그인 중 문제가 발생했습니다.')
        }
    };

    const onHandleKeyDown = async(e : KeyboardEvent<HTMLInputElement>) => {
        if (e.key === 'Enter') {
            e.preventDefault();
            onHandleLogin();
        }
    };

    const onHandleJoin = async() => {
        router.push('/system/joinMember');
    };

    const onHandleOAuth = async(provider : string) => {
        const result = await signIn(provider, {redirect:false});
        //console.log("result :", result);
    };

    return (
        <div className='fixed inset-0 bg-opacity-50 flex flex-col items-center justify-center z-50'>
            <div className="w-full h-120 max-w-md p-8 space-y-6 bg-white rounded-lg shadow-lg">
                <h1 className='text-center text-3xl font-bold text-gray-800'>로그인</h1>
                <form className="space-y-6" onSubmit={onHandleLogin}>
                    <div>
                        <label 
                            htmlFor="inputId"
                            className="block mb-2 text-sm font-medium text-gray-700">
                            아이디
                        </label>
                        <input
                            id = "inputId"
                            type="text"
                            placeholder="7~16자 영문, 숫자, _"
                            ref={idRef} 
                                minLength={7}
                                maxLength={16}
                            className="w-full px-4 py-2 text-gray-700 bg-gray-100 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                        />
                    </div>
                    <div>
                        <label 
                            htmlFor="inputPassword"
                            className="block mb-2 text-sm font-medium text-gray-700">
                            비밀번호
                        </label>
                        <input
                            id = "inputPassword"
                            type="password"
                            placeholder="10~20자 영문, 숫자, 특수문자 포함"
                            ref={passwordRef} 
                            minLength={10}
                            maxLength={20}
                            onKeyDown={onHandleKeyDown}
                            className="w-full px-4 py-2 text-gray-700 bg-gray-100 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                        />
                    </div>
                    <div className="flex justify-around">
                        <button type="submit" onClick={onHandleLogin} className="px-6 py-2 text-white bg-blue-500 rounded-md hover:bg-blue-600 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-opacity-50">로그인</button>
                        <button type="button" onClick={onHandleJoin} className="px-6 py-2 text-blue-500 bg-white border border-blue-500 rounded-md hover:bg-gray-100 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-opacity-50">회원가입</button>
                    </div>
                    <div className="flex justify-center space-x-4">
                        <button type="button" onClick={() => onHandleOAuth('google')} className="px-4 py-2 text-sm text-black bg-red-400 rounded-md hover:bg-red-600">구글 로그인</button>
                        <button type="button" onClick={() => onHandleOAuth('naver')} className="px-4 py-2 text-sm text-black bg-green-400 rounded-md hover:bg-green-600">네이버 로그인</button>
                        <button type="button" onClick={() => onHandleOAuth('kakao')} className="px-4 py-2 text-sm text-black bg-yellow-300 rounded-md hover:bg-yellow-600">카카오 로그인</button>
                    </div>
                </form>
            </div>
        </div>
    );
}