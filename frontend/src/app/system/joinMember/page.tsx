'use client'
import { FormEvent, useRef, useEffect, useState } from "react";
import { useRouter } from "next/navigation";
import { signIn, useSession, signOut, getSession} from "next-auth/react";


export default function AddMemberPage() {
    const idRef = useRef<HTMLInputElement>(null);
    const passwordRef = useRef<HTMLInputElement>(null);
    const passwordRef2 = useRef<HTMLInputElement>(null);
    const nameRef = useRef<HTMLInputElement>(null);
    const emailRef = useRef<HTMLInputElement>(null);
    const router = useRouter();

    const [isValidateID, setIsValidateID] = useState<boolean>(false);
    const [isValidateEmail, setIsValidateEmail] = useState<boolean>(false);
    const [googleProvider, setGoogleProvider] = useState<string|null>(null);
    const [naverProvider, setNaverProvider] = useState<string|null>(null); 
    const [kakaoProvider, setKakaoProvider] = useState<string|null>(null);
    const { data: session, status, update} = useSession();
    const [googleOAuthText, setGoogleOAuthText] = useState<string>('구글 인증');
    const [naverOAuthText, setNaverOAuthText] = useState<string>('네이버 인증');
    const [kakaoOAuthText, setKakaoOAuthText] = useState<string>('카카오 인증');




    useEffect(() => {
        loadInputData();
        fetchSession();
    }, []);

    const fetchSession = async () => {
        try {
            const session = await getSession();
            //console.log('session:', session);
            if(session) {
                await signOut({ redirect: false, callbackUrl: "/" })
                //console.log("provider : ", session.provider);
                const provider = `${session.provider}*${session.user?.name}*${session.user?.email}`;
                handleCheckDuplicate(session.provider, provider);
            }
        } catch (error) {
            console.error('Error fetching session:', error);
        } finally {
            await signOut({ redirect: false, callbackUrl: "/" })
        }
    }

    const removeInputData = async () => {
        await localStorage.removeItem('joinData');
    }

    const loadInputData = async () => {
        const load = await localStorage.getItem('joinData');
        if(load)
        {
            //console.log("loadInputData:", load);
            //console.log("loadInputData:", load);
            const json = await JSON.parse(load);
            idRef.current!.value = json.id;
            passwordRef.current!.value = json.password;
            passwordRef2.current!.value = json.password2;
            nameRef.current!.value = json.name;
            emailRef.current!.value = json.email;
            setGoogleProvider(json.googleProvider);
            setNaverProvider(json.naverProvider);
            setKakaoProvider(json.kakaoProvider);
            setIsValidateID(json.isValidateID);
            setIsValidateEmail(json.isValidateEmail);
            removeInputData();
        }
    }

    const saveInputData = async () => {
        const id = idRef.current?.value;
        const password = passwordRef.current?.value;
        const password2 = passwordRef2.current?.value;
        const name = nameRef.current?.value;
        const email = emailRef.current?.value;
        await localStorage.setItem('joinData', JSON.stringify({ id, password, password2, name, email, googleProvider, naverProvider, kakaoProvider, isValidateID, isValidateEmail }));
    }

    const fetchValidateID = async (id: string) => {
        try {
            const response = await fetch('http://localhost:8080/api/v1/join/validateID', {
                method : 'POST',
                headers : {
                    'Content-Type' : 'application/json'
                },
                body: id
            });

            if (!response.ok) {
                return false;
            }
            
            const result = await response.text();
            return result === 'True';
        } catch (error) {
            alert('서버와의 통신 중 오류가 발생했습니다. 잠시 후 다시 시도해주세요.');
        }
        return false;
    }

    const fetchValidateEmail = async (email: string) => {
        try {
            const response = await fetch('http://localhost:8080/api/v1/join/validateEmail', {
                method : 'POST',
                headers : {
                    'Content-Type' : 'application/json'
                },
                body: email
            });

            if (!response.ok) {
                return false;
            }
            
            const result = await response.text();
            return result === 'True';
        } catch (error) {
            alert('서버와의 통신 중 오류가 발생했습니다. 잠시 후 다시 시도해주세요.');
        }
        return false;
    }

    const fetchValidateOAuth = async (provider : string,  user: string) => {
        try {
            const response = await fetch(`http://localhost:8080/api/v1/join/validate${provider}`, {
                method : 'POST',
                headers : {
                    'Content-Type' : 'application/json'
                },
                body: user
            });

            if (!response.ok) {
                return false;
            }
            
            const result = await response.text();
            return result === 'True';
        } catch (error) {
            alert('서버와의 통신 중 오류가 발생했습니다. 잠시 후 다시 시도해주세요.');
        }
        return false;
    }

    const validateId = (id: string) => {
        const regex = /^[a-zA-Z0-9_]{7,16}$/;
        return regex.test(id);
    }

    const validatePassword = (password: string) => {
        const regex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&#])[A-Za-z\d@$!%*?&#]{10,20}$/;
        return regex.test(password);
    }

    const validateName = (name: string) => {
        const regex = /^[a-zA-Zㄱ-ㅎㅏ-ㅣ가-힣]{3,20}$/;
        return regex.test(name);
    }

    const validateEmail = (email: string) => {
        const regex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        return regex.test(email);
    }

    const handleCheckDuplicate = async (type: string, user: string) => {
        let value;
        let result = true;
        if (type === 'id') {
            value = idRef.current?.value;
            if(!value) {
                alert(`아이디를 입력해주세요.`);
                return;
            }
            if (!validateId(value)) {
                alert('아이디는 7~16자의 영문, 숫자, 밑줄(_)만 사용 가능합니다.');
                alert('아이디는 7~16자의 영문, 숫자, 밑줄(_)만 사용 가능합니다.');
                return;
            }

            result = await fetchValidateID(value);
            setIsValidateID(result);

            if(result)
                alert(`사용 가능한 ${type}입니다.`);
            else
                alert(`사용 가능할 수 없는 ${type}입니다.`);
        }
        if (type === 'email') {
            value = emailRef.current?.value;
            if(!value) {
                alert(`이메일을 입력해주세요.`);
                return;
            }
            if (!validateEmail(value)) {
                alert('유효하지 않은 이메일 주소입니다.');
                return;
            }

            result = await fetchValidateEmail(value);
            setIsValidateEmail(result);

            if(result)
                alert(`사용 가능한 ${type}입니다.`);
            else
                alert(`사용 가능할 수 없는 ${type}입니다.`);
        }
        if (type === 'google' || type === 'naver' || type === 'kakao') {
            const str = {
                'google' : '구글',
                'naver' : '네이버',
                'kakao' : '카카오',
            };
            result = await fetchValidateOAuth(type, user);

            if(result){
                alert(`${str[type]} 인증되었습니다.`);
                switch (type) {
                    case 'google':
                        setGoogleProvider(user);
                        setGoogleOAuthText('구글 인증 완료');
                        break;
                    case 'naver':
                        setNaverProvider(user);
                        setNaverOAuthText('네이버 인증 완료');
                        break;
                    case 'kakao':
                        setKakaoProvider(user);
                        setKakaoOAuthText('카카오 인증 완료');
                        break;
                }
            }
            else
                alert(`이미 사용 중인 ${str[type]} 계정입니다.`);
        }
    }

    const onHandleSignUp = async (e: FormEvent<HTMLFormElement | HTMLButtonElement>) => {
        e.preventDefault();

        const userid = idRef.current?.value;
        const password = passwordRef.current?.value;
        const password2 = passwordRef2.current?.value;
        const username = nameRef.current?.value;
        const email = emailRef.current?.value;

        //console.log("onHandleSignUp:", { id, password, password2, name, email });
        //console.log("onHandleSignUp:", { id, password, password2, name, email });

        if (!userid) {
            alert('아이디를 입력해주세요.');
            idRef.current?.focus();
            return;
        }

        if(!isValidateID) {
            alert('아이디 중복확인을 해주세요.');
            return;
        }

        if(!isValidateID) {
            alert('아이디 중복확인을 해주세요.');
            return;
        }

        if (!password) {
            alert('비밀번호를 입력해주세요.');
            passwordRef.current?.focus();
            return;
        }

        if (!password2) {
            alert('비밀번호를 입력해주세요.');
            passwordRef2.current?.focus();
            return;
        }

        if (!username) {
            alert('이름을 입력해주세요.');
            nameRef.current?.focus();
            return;
        }

        if (!email) {
            alert('이메일 주소를 입력해주세요.');
            emailRef.current?.focus();
            return;
        }

        if(!isValidateEmail) {
            alert('이메일 주소 중복확인을 해주세요.');
            return;
        }

        if(!isValidateEmail) {
            alert('이메일 주소 중복확인을 해주세요.');
            return;
        }

        if (!validateId(userid)) {
            alert('아이디는 7~16자의 영문, 숫자, 밑줄(_)만 사용 가능합니다.');
            idRef.current?.focus();
            return;
        }

        if (password !== password2) {
            alert('비밀번호가 일치하지 않습니다.');
            passwordRef.current?.focus();
            return;
        }

        if (!validatePassword(password)) {
            alert('비밀번호는 10~20자이며, 영문 대/소문자, 숫자, 특수문자를 각각 1개 이상 포함해야 합니다.');
            passwordRef.current?.focus();
            return;
        }

        if (!validateName(username)) {
            alert('이름은 3~20자의 한글 또는 영문으로만 구성되어야 합니다.');
            nameRef.current?.focus();
            return;
        }

        if (!validateEmail(email)) {
            alert('유효하지 않은 이메일 주소입니다.');
            emailRef.current?.focus();
            return;
        }

        try {
            const response = await fetch('http://localhost:8080/api/v1/join/joinUser', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    'userid' : userid,
                    'password' : password,
                    'username' : username,
                    'email' : email,
                    'google' : googleProvider,
                    'naver' : naverProvider,
                    'kakao' : kakaoProvider,
                }),
            });

            if(!response.ok) {
                alert('회원가입에 실패했습니다.');
                return;
            }

            const result = await response.json();
            console.log(result);

            alert('회원가입이 완료되었습니다.');
            router.push('/system/login');
            
        } catch (error) {
            alert('서버와의 통신 중 오류가 발생했습니다. 잠시 후 다시 시도해주세요.');
        } finally {
            removeInputData();
        }
    };

    const onHandleOAuth = async(provider : string) => {
        saveInputData();
        const result = await signIn(provider, {redirect:false});
        //console.log("result :", result);
    };

    return (
        <div className='fixed inset-0 bg-opacity-50 flex flex-col items-center justify-center z-50'>
            <div className="w-full h-180 max-w-md p-8 space-y-6 bg-white rounded-lg shadow-lg">
                <h1 className='text-center text-3xl font-bold text-gray-800'>회원가입</h1>
                <form className="space-y-6" onSubmit={onHandleSignUp}>
                    <div>
                        <label 
                            htmlFor="inputId"
                            className="block mb-2 text-sm font-medium text-gray-700">
                            아이디
                        </label>
                        <div className="flex space-x-2">
                            <input
                                id = "inputId"
                                type="text"
                                placeholder="7~16자 영문, 숫자, _"
                                ref={idRef} 
                                minLength={7}
                                maxLength={16}
                                onKeyDown={()=>{setIsValidateID(false)}}
                                className="flex-grow px-4 py-2 text-gray-700 bg-gray-100 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                            />
                            <button type="button" 
                                    suppressHydrationWarning={true}
                                    disabled={isValidateID}
                                    onClick={() => handleCheckDuplicate('id', '')} 
                                    className="px-4 py-2 text-sm text-blue-500 bg-white border border-blue-500 rounded-md hover:bg-gray-100
                                                disabled:bg-gray-100 disabled:cursor-not-allowed disabled:text-gray-400 disabled:border-gray-400"
                                    >중복확인</button>
                        </div>
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
                            className="w-full px-4 py-2 text-gray-700 bg-gray-100 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                        />
                    </div>
                    <div>
                        <label 
                            htmlFor="inputPassword2"
                            className="block mb-2 text-sm font-medium text-gray-700">
                            비밀번호 확인
                        </label>
                        <input
                            id = "inputPassword2"
                            type="password"
                            placeholder="비밀번호를 다시 입력하세요"
                            ref={passwordRef2} 
                            minLength={10}
                            maxLength={20}
                            className="w-full px-4 py-2 text-gray-700 bg-gray-100 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                        />
                    </div>
                    <div>
                        <label 
                            htmlFor="inputName"
                            className="block mb-2 text-sm font-medium text-gray-700">
                            이름
                        </label>
                        <input
                            id = "inputName"
                            type="text"
                            placeholder="3~20자 한글/영문"
                            ref={nameRef} 
                            minLength={3}
                            maxLength={20}
                            className="w-full px-4 py-2 text-gray-700 bg-gray-100 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                        />
                    </div>
                    <div>
                        <label 
                            htmlFor="inputEmail"
                            className="block mb-2 text-sm font-medium text-gray-700">
                            이메일 주소
                        </label>
                        <div className="flex space-x-2">
                            <input
                                id = "inputEmail"
                                type="email"
                                placeholder="your@email.com"
                                ref={emailRef} 
                                onKeyDown={()=>{setIsValidateEmail(false)}}
                                className="flex-grow px-4 py-2 text-gray-700 bg-gray-100 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                            />
                             <button type="button" 
                                    suppressHydrationWarning={true}
                                    disabled={isValidateEmail}
                                    onClick={() => handleCheckDuplicate('email', '')} 
                                    className="px-4 py-2 text-sm text-blue-500 bg-white border border-blue-500 rounded-md hover:bg-gray-100
                                                disabled:bg-gray-100 disabled:cursor-not-allowed disabled:text-gray-400 disabled:border-gray-400"
                                    >중복확인</button>
                        </div>
                    </div>
                    <div className="flex justify-center space-x-4">
                        <button type="button" 
                                onClick={() => onHandleOAuth('google')} 
                                disabled={!!googleProvider}
                                className="px-4 py-2 text-sm text-black bg-red-400 rounded-md hover:bg-red-600
                                            disabled:bg-gray-100 disabled:cursor-not-allowed disabled:text-gray-400 disabled:border-gray-400"
                                >{googleOAuthText}</button>
                        <button type="button" 
                                onClick={() => onHandleOAuth('naver')} 
                                disabled={!!naverProvider}
                                className="px-4 py-2 text-sm text-black bg-green-400 rounded-md hover:bg-green-600
                                            disabled:bg-gray-100 disabled:cursor-not-allowed disabled:text-gray-400 disabled:border-gray-400"
                                >{naverOAuthText}</button>
                        <button type="button" 
                                onClick={() => onHandleOAuth('kakao')} 
                                disabled={!!kakaoProvider}
                                className="px-4 py-2 text-sm text-black bg-yellow-300 rounded-md hover:bg-yellow-600
                                            disabled:bg-gray-100 disabled:cursor-not-allowed disabled:text-gray-400 disabled:border-gray-400"
                                >{kakaoOAuthText}</button>
                    </div>
                    <div className="flex justify-center">
                        <button type="submit" className="w-full px-6 py-3 text-white bg-blue-500 rounded-md hover:bg-blue-600 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-opacity-50">
                            회원가입
                        </button>
                    </div>
                </form>
            </div>
        </div>
    );
}