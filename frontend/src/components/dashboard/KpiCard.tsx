"use client";

import React from 'react';
import { motion } from 'framer-motion';

interface KpiCardProps {
  title: string;
  value: string;
  description: string;
  trend: string;
  trendLabel: string;
  variant?: 'red' | 'purple' | 'orange';
}

// 부모 섹션에서 사용할 수 있도록 export (Stagger 효과용)
export const itemVariants = {
  hidden: { y: 20, opacity: 0 },
  visible: { y: 0, opacity: 1, transition: { duration: 0.5 } }
};

const KpiCard = ({ 
  title, 
  value, 
  description, 
  trend, 
  trendLabel, 
  variant = 'red' 
}: KpiCardProps) => {
  
  // 1. 디자인 스타일 정의 (색상 포인트)
  const colorStyles = {
    red: "bg-red-50 text-red-600",
    purple: "bg-purple-50 text-purple-600",
    orange: "bg-orange-50 text-orange-600",
  };

  return (
    <motion.div 
      variants={itemVariants} // 부모의 staggerChildren에 반응
      whileHover={{ y: -5, transition: { duration: 0.2 } }} // 호버 시 살짝 떠오름
      className="bg-white p-6 rounded-2xl shadow-sm border border-gray-100 flex flex-col justify-between h-full"
    >
      <div>
        <div className="flex justify-between items-start mb-4">
          <h3 className="text-gray-500 text-sm font-medium">{title}</h3>
          <button className="text-gray-400 hover:text-gray-600 transition-colors">
            <svg width="16" height="16" fill="currentColor" viewBox="0 0 24 24">
              <circle cx="12" cy="12" r="2" />
              <circle cx="12" cy="5" r="2" />
              <circle cx="12" cy="19" r="2" />
            </svg>
          </button>
        </div>
        
        <div className="text-3xl font-bold text-slate-800 mb-1">{value}</div>
      </div>

      <div className="mt-6 flex items-center justify-between">
        <span className="text-xs text-gray-400 font-medium">{description}</span>
        <div className={`px-2 py-1 rounded-full text-[10px] font-bold ${colorStyles[variant]}`}>
          {trend} {trendLabel}
        </div>
      </div>
    </motion.div>
  );
};

export default KpiCard;
