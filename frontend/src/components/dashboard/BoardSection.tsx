"use client";

import KpiSection from "./KpiSection";
import Link from "next/link";
import { motion } from "framer-motion";

export default function BoardSection() {
  return (
    <div className="w-full h-full p-8 md:p-12 overflow-y-auto">
      <div className="max-w-[1400px] mx-auto space-y-10">
        
        {/* 1. 헤더 영역 */}
        <header className="flex justify-between items-center">
          <div>
            <h2 className="text-3xl font-bold text-slate-900">Dashboard</h2>
            <p className="text-gray-500">Real-time property and fishing point analytics</p>
          </div>
          <button className="bg-[#FF5A3D] text-white px-6 py-3 rounded-xl font-bold shadow-lg hover:bg-orange-600 transition-all">
            Schedule Reports
          </button>
        </header>

        {/* 2. KPI 섹션 (기존에 만든 KpiSection 호출) */}
        <KpiSection />

        {/* 3. 콘텐츠 그리드 (차트 & 지도) */}
        <div className="grid grid-cols-1 lg:grid-cols-12 gap-8">
          
          {/* 차트 카드: 클릭 시 차트 상세 페이지로 이동 */}
          <Link href="/dashboard/chart" className="lg:col-span-8 group">
            <motion.div 
              whileHover={{ y: -5 }}
              className="bg-white p-8 rounded-3xl border border-gray-100 shadow-sm h-[450px] transition-all group-hover:shadow-xl"
            >
              <h3 className="text-xl font-bold mb-6">Listed vs Offer Price for Rejections</h3>
              <div className="w-full h-[320px] bg-slate-50 rounded-2xl flex items-center justify-center border-2 border-dashed border-gray-200 text-gray-400">
                {/* 여기에 BarChart 컴포넌트 삽입 예정 */}
                Bar Chart Visualization
              </div>
            </motion.div>
          </Link>

          {/* 지도 카드: 클릭 시 지도 상세 페이지로 이동 */}
          <Link href="/dashboard/map" className="lg:col-span-4 group">
            <motion.div 
              whileHover={{ y: -5 }}
              className="bg-white p-8 rounded-3xl border border-gray-100 shadow-sm h-[450px] transition-all group-hover:shadow-xl"
            >
              <h3 className="text-xl font-bold mb-6">Properties & Fishing Points</h3>
              <div className="w-full h-[320px] bg-blue-50 rounded-2xl flex items-center justify-center border-2 border-dashed border-blue-100 text-blue-300">
                {/* 여기에 WorldMap 컴포넌트 삽입 예정 */}
                Interactive Fishing Map
              </div>
            </motion.div>
          </Link>

        </div>
      </div>
    </div>
  );
}