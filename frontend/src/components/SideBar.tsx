"use client";

import Link from "next/link";
import { usePathname } from "next/navigation";
interface SidebarProps {
  selectedSpecies: string;
  onSpeciesChange: (species: string) => void;
}
export default function Sidebar({selectedSpecies, onSpeciesChange} : SidebarProps) {
  const pathname = usePathname();

  const menuItems = [
    { name: "대시보드", icon: "📊", path: "/dashboard" },
    { name: "낚시터 데이터", icon: "🎣", path: "/dashboard/fishing" },
    { name: "통계 분석", icon: "📈", path: "/dashboard/analysis" },
    { name: "지도 보기", icon: "🗺️", path: "/dashboard/map" },
    { name: "설정", icon: "⚙️", path: "/dashboard/settings" },
  ];
  //  const speciesList = [
  //   "전체",
  //   "감성돔",
  //   "농어",
  //   "넙치",
  //   "우럭",
  //   "광어",
  //   "갈치",
  //   "삼치",
  //   "방어",
  //   "볼락",

  // ];

  return (
    <aside className="w-64 bg-white border-r border-gray-200 flex flex-col">
      {/* 로고 */}
      <div className="p-6 border-b border-gray-200">
        <Link href="/" className="flex items-center gap-2">
          <div className="w-8 h-8 bg-blue-500 rounded flex items-center justify-center text-white font-bold">
            🌊
          </div>
          <span className="text-xl font-bold">바다낚시</span>
        </Link>
      </div>

      {/* 메뉴 */}
      <nav className="flex-1 p-4">
        <ul className="space-y-2">
          {menuItems.map((item) => (
            <li key={item.path}>
              <Link
                href={item.path}
                className={`flex items-center gap-3 px-4 py-3 rounded-lg transition-colors ${
                  pathname === item.path
                    ? "bg-blue-50 text-blue-600"
                    : "text-gray-700 hover:bg-gray-50"
                }`}
              >
                <span className="text-xl">{item.icon}</span>
                <span className="font-medium">{item.name}</span>
              </Link>
            </li>
          ))}
        </ul>
      </nav>

      {/* 하단 정보 */}
      <div className="p-4 border-t border-gray-200">
        <div className="bg-gradient-to-br from-blue-500 to-cyan-500 rounded-lg p-4 text-white">
          <h3 className="font-bold mb-2">프리미엄 업그레이드</h3>
          <p className="text-sm text-white/80 mb-3">
            더 많은 기능을 이용하세요
          </p>
          <button className="w-full bg-white text-blue-600 py-2 rounded-lg font-semibold hover:bg-gray-100">
            자세히 보기
          </button>
        </div>
      </div>
    </aside>
  );
}