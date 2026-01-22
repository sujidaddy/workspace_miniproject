import React, {useState, useEffect, useMemo} from 'react';
import { AreaChart, Area, XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer } from 'recharts';
import { fetchWeatherChartData } from '@/ApiList';

//유속(조류) 데이터
interface CrspData {
  predcYmd: string;
  minValue: number;
  maxValue: number;
}
interface Map4Props {
  selectedLocation: string;  
  title?: string; 
  color?: string;
}
function Map4({ selectedLocation, title, color }: Map4Props) {
  
  const [crspData, setCrspData] = useState<CrspData[]>([]);
  const [loading, setLoading] = useState(false);

useEffect(() => {
      const fetchCrspData = async () => {
        if (!selectedLocation) return;
        
        setLoading(true);
        try {
          const result = await fetchWeatherChartData(selectedLocation, '유속');
          if (result && result.data) {
            setCrspData(result.data);
          }
        } catch (error) {
          console.error('유속 데이터 로딩 실패:', error);
        } finally {
          setLoading(false);
        }
      };
      fetchCrspData();
    }, [selectedLocation]);

    console.log("crspData", crspData);

  return (
      <div style={{ width: '400px', height: '200px' }}>
          <ResponsiveContainer width="100%" height="100%">
              <AreaChart data={crspData} margin={{ top: 10, right: 30, left: 0, bottom: 20 }}>
              <CartesianGrid strokeDasharray="3 3" />
                <XAxis dataKey="predcYmd" 
                tick={{ fontSize: 8, fill: '#ffffff' }}/>
                <YAxis 
                      label={{value:'유속(m/s)', angle:-15, position:'insideLeft',
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
            <Area type="monotone" dataKey="minValue" name="최소유속(m/s)" stroke="#0088FE" fill="#0088FE" fillOpacity={0.6} isAnimationActive={true}/>
            <Area type="monotone" dataKey="maxValue" name="최대유속(m/s)" stroke="#FF8042" fill="#FF8042" fillOpacity={0.6} isAnimationActive={true}/>
      </AreaChart>
    </ResponsiveContainer>
    </div>
  );
};
export default Map4;


