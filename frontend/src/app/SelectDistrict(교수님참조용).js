import { useState, useEffect } from 'react';

const DistrictSelect = () => {

  const [sdList, setSdList] = useState([]); // 시/도 목록
  const [sggList, setSggList] = useState([]); // 시/군/구 목록
  const [selectedSd, setSelectedSd] = useState(''); // 선택된 시/도 ID
  const [selectedSgg, setSelectedSgg] = useState(''); // 선택된 시/군/구 ID
  const [data, setData] = useState(''); // 선택된 시/군/구 데이터

  // 2. 초기 렌더링 시 '시/도' 목록 가져오기
  useEffect(() => {
    fetch(`http://localhost:8080/api/sido`)
      .then(res => res.json())
      .then(data => setSdList(data))
      .catch(err => console.error("시/도 로딩 실패:", err));
  }, []);

  // 3. 시/도(selectedSd)가 변경될 때마다 실행
  useEffect(() => {
    if (!selectedSd) {
      setSggList([]); // 시/도 선택이 풀리면 시군구 목록 초기화
      return;
    }
    fetch(`http://localhost:8080/api/sigungu?sidoId=${selectedSd}`)
      .then(res => res.json())
      .then(data => {
        setSggList(data);
        setSelectedSgg(''); // 시/도가 바뀌면 기존 시/군/구 선택값 초기화
      })
      .catch(err => console.error("시/군/구 로딩 실패:", err));
  }, [selectedSd]);

  // 3. 시군구(selectedSgg)가 변경될 때마다 실행
  useEffect(() => {
    if (!selectedSgg) {
      return;
    }    
    fetch(`http://localhost:8080/api/chart?location_no=${selectedSgg}`)
      .then(res => res.text())
      .then(data => {
        console.log("data", data);
        setData(data);
      })
      .catch(err => console.error("Chart 데이터 로딩 실패:", err));
  }, [selectedSgg]);

  return (
    <div style={{ display: 'flex', flexDirection: 'column' }}>
      <div style={{ display: 'flex', gap: '10px', padding: '20px' }}>
        {/* 시/도 선택 콤보박스 */}
        <select 
          value={selectedSd} 
          onChange={(e) => {
              console.log("선택된 시도 ID:", e.target.value);
              setSelectedSd(e.target.value);
          }}>
          <option value="">시/도 선택</option>
          {sdList.map(item => (
            <option key={item.id} value={item.id}>
              {item.name}
            </option>
          ))}
        </select>
        {/* 시/군/구 선택 콤보박스 */}
        <select 
          value={selectedSgg} 
          onChange={(e) => {
              console.log("선택된 시군구 ID:", e.target.value);
              setSelectedSgg(e.target.value);
          }}
          disabled={!selectedSd}>
          <option value="">시/군/구 선택</option>
          {sggList.map(item => (
            <option key={item.id} value={item.id}>
              {item.name}
            </option>
          ))}
        </select>
      </div>
      <div style={{ display: 'flex', gap: '10px', padding: '20px' }}>
        <h2>{data}</h2>
      </div>
    </div>
  );
};
export default DistrictSelect;
