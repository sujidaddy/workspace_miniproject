// import React, { useState, useEffect } from 'react';
// import { AreaChart, Area, XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer } from 'recharts';
// import { fetchWeatherChartData } from '@/ApiList';

// // 파고데이터
// interface MapProps {
//   data?: any[];
//   locationNo?: number;
// }

// interface WaveData {
//   xAxisLabel: string;
//   minWvhgt: number;
//   maxWvhgt: number;
// }

// const Map1: React.FC<MapProps> = ({ data, locationNo = 1 }) => {
//   const [waveData, setWaveData] = useState<WaveData[]>([]);
//   const [loading, setLoading] = useState(false);

//   // 파고 데이터 fetch
//   useEffect(() => {
//     const fetchWaveData = async () => {
//       if (!locationNo) return;
      
//       setLoading(true);
//       try {
//         const result = await fetchWeatherChartData(locationNo, '파고');
//         if (result && result.data) {
//           setWaveData(result.data);
//         }
//       } catch (error) {
//         console.error('파고 데이터 로딩 실패:', error);
//       } finally {
//         setLoading(false);
//       }
//     };

//     fetchWaveData();
//   }, [locationNo]);

//   // 표시할 데이터 결정 (props로 받은 data가 있으면 우선 사용, 없으면 API에서 가져온 데이터 사용)
//   const chartData = data && data.length > 0 ? data : waveData;

//   if (loading) {
//     return (
//       <div className="flex items-center justify-center h-full">
//         <div className="text-gray-500">파고 데이터 로딩 중...</div>
//       </div>
//     );
//   }

//   return (
//     <ResponsiveContainer width="100%" height="100%">
//       <AreaChart data={chartData} margin={{ top: 5, right: 20, left: -10, bottom: 5 }}>
//         <CartesianGrid strokeDasharray="3 3" />
//         <XAxis 
//           dataKey="xAxisLabel" 
//           tick={{ fontSize: 12 }}
//           tickFormatter={(value) => {
//             // 날짜 형식이면 간단하게 표시
//             if (typeof value === 'string' && value.includes('-')) {
//               return value.split('-').slice(1).join('/'); // MM/DD 형식으로 변환
//             }
//             return value;
//           }}
//         />
//         <YAxis 
//           domain={[0, 'auto']}
//           tick={{ fontSize: 12 }}
//           label={{ value: '파고(m)', angle: -90, position: 'insideLeft' }}
//         />
//         <Tooltip 
//           formatter={(value: number | undefined, name: string | undefined) => [
//             value ? `${value}m` : '0m', 
//             name || ''
//           ]}
//           labelFormatter={(label) => `시간: ${label}`}
//         />
//         <Legend 
//           wrapperStyle={{
//             fontSize: '14px',
//             color: '#333333',
//             fontWeight: '500'
//           }}
//         />         
//         <Area 
//           type="monotone" 
//           dataKey="minWvhgt" 
//           name="최소파고(m)" 
//           stroke="#8884d8" 
//           fill="#8884d8" 
//           fillOpacity={0.6} 
//         />
//         <Area 
//           type="monotone" 
//           dataKey="maxWvhgt" 
//           name="최대파고(m)" 
//           stroke="#82ca9d" 
//           fill="#82ca9d" 
//           fillOpacity={0.6} 
//         />
//       </AreaChart>
//     </ResponsiveContainer>
//   );
// };

// export default Map1;

import React, { useState, useEffect } from 'react';
import { AreaChart, Area, XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer } from 'recharts';
import { fetchWeatherChartData } from '@/ApiList';


// 파고데이터
interface Map1Props {
  data?: any[];
  locationNo?: number;
}

interface WaveData {
  xAxisLabel: string;
  minWvhgt: number;
  maxWvhgt: number;
}

const Map1: React.FC<Map1Props> = ({ data, locationNo = 1 }) => {
  const [waveData, setWaveData] = useState<WaveData[]>([]);
  const [chartData, setChartData] = useState<WaveData[]>([]);
  const [loading, setLoading] = useState(false);
  console.log('파고 데이터 요청:', locationNo, '파고', data);

  useEffect(() => {
    if (!locationNo || !data) return;
    setWaveData(data);
  }, [data, locationNo]);

  useEffect(() => {
     // 표시할 데이터 결정 (props로 받은 data가 있으면 우선 사용, 없으면 API에서 가져온 데이터 사용)
  setChartData (data && data.length > 0 ? data : waveData);

  }, [waveData]);


    if (!locationNo) return;

  // 파고 데이터 fetch
  useEffect(() => {
    const fetchWaveData = async () => {
      if (!locationNo) return;
      
      // setLoading(true);
      // try {
      //   console.log('파고 데이터 요청:', locationNo, '파고');
      //   const result = await fetchWeatherChartData(locationNo, '파고');
      //   console.log('API 응답 전체:', result);
        
      //   // API 응답 구조에 따라 데이터 추출
      //   let chartData = [];
      //   if (result) {
      //     // 가능한 응답 구조들을 확인
      //     if (result.data) {
      //       chartData = result.data;
      //     } else if (Array.isArray(result)) {
      //       chartData = result;
      //     } else if (result.result) {
      //       chartData = result.result;
      //     } else {
      //       console.log('예상하지 못한 응답 구조:', result);
      //     }
          
      //     console.log('추출된 차트 데이터:', chartData);
      //     setWaveData(chartData);
      //   }
      // } catch (error) {
      //   console.error('파고 데이터 로딩 실패:', error);
      // } finally {
      //   setLoading(false);
      // }
    };

    // fetchWaveData();
  }, [locationNo]);

 
  if (loading) {
    return (
      <div className="flex items-center justify-center h-full">
        <div className="text-gray-500">파고 데이터 로딩 중...</div>
      </div>
    );
  }

  return (
    <ResponsiveContainer width="100%" height="100%">
      <AreaChart data={chartData} margin={{ top: 5, right: 20, left: -10, bottom: 5 }}>
        <CartesianGrid strokeDasharray="3 3" />
        <XAxis 
          dataKey="xAxisLabel" 
          tick={{ fontSize: 12 }}
          tickFormatter={(value) => {
            // 날짜 형식이면 간단하게 표시
            if (typeof value === 'string' && value.includes('-')) {
              return value.split('-').slice(1).join('/'); // MM/DD 형식으로 변환
            }
            return value;
          }}
        />
        <YAxis 
          domain={[0, 'auto']}
          tick={{ fontSize: 12 }}
          label={{ value: '파고(m)', angle: -90, position: 'insideLeft' }}
        />
        <Tooltip 
          formatter={(value: number | undefined, name: string | undefined) => [
            value ? `${value}m` : '0m', 
            name || ''
          ]}
          labelFormatter={(label) => `시간: ${label}`}
        />
        <Legend 
          wrapperStyle={{
            fontSize: '14px',
            color: '#333333',
            fontWeight: '500'
          }}
        />         
        <Area 
          type="monotone" 
          dataKey="minWvhgt" 
          name="최소파고(m)" 
          stroke="#8884d8" 
          fill="#8884d8" 
          fillOpacity={0.6} 
        />
        <Area 
          type="monotone" 
          dataKey="maxWvhgt" 
          name="최대파고(m)" 
          stroke="#82ca9d" 
          fill="#82ca9d" 
          fillOpacity={0.6} 
        />
      </AreaChart>
    </ResponsiveContainer>
  );
};

export default Map1;