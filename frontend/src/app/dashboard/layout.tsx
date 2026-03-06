// src/app/dashboard/layout.tsx
import Sidebar from "@/components/layout/SideBar";
import Header from "@/components/layout/Header";

export default function DashboardLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  return (
    <div className="flex h-screen bg-[#F8F9FA] overflow-hidden">
      {/* 1. 좌측 사이드바 (구조도의 Sidebar.tsx) */}
      <Sidebar />

      <div className="flex-1 flex flex-col min-w-0">
        {/* 2. 상단 헤더 (구조도의 Header.tsx + SearchBar 포함) */}
        <Header />

        {/* 3. 메인 콘텐츠 영역 (page.tsx가 들어가는 곳) */}
        <main className="flex-1 overflow-y-auto p-6 custom-scrollbar">
          {children}
        </main>
      </div>
    </div>
  );
}