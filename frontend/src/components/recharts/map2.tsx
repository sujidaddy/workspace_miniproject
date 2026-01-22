
import React, { useState, useEffect, useMemo } from 'react';
import { AreaChart, Area, XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer } from 'recharts';
import { fetchWeatherChartData } from '@/ApiList';

interface WindData {
  predcYmd: string;
  minValue: number;
  maxValue: number;
}
interface Map2Props {
  selectedLocation: string;  
  title?: string; 
  color?: string;
}
function Map2({ selectedLocation, title, color }: Map2Props) {

  const [windData, setWindData] = useState<WindData[]>([]);
  const [loading, setLoading] = useState(false);

  // 풍속 데이터 fetch
   useEffect(() => {
      const fetchWindData = async () => {
        if (!selectedLocation) return;
        
        setLoading(true);
        try {
          const result = await fetchWeatherChartData(selectedLocation, '풍속');
          if (result && result.data) {
            setWindData(result.data);
          }
        } catch (error) {
          console.error('풍속 데이터 로딩 실패:', error);
        } finally {
          setLoading(false);
        }
      };
      fetchWindData();
    }, [selectedLocation]);



  // 표시할 데이터 결정 (props로 받은 data가 있으면 우선 사용, 없으면 API에서 가져온 데이터 사용)
 
  console.log("windData", windData);


  return (
    <div style={{ width: '400px', height: '200px' }}>
        <ResponsiveContainer width="100%" height="100%">
            <AreaChart data={windData} margin={{ top: 10, right: 30, left: 0, bottom: 20 }}>
            <CartesianGrid strokeDasharray="3 3" />

            <XAxis dataKey="predcYmd"  
                  tick={{ fontSize: 8, fill: '#ffffff' }}/>         
            <YAxis 
                  label={{ value: '풍속 (m)', angle: -90, position: 'insideLeft',
                          style: { fontSize: 8 } }} tick={{ fontSize: 8, fill: '#ffffff' }} 
            />
        <Tooltip />
        <Legend 
          verticalAlign="top"
          wrapperStyle={{
            fontSize: '10px',
            fontFamily: 'Pretendard, sans-serif',
            fontWeight: '600',
            paddingTop: '0px'
          }}
        />
        <Area type="monotone" dataKey="minValue" name="최소풍속(m/s)" stroke="#8884d8" fill="#8884d8" fillOpacity={0.3} isAnimationActive={true}/>
        <Area type="monotone" dataKey="maxValue" name="최대풍속(m/s)" stroke="#82ca9d" fill="#82ca9d" fillOpacity={0.3} isAnimationActive={true}/>
      </AreaChart>
    </ResponsiveContainer>
    </div>
  );
};

export default Map2;
