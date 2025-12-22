// src/components/layout/Header.tsx
"use client";

import SearchBar from "./SearchBar";
import { Bell, Trophy, ChevronDown } from "lucide-react";
import Image from "next/image";

export default function Header() {
  return (
    <header className="h-20 bg-white border-b border-gray-100 flex items-center justify-between px-8 sticky top-0 z-20">
      {/* 왼쪽: 검색창 섹션 (구조도의 SearchBar 포함) */}
      <div className="flex-1">
        <SearchBar />
      </div>

      {/* 오른쪽: 사용자 액션 섹션 */}
      <div className="flex items-center gap-6">
        {/* 아이콘 버튼들 */}
        <div className="flex items-center gap-4 text-gray-400">
          <button className="p-2 hover:bg-gray-50 rounded-lg transition-colors">
            <Trophy size={20} />
          </button>
          <button className="p-2 hover:bg-gray-50 rounded-lg transition-colors relative">
            <Bell size={20} />
            <span className="absolute top-2 right-2 w-2 h-2 bg-red-500 rounded-full border-2 border-white"></span>
          </button>
        </div>

        {/* 사용자 프로필 (대시보드 이미지 스타일 반영) */}
        <div className="flex items-center gap-3 pl-6 border-l border-gray-100 cursor-pointer group">
          <div className="w-10 h-10 rounded-full overflow-hidden border border-gray-200">
            <div className="bg-orange-100 w-full h-full flex items-center justify-center text-orange-600 font-bold">
              JD
            </div>
          </div>
          <div className="hidden md:block">
            <p className="text-sm font-bold text-slate-800">John Doe</p>
            <p className="text-xs text-gray-400">Admin</p>
          </div>
          <ChevronDown size={16} className="text-gray-400 group-hover:text-gray-600 transition-colors" />
        </div>
      </div>
    </header>
  );
}