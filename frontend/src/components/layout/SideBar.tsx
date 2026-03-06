"use client";

import Link from "next/link";
import { LayoutDashboard, Users, BarChart3, Package, MessageSquare, Settings, ShieldCheck } from "lucide-react";

const menuItems = [
  { icon: LayoutDashboard, label: "Dashboard", href: "/dashboard", active: true },
  { icon: Users, label: "Client Data", href: "/dashboard/clients" },
  { icon: BarChart3, label: "Analytics", href: "/dashboard/analytics" },
  { icon: Package, label: "Product", href: "/dashboard/products" },
  { icon: MessageSquare, label: "Messages", href: "/dashboard/messages" },
  { icon: Settings, label: "Settings", href: "/dashboard/settings" },
  { icon: ShieldCheck, label: "Security", href: "/dashboard/security" },
];

export default function Sidebar() {
  return (
    <aside className="w-64 bg-white border-r border-gray-100 flex flex-col h-full">
      <div className="p-6">
        <div className="flex items-center gap-2 text-[#FF5A3D] font-bold text-xl">
          <div className="w-8 h-8 bg-[#FF5A3D] rounded-lg flex items-center justify-center text-white">D</div>
          Databrain
        </div>
      </div>

      <nav className="flex-1 px-4 py-4 space-y-2">
        {menuItems.map((item) => (
          <Link
            key={item.label}
            href={item.href}
            className={`flex items-center gap-3 px-4 py-3 rounded-xl transition-all ${
              item.active ? "bg-orange-50 text-[#FF5A3D]" : "text-gray-500 hover:bg-gray-50"
            }`}
          >
            <item.icon size={20} />
            <span className="font-medium">{item.label}</span>
          </Link>
        ))}
      </nav>

      {/* 업그레이드 배너 (이미지 하단 반영) */}
      <div className="p-4 mt-auto">
        <div className="bg-[#FF5A3D] p-6 rounded-2xl text-white">
          <p className="text-xs opacity-80 mb-2">Become Pro Access</p>
          <p className="text-sm font-bold mb-4 leading-tight">Get Detailed analytics for help, upgrade pro</p>
          <button className="w-full bg-white text-[#FF5A3D] py-2 rounded-lg font-bold text-sm">Upgrade Pro</button>
        </div>
      </div>
    </aside>
  );
}