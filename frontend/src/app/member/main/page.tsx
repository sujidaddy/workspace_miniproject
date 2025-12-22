import type {TokenType} from "@/components/Token";
import getUserData from "@/components/Token";

export default async function MainPage() {
    const user : TokenType | null = await getUserData();

    return (
        <div>
            로그인 후 첫 페이지
            {user && (
                <>
                    <p>{user.username} 님 환영합니다.</p>
                    <p>로그인 제공자: {user.provider}</p>
                </>
            )}
        </div>
    );
}