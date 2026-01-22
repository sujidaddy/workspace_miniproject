"use client";

import { motion } from 'framer-motion';
// @ 대신 상대 경로(./) 사용
import KpiCard from './KpiCard';

// 1. 대시보드 데이터 구성 (이미지 수치 반영)
const kpiData = [
  {
    title: "Property Portfolio Control",
    value: "4860",
    description: "Properties Managed",
    trend: "▲ 30%",
    trendLabel: "Last Year",
    variant: "red" as const,
  },
  {
    title: "Portfolio Value Management",
    value: "$2B",
    description: "Asset Value Managed",
    trend: "▲ 60%",
    trendLabel: "Last Month",
    variant: "purple" as const,
  },
  {
    title: "Portfolio Divestitures",
    value: "1037",
    description: "Properties Sold",
    trend: "▼ 10%",
    trendLabel: "Last Year",
    variant: "orange" as const,
  },
];

// 2. 애니메이션 설정 (부모 컨테이너)
const containerVariants = {
  hidden: { opacity: 0 },
  visible: {
    opacity: 1,
    transition: {
      staggerChildren: 0.15, // 카드 사이의 등장 간격 (0.15초)
    },
  },
};

export default function KpiSection() {
  return (
    <motion.section 
      variants={containerVariants}
      initial="hidden"
      whileInView="visible"
      viewport={{ once: true }} // 스크롤 시 한 번만 실행
      className="grid grid-cols-1 md:grid-cols-3 gap-6 mb-8"
    >
      {kpiData.map((data, index) => (
        <KpiCard 
          key={index}
          {...data} // 데이터를 props로 전개
        />
      ))}
    </motion.section>
  );
}