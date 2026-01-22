"use client";
interface ForecastPanelProps {
  data: any[];
}

export default function ForecastPanel({data}: ForecastPanelProps) {
  const totalSpots = data.length;
  const avgScore = data.length > 0
    ? (data.reduce((acc, cur) => acc + cur.lastScr, 0) / data.length).toFixed(1)
    : "0.0";
  
  const indexStats = {
    매우좋음: data.filter((item) => item.totalIndex === "매우좋음").length,
    좋음: data.filter((item) => item.totalIndex === "좋음").length,
    보통: data.filter((item) => item.totalIndex === "보통").length,
    나쁨: data.filter((item) => item.totalIndex === "나쁨").length,
  };
  
  const goodSpots = indexStats.매우좋음 + indexStats.좋음;

  return (
    <div className="grid grid-cols-3 gap-8 mb-10">
      {/* 패널 1 - totalSpots 사용 */}
      <div className="bg-gradient-to-br from-blue-500 to-blue-600 text-white p-8 rounded-[2rem] shadow-lg">
        <p className="text-sm font-semibold opacity-90">총 낚시터</p>
        <p className="text-5xl font-black mt-2">{totalSpots}</p>
        <p className="text-xs mt-2 opacity-75">개 지역</p>
      </div>

      {/* 패널 2 - avgScore 사용 */}
      <div className="bg-gradient-to-br from-green-500 to-green-600 text-white p-8 rounded-[2rem] shadow-lg">
        <p className="text-sm font-semibold opacity-90">평균 점수</p>
        <p className="text-5xl font-black mt-2">{avgScore}</p>
        <p className="text-xs mt-2 opacity-75">종합 지수</p>
      </div>

      {/* 패널 3 - goodSpots 사용 */}
      <div className="bg-gradient-to-br from-purple-500 to-purple-600 text-white p-8 rounded-[2rem] shadow-lg">
        <p className="text-sm font-semibold opacity-90">추천 지역</p>
        <p className="text-5xl font-black mt-2">{goodSpots}</p>
        <p className="text-xs mt-2 opacity-75">좋음 이상</p>
      </div>
    </div>
  );
}
//     <div className="grid grid-cols-1 lg:grid-cols-3 gap-6 w-full mb-10">
      
//       {/* [1] 어종별 민감도 순위 */}
//       <div className="bg-white p-8 rounded-[2.5rem] shadow-sm border border-gray-50 flex flex-col h-60">
//         <div className="flex justify-between items-center mb-6">
//           <h4 className="font-bold text-slate-800">어종별 출조 최적도</h4>
//           <span className="text-[10px] text-blue-500 font-bold bg-blue-50 px-2 py-1 rounded-md">LIVE</span>
//         </div>
//         <div className="space-y-4">
//           <div className="flex justify-between items-center text-sm font-bold">
//             <span className="text-slate-600">감성돔 (가거도)</span>
//             <span className="text-[#FF5A3D]">82%</span>
//           </div>
//           <div className="w-full h-2 bg-gray-50 rounded-full overflow-hidden">
//             <div className="h-full bg-gradient-to-r from-orange-400 to-[#FF5A3D]" style={{ width: '82%' }}></div>
//           </div>
//         </div>
//       </div>

//       {/* [2] 중앙: 통합 지수 (가장 가시성이 높아야 함) */}
//       <div className="bg-slate-900 p-8 rounded-[2.5rem] shadow-xl flex flex-col items-center justify-center h-60 text-white relative overflow-hidden">
//         <div className="absolute top-0 right-0 w-32 h-32 bg-[#FF5A3D]/10 rounded-full -mr-16 -mt-16 blur-3xl"></div>
//         <p className="text-slate-400 text-xs font-bold uppercase tracking-widest mb-2">Total Fishing Index</p>
//         <h2 className="text-6xl font-black italic">74.5</h2>
//         <p className="text-emerald-400 text-sm mt-2 font-bold">▲ 4.2% Increase</p>
//       </div>
//     </div>
//   );
// }