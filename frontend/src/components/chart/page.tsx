// src/app/dashboard/chart/page.tsx
"use client";

import { useState, useEffect } from 'react';
import { 
  LineChart, 
  Line, 
  BarChart,
  Bar,
  XAxis, 
  YAxis, 
  CartesianGrid, 
  Tooltip, 
  Legend, 
  ResponsiveContainer 
} from 'recharts';

interface ChartData {
  month: string;
  offerPrice: number;
  listedPrice: number;
}

interface Statistics {
  portfolioControl: number;
  portfolioValue: string;
  diversifications: number;
}

export default function ChartPage() {
  const [salesData, setSalesData] = useState<ChartData[]>([]);
  const [statistics, setStatistics] = useState<Statistics | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [lastUpdate, setLastUpdate] = useState<Date>(new Date());

  const fetchData = async () => {
    try {
      setLoading(true);
      const response = await fetch('/api/chart-data', {
        cache: 'no-store',
      });
      
      if (!response.ok) {
        throw new Error('데이터를 불러오는데 실패했습니다');
      }
      
      const data = await response.json();
      setSalesData(data.salesData);
      setStatistics(data.statistics);
      setLastUpdate(new Date());
      setError(null);
    } catch (err) {
      setError(err instanceof Error ? err.message : '알 수 없는 오류가 발생했습니다');
      console.error('Fetch Error:', err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchData();
    const interval = setInterval(fetchData, 30000);
    return () => clearInterval(interval);
  }, []);

  if (loading && !salesData.length) {
    return (
      <div className="flex items-center justify-center h-screen">
        <div className="text-center">
          <div className="animate-spin rounded-full h-16 w-16 border-b-2 border-blue-600 mx-auto"></div>
          <p className="mt-4 text-gray-600">데이터를 불러오는 중...</p>
        </div>
      </div>
    );
  }

  if (error && !salesData.length) {
    return (
      <div className="flex items-center justify-center h-screen">
        <div className="text-center bg-red-50 p-8 rounded-lg max-w-md">
          <p className="text-red-600 font-semibold mb-2">⚠️ 데이터 로딩 실패</p>
          <p className="text-gray-600 text-sm mb-4">{error}</p>
          <button 
            onClick={fetchData}
            className="px-6 py-2 bg-red-600 text-white rounded-lg hover:bg-red-700"
          >
            다시 시도
          </button>
        </div>
      </div>
    );
  }

  return (
    <div className="space-y-6">
      {/* 데이터베이스 연결 상태 */}
      <div className="bg-blue-50 border border-blue-200 rounded-lg p-4 flex items-center justify-between">
        <div className="flex items-center gap-2">
          <div className="w-3 h-3 bg-blue-500 rounded-full animate-pulse"></div>
          <span className="text-blue-700 font-medium">Database Connected</span>
        </div>
        <div className="flex items-center gap-4">
          <span className="text-blue-600 text-sm">
            마지막 업데이트: {lastUpdate.toLocaleTimeString('ko-KR')}
          </span>
          <button
            onClick={fetchData}
            disabled={loading}
            className="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 disabled:opacity-50 text-sm transition-colors"
          >
            {loading ? '업데이트 중...' : '🔄 새로고침'}
          </button>
        </div>
      </div>

      {/* 상단 통계 카드 */}
      <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
        <div className="bg-white rounded-lg shadow p-6 hover:shadow-lg transition-shadow">
          <div className="flex justify-between items-start">
            <div>
              <p className="text-gray-500 text-sm">Property Portfolio Control</p>
              <p className="text-3xl font-bold text-gray-800 mt-2">
                {statistics?.portfolioControl.toLocaleString() || '0'}
              </p>
              <p className="text-gray-400 text-xs mt-1">Target last 30days</p>
            </div>
            <span className="px-3 py-1 bg-red-100 text-red-600 text-xs rounded-full">-8% Goal</span>
          </div>
        </div>
        
        <div className="bg-white rounded-lg shadow p-6 hover:shadow-lg transition-shadow">
          <div className="flex justify-between items-start">
            <div>
              <p className="text-gray-500 text-sm">Portfolio Value Management</p>
              <p className="text-3xl font-bold text-gray-800 mt-2">
                ${statistics?.portfolioValue || '0'}
              </p>
              <p className="text-gray-400 text-xs mt-1">Asset Value Managed</p>
            </div>
            <span className="px-3 py-1 bg-purple-100 text-purple-600 text-xs rounded-full">Achievement</span>
          </div>
        </div>
        
        <div className="bg-white rounded-lg shadow p-6 hover:shadow-lg transition-shadow">
          <div className="flex justify-between items-start">
            <div>
              <p className="text-gray-500 text-sm">Portfolio Diversifications</p>
              <p className="text-3xl font-bold text-gray-800 mt-2">
                {statistics?.diversifications.toLocaleString() || '0'}
              </p>
              <p className="text-gray-400 text-xs mt-1">Property Unit</p>
            </div>
            <span className="px-3 py-1 bg-red-100 text-red-600 text-xs rounded-full">-2% Openings</span>
          </div>
        </div>
      </div>

      {/* 메인 Line 차트 */}
      <div className="bg-white p-8 rounded-3xl border border-gray-100 shadow-sm hover:shadow-lg transition-shadow">
        <div className="flex justify-between items-center mb-6">
          <div>
            <h3 className="text-xl font-bold text-gray-800">
              Listed vs Offer Price for Rejections
            </h3>
            <p className="text-sm text-gray-500 mt-1">
              총 {salesData.length}개월 데이터
            </p>
          </div>
          <select className="px-4 py-2 border border-gray-200 rounded-lg text-sm text-gray-600 hover:border-gray-300 transition-colors">
            <option>Last Month</option>
            <option>Last 3 Months</option>
            <option>Last 6 Months</option>
            <option>Last Year</option>
          </select>
        </div>

        {salesData.length > 0 ? (
          <ResponsiveContainer width="100%" height={450}>
            <LineChart data={salesData}>
              <CartesianGrid strokeDasharray="3 3" stroke="#f0f0f0" vertical={false} />
              <XAxis 
                dataKey="month" 
                tick={{ fill: '#9CA3AF', fontSize: 12 }}
                axisLine={{ stroke: '#E5E7EB' }}
                tickLine={false}
              />
              <YAxis 
                tick={{ fill: '#9CA3AF', fontSize: 12 }}
                axisLine={{ stroke: '#E5E7EB' }}
                tickLine={false}
                domain={[0, 100]}
                ticks={[0, 20, 40, 60, 80, 100]}
                label={{ value: '%', position: 'top', offset: 10, fill: '#9CA3AF' }}
              />
              <Tooltip 
                contentStyle={{ 
                  backgroundColor: '#fff',
                  border: '1px solid #E5E7EB',
                  borderRadius: '12px',
                  boxShadow: '0 4px 12px rgba(0,0,0,0.1)',
                  padding: '12px'
                }}
                labelStyle={{ fontWeight: 'bold', marginBottom: '8px' }}
              />
              <Legend 
                wrapperStyle={{ paddingTop: '30px' }}
                iconType="circle"
                iconSize={10}
              />
              <Line 
                type="monotone" 
                dataKey="offerPrice" 
                stroke="#EF4444" 
                strokeWidth={3}
                dot={{ fill: '#EF4444', r: 5, strokeWidth: 2, stroke: '#fff' }}
                name="Offer Price"
                activeDot={{ r: 7 }}
              />
              <Line 
                type="monotone" 
                dataKey="listedPrice" 
                stroke="#8B5CF6" 
                strokeWidth={3}
                dot={{ fill: '#8B5CF6', r: 5, strokeWidth: 2, stroke: '#fff' }}
                name="Listed Price"
                activeDot={{ r: 7 }}
              />
            </LineChart>
          </ResponsiveContainer>
        ) : (
          <div className="h-[450px] flex items-center justify-center text-gray-400">
            데이터가 없습니다
          </div>
        )}
      </div>

      {/* 추가 차트들 */}
      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        
        {/* Bar Chart */}
        <div className="bg-white p-8 rounded-3xl border border-gray-100 shadow-sm h-[450px] hover:shadow-lg transition-shadow">
          <h3 className="text-xl font-bold text-gray-800 mb-6">Bar Chart Visualization</h3>
          {salesData.length > 0 ? (
            <ResponsiveContainer width="100%" height={320}>
              <BarChart data={salesData}>
                <CartesianGrid strokeDasharray="3 3" stroke="#f0f0f0" vertical={false} />
                <XAxis 
                  dataKey="month" 
                  tick={{ fill: '#9CA3AF', fontSize: 11 }}
                  axisLine={{ stroke: '#E5E7EB' }}
                  tickLine={false}
                />
                <YAxis 
                  tick={{ fill: '#9CA3AF', fontSize: 11 }}
                  axisLine={{ stroke: '#E5E7EB' }}
                  tickLine={false}
                />
                <Tooltip 
                  contentStyle={{ 
                    backgroundColor: '#fff',
                    border: '1px solid #E5E7EB',
                    borderRadius: '12px',
                    boxShadow: '0 4px 12px rgba(0,0,0,0.1)'
                  }}
                />
                <Legend iconType="circle" iconSize={10} />
                <Bar dataKey="offerPrice" fill="#EF4444" radius={[8, 8, 0, 0]} name="Offer Price" />
                <Bar dataKey="listedPrice" fill="#8B5CF6" radius={[8, 8, 0, 0]} name="Listed Price" />
              </BarChart>
            </ResponsiveContainer>
          ) : (
            <div className="h-[320px] flex items-center justify-center text-gray-400">
              데이터가 없습니다
            </div>
          )}
        </div>

        {/* Trend Chart */}
        <div className="bg-white p-8 rounded-3xl border border-gray-100 shadow-sm h-[450px] hover:shadow-lg transition-shadow">
          <h3 className="text-xl font-bold text-gray-800 mb-6">Price Trend Analysis</h3>
          {salesData.length > 0 ? (
            <ResponsiveContainer width="100%" height={320}>
              <LineChart data={salesData}>
                <CartesianGrid strokeDasharray="3 3" stroke="#f0f0f0" />
                <XAxis 
                  dataKey="month" 
                  tick={{ fill: '#9CA3AF', fontSize: 11 }}
                  axisLine={{ stroke: '#E5E7EB' }}
                />
                <YAxis 
                  tick={{ fill: '#9CA3AF', fontSize: 11 }}
                  axisLine={{ stroke: '#E5E7EB' }}
                />
                <Tooltip 
                  contentStyle={{ 
                    backgroundColor: '#fff',
                    border: '1px solid #E5E7EB',
                    borderRadius: '12px'
                  }}
                />
                <Legend iconType="circle" iconSize={10} />
                <Line 
                  type="monotone" 
                  dataKey="offerPrice" 
                  stroke="#EF4444" 
                  strokeWidth={4}
                  dot={false}
                  name="Offer"
                />
                <Line 
                  type="monotone" 
                  dataKey="listedPrice" 
                  stroke="#8B5CF6" 
                  strokeWidth={4}
                  dot={false}
                  name="Listed"
                />
              </LineChart>
            </ResponsiveContainer>
          ) : (
            <div className="h-[320px] flex items-center justify-center text-gray-400">
              데이터가 없습니다
            </div>
          )}
        </div>

      </div>
    </div>
  );
}