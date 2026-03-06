// src/app/dashboard/page.tsx
import KpiSection from "@/components/dashboard/KpiSection";
// import ChartCard from "@/components/dashboard/ChartCard"; // 구조도에 정의된 컴포넌트
// import MapCard from "@/components/dashboard/MapCard";     // 구조도에 정의된 컴포넌트

export default function DashboardPage() {
  return (
    <div className="max-w-[1600px] mx-auto space-y-8">
      {/* 대시보드 타이틀 영역 */}
      <header className="flex justify-between items-end">
        <div>
          <h1 className="text-3xl font-bold text-slate-900">Dashboard</h1>
          <p className="text-gray-500 mt-1">Welcome back, here is what's happening today.</p>
        </div>
        <button className="bg-[#FF5A3D] text-white px-6 py-2.5 rounded-xl font-semibold hover:bg-orange-600 transition-colors">
          Schedule Reports
        </button>
      </header>

      {/* 1. KPI 지표 섹션 (구조도의 KpiSection.tsx) */}
      <KpiSection />

      {/* 2. 차트 및 지도 섹션 (구조도의 ContentGrid.tsx 역할) */}
      <div className="grid grid-cols-1 lg:grid-cols-12 gap-6">
        {/* 차트 영역 (8개 컬럼 차지) */}
        <div className="lg:col-span-8 bg-white p-6 rounded-2xl border border-gray-100 shadow-sm">
          <div className="flex justify-between mb-4">
            <h3 className="font-bold text-lg">Listed vs Offer Price for Rejections</h3>
          </div>
          <div className="h-[300px] bg-gray-50 rounded-lg flex items-center justify-center border-dashed border-2 border-gray-200">
             {/* <ChartCard /> 로 대체 예정 */}
             <span className="text-gray-400 font-medium">Chart Loading...</span>
          </div>
        </div>

        {/* 지도 영역 (4개 컬럼 차지) */}
        <div className="lg:col-span-4 bg-white p-6 rounded-2xl border border-gray-100 shadow-sm">
          <h3 className="font-bold text-lg mb-4">Properties & Location</h3>
          <div className="h-[300px] bg-gray-50 rounded-lg flex items-center justify-center border-dashed border-2 border-gray-200">
             {/* <MapCard /> 로 대체 예정 (낚시 포인트 Map) */}
             <span className="text-gray-400 font-medium">Map Loading...</span>
          </div>
        </div>
      </div>
    </div>
  );
}