// src/components/layout/Header.tsx
"use client";
import { useState, useEffect} from "react";
import SearchBar from "./SearchBar";
import { Bell, Trophy, ChevronDown } from "lucide-react";
import Image from "next/image";

export default function AppHeader() {
  const [user, setUser] = useState<{name: string; role: string} | null>(null);
  const [isLoading, setIsLoading]= useState(false);


// 1. 로그인 API 호출 함수
  const handleLogin = async () => {
    setIsLoading(true);
    const API_URL = "http://10.125.121.176:8080/api/v1/loginid";
    const params = new URLSearchParams({
      userid: "administrator",
      password: "Abcd1234!@",
    });

    try {
      const response = await fetch(`${API_URL}?${params.toString()}`, {
        method: "GET",
      });

      if (response.ok) {
        const data = await response.json();
        console.log("✅ 로그인 성공:", data);
        alert("로그인에 성공하였습니다.");
        
      } else {
        console.error("❌ 로그인 실패:", response.status);
      }
    } catch (error) {
      console.error("⚠️ 네트워크 오류:", error);
    } finally {
      setIsLoading(false);
    }
  };
  return (
    <header className="h-20 bg-white border-b border-gray-100 flex items-center justify-between px-8 sticky top-0 z-20">
      <div className="flex-1"><SearchBar /></div>

      <div className="flex items-center gap-6">
        <div className="flex items-center gap-4 text-gray-400">
          <button className="p-2 hover:bg-gray-50 rounded-lg transition-colors">
            <Trophy size={20} />
          </button>
          <button className="p-2 hover:bg-gray-50 rounded-lg transition-colors relative">
            <Bell size={20} />
            <span className="absolute top-2 right-2 w-2 h-2 bg-red-500 rounded-full border-2 border-white"></span>
          </button>
        </div>

        {/* 2. 사용자 프로필 섹션 (클릭 시 handleLogin 실행) */}
        <div 
          onClick={handleLogin}
          className={`flex items-center gap-3 pl-6 border-l border-gray-100 cursor-pointer group ${isLoading ? 'opacity-50' : ''}`}
        >
          <div className="w-10 h-10 rounded-full overflow-hidden border border-gray-200">
            <div className="bg-orange-100 w-full h-full flex items-center justify-center text-orange-600 font-bold text-sm">
              {isLoading ? "..." : "JD"}
            </div>
          </div>
          <div className="hidden md:block">
            <p className="text-sm font-bold text-slate-800">
              {isLoading ? "Connecting..." : "Login"}
            </p>
            <p className="text-xs text-gray-400">Admin</p>
          </div>
          <ChevronDown size={16} className="text-gray-400 group-hover:text-gray-600 transition-colors" />
        </div>
      </div>
    </header>
  );
}
  //0119 원본
//   return (
//     <header className="h-20 bg-white border-b border-gray-100 flex items-center justify-between px-8 sticky top-0 z-20">
      
//       <div className="flex-1">
//         <SearchBar />
//       </div>

//       {/* 오른쪽: 사용자 액션 섹션 */}
//       <div className="flex items-center gap-6">
//         {/* 아이콘 버튼들 */}
//         <div className="flex items-center gap-4 text-gray-400">
//           <button className="p-2 hover:bg-gray-50 rounded-lg transition-colors">
//             <Trophy size={20} />
//           </button>
//           <button className="p-2 hover:bg-gray-50 rounded-lg transition-colors relative">
//             <Bell size={20} />
//             <span className="absolute top-2 right-2 w-2 h-2 bg-red-500 rounded-full border-2 border-white"></span>
//           </button>
//         </div>

//         {/* 사용자 프로필 (대시보드 이미지 스타일 반영) */}
//         <div className="flex items-center gap-3 pl-6 border-l border-gray-100 cursor-pointer group">
//           <div className="w-10 h-10 rounded-full overflow-hidden border border-gray-200">
//             <div className="bg-orange-100 w-full h-full flex items-center justify-center text-orange-600 font-bold">
//               JD
//             </div>
//           </div>
//           <div className="hidden md:block">
//             <p className="text-sm font-bold text-slate-800">Login</p>
//             <p className="text-xs text-gray-400">Admin</p>
//           </div>
//           <ChevronDown size={16} className="text-gray-400 group-hover:text-gray-600 transition-colors" />
//         </div>
//       </div>
//     </header>
//   );
// }