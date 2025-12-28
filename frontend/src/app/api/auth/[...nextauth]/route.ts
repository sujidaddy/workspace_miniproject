import NextAuth from "next-auth"
import GoogleProvider from "next-auth/providers/google"
import Naver from "next-auth/providers/naver"
import Kakao from "next-auth/providers/kakao"

const handler = NextAuth({
  providers: [
    GoogleProvider({
      clientId: process.env.NEXT_PUBLIC_GOOGLE_CLIENT_ID!,
      clientSecret: process.env.NEXT_PUBLIC_GOOGLE_CLIENT_SECRET!,
    }),
    Naver({
      clientId: process.env.NEXT_PUBLIC_NAVER_CLIENT_ID!,
      clientSecret: process.env.NEXT_PUBLIC_NAVER_CLIENT_SECRET!,
      profile(profile) {
        return {
          id: profile.response.id, 
          name: profile.response.name,
          email: profile.response.email,
          image: profile.response.profile_image,
        }
      }
    }),
    Kakao({
      clientId: process.env.NEXT_PUBLIC_KAKAO_CLIENT_ID!,
      clientSecret: process.env.NEXT_PUBLIC_KAKAO_CLIENT_SECRET!,
      profile(profile) {
        return {
          id: profile.id,
          name: profile.kakao_account.profile.nickname,
          email: profile.kakao_account.email,
          image: profile.kakao_account.profile.profile_image_url,
        }
      }
    }),
  ],
  callbacks: {
    async jwt({ token, account }) {
      // 최초 로그인 시 account 객체에 provider 정보가 들어있습니다.
      if(account) {
        token.provider = account.provider;
      }
      return token
    },
    async session({ session, token }) {
      // 세션 객체에 provider 정보를 전달
      session.provider = token.provider;
      return session;
    }
  },

  secret: process.env.NEXTAUTH_SECRET,
  //debug: true,
})

export { handler as GET, handler as POST }