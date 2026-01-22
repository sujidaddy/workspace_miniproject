
import React, { useState, useEffect, useMemo } from 'react';
import { AreaChart, Area, XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer } from 'recharts';
import { fetchWeatherChartData } from '@/ApiList';
// 수온데이터
interface TempData {
  predcYmd: string;
  minValue: number;
  maxValue: number;
}
interface Map3Props {
  selectedLocation: string;  
  title?: string; 
  color?: string;
}

function Map3({ selectedLocation, title, color }: Map3Props) {
  
  const [tempData, setTempData] = useState<TempData[]>([]);
  const [loading, setLoading] = useState(false);

   useEffect(() => {
      const fetchTempData = async () => {
        if (!selectedLocation) return;
        
        setLoading(true);
        try {
          const result = await fetchWeatherChartData(selectedLocation, '수온');
          if (result && result.data) {
            setTempData(result.data);
          }
        } catch (error) {
          console.error('수온 데이터 로딩 실패:', error);
        } finally {
          setLoading(false);
        }
      };
      fetchTempData();
    }, [selectedLocation]);

     console.log("tempData", tempData);

  return (
      <div style={{ width: '400px', height: '200px' }}>
          <ResponsiveContainer width="100%" height="100%">
              <AreaChart data={tempData} margin={{ top: 10, right: 30, left: 0, bottom: 20 }}>
              <CartesianGrid strokeDasharray="3 3" />
                <XAxis dataKey="predcYmd" 
                        tick={{ fontSize: 8, fill: '#ffffff' }}/>                
                <YAxis 
                    label={{ value: '수온 (°C', angle: -90, position: 'insideLeft',
                              style: { fontSize: 8 } }} tick={{ fontSize: 8, fill: '#ffffff' }}
                />
        <Tooltip />
        <Legend 
          verticalAlign="top"
          wrapperStyle={{
            fontSize: '10px',
            fontFamily: 'Pretendard, sans-serif',
            fontWeight: '600',
          }}
        />
        <Area type="monotone" dataKey="minValue" name="최소수온(°C)" stroke="#00C49F" fill="#00C49F" fillOpacity={0.3} isAnimationActive={true}/>
        <Area type="monotone" dataKey="maxValue" name="최대수온(°C)" stroke="#FFBB28" fill="#FFBB28" fillOpacity={0.3} isAnimationActive={true}/>
        
      </AreaChart>
    </ResponsiveContainer>
    </div>
  );
};
export default Map3;
