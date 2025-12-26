'use client'
import { FormEvent, useState, useRef, useEffect } from "react";
import { useRouter } from "next/navigation";
import { signIn, useSession, signOut} from "next-auth/react";
import { useAtom } from "jotai";
import { type Member, loginMemberAtom } from "@/atoms/atoms";
import Wave from 'react-wavify';


export default function SystemLoginogin() {
    const idRef = useRef<HTMLInputElement>(null);
    const passwordRef = useRef<HTMLInputElement>(null);
    const router = useRouter();

    const [googleProvider, setGoogleProvider] = useState<string>('');
    const [naverProvider, setNaverProvider] = useState<string>('');
    const [kakaoProvider, setKakaoProvider] = useState<string>('');
    const { data: session} = useSession();
    const [loginMember, setLoginMember] = useAtom(loginMemberAtom);

    useEffect(() => {
        if(session) {
            parseSession(session);
        }
    }, [session]);

    const parseSession = async (session : any) => {
        //console.log("OAuth2 session set:", session);
        let url;
        let user_provider;
        try {
            const provider = await localStorage.getItem('provider');
            //console.log('provider:', provider);
            switch(provider) {
                case 'google':
                    url = 'http://localhost:8080/api/v1/loginGoogle';
                    user_provider = `google*${session.user?.name}*${session.user?.email}`;
                    break;
                case 'naver':
                    url = 'http://localhost:8080/api/v1/loginNaver';
                    user_provider = `naver*"${session.user?.name}*${session.user?.email}`
                    break;
                case 'kakao':
                    url = 'http://localhost:8080/api/v1/loginKakao';
                    user_provider = `kakao*${session.user?.name}`
                    break;
            }
            const data = await signOut({ redirect: false, callbackUrl: "/" });
            //console.log("세션이 삭제되었습니다.", data.url);
            
            //console.log('OAuth2 login url:', url, user_provider);

            const response = await fetch(url!, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ 'provider' : user_provider }),
            });

            const result = await response.json();
            //console.log('fetch oauth2 login:', result);

            if (!response.ok) {
                return false;
            }
            setLoginMember(result);
            router.push('/system/main');
        } catch (error) {
            console.error('Error during parseSession:', error);
        } finally {
            const data = await signOut({ redirect: false, callbackUrl: "/" });
            //console.log("세션이 삭제되었습니다.", data.url);
        }
        
    }

    const validateId = (id: string) => {
        const regex = /^[a-zA-Z0-9_]{8,16}$/;
        return regex.test(id);
    }

    const validatePassword = (password: string) => {
        const regex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{10,20}$/;
        return regex.test(password);
    }

    const fetchLogin = async(id : string, password : string) => {
        try {
            const response = await fetch('http://localhost:8080/api/v1/login_id', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ 'id' : id, 'password' :password }),
            });

            const result = await response.json();
            //console.log('fetchLogin response:', result);

            if (!response.ok) {
                return false;
            }
            setLoginMember(result);
            
            return true;
        } catch (error) {
            console.error('Error during login:', error);
            return false;
        }
    }

    const onHandleGoogle = async() => {
        await localStorage.setItem('provider', 'google');
        signIn('google', { callbackUrl: '/system/login' });
    };

    const onHandleNaver = async() => {
        await localStorage.setItem('provider', 'naver');
        signIn('naver', { callbackUrl: '/system/login' });
    };

    const onHandleKakao = async() => {
        await localStorage.setItem('provider', 'kakao');
        signIn('kakao', { callbackUrl: '/system/login' });
    };

    const onHandleLogin = async(e : FormEvent<HTMLFormElement | HTMLButtonElement>) => {
        console.log("onHandleLogin");
        e.preventDefault();

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
            alert('아이디는 8~16자의 영문, 숫자, 밑줄(_)만 사용 가능합니다.');
            idRef.current?.focus();
            return;
        }

        if (!validatePassword(password)) {
            alert('비밀번호는 10~20자이며, 영문 대/소문자, 숫자, 특수문자를 각각 1개 이상 포함해야 합니다.');
            passwordRef.current?.focus();
            return;
        }

        const result = await fetchLogin(id, password);
        if (!result) {
            alert('로그인에 실패했습니다. 아이디와 비밀번호를 확인해주세요.');
            passwordRef.current!.value = '';
            passwordRef.current?.focus();
            return;
        }

        router.push('/system/main');
    };

    const onHandleJoin = async() => {
        console.log("onHandleJoin");
        router.push('/system/joinMember');
    };

    return (
        <div className='fixed inset-0 bg-opacity-50 flex flex-col items-center justify-center z-50'>
            <Wave
                fill='rgba(30, 180, 220, 0.4)'
                paused={false}
                style={{ position: 'absolute', bottom: 0, left: 0, width: '100%', height: '200px' }}
                options={{
                    height: 100,
                    amplitude: 40,
                    speed: 0.15,
                    points: 3
                }}
            />
            <Wave
                fill='rgba(30, 160, 240, 0.5)'
                paused={false}
                style={{ position: 'absolute', bottom: 0, left: 0, width: '100%', height: '200px' }}
                options={{
                    height: 80,
                    amplitude: 30,
                    speed: 0.2,
                    points: 4
                }}
            />
            <Wave
                fill='rgba(30, 144, 255, 0.6)'
                paused={false}
                style={{ position: 'absolute', bottom: 0, left: 0, width: '100%', height: '200px' }}
                options={{
                    height: 60,
                    amplitude: 20,
                    speed: 0.25,
                    points: 5
                }}
            />
            <div className="w-full h-120 max-w-md p-8 space-y-6 bg-white rounded-lg shadow-lg">
                <h1 className='text-center text-3xl font-bold text-gray-800'>로그인</h1>
                <form className="space-y-6">
                    <div>
                        <label 
                            htmlFor="inputId"
                            className="block mb-2 text-sm font-medium text-gray-700">
                            아이디
                        </label>
                        <input
                            id = "inputId"
                            type="text"
                            placeholder="8~16자 영문, 숫자, _"
                            ref={idRef} 
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
                            className="w-full px-4 py-2 text-gray-700 bg-gray-100 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                        />
                    </div>
                    <div className="flex justify-around">
                        <button type="button" onClick={onHandleLogin} className="px-6 py-2 text-white bg-blue-500 rounded-md hover:bg-blue-600 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-opacity-50">로그인</button>
                        <button type="button" onClick={onHandleJoin} className="px-6 py-2 text-blue-500 bg-white border border-blue-500 rounded-md hover:bg-gray-100 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-opacity-50">회원가입</button>
                    </div>
                    <div className="flex justify-center space-x-4">
                        <button type="button" onClick={onHandleGoogle} className="px-4 py-2 text-sm text-black bg-red-400 rounded-md hover:bg-red-600">구글 로그인</button>
                        <button type="button" onClick={onHandleNaver} className="px-4 py-2 text-sm text-black bg-green-400 rounded-md hover:bg-green-600">네이버 로그인</button>
                        <button type="button" onClick={onHandleKakao} className="px-4 py-2 text-sm text-black bg-yellow-300 rounded-md hover:bg-yellow-600">카카오 로그인</button>
                    </div>
                </form>
            </div>
        </div>
    );
}