-- FetchData 성능 최적화를 위한 인덱스 생성 스크립트

-- 1. Location 테이블 인덱스
-- name으로 빠르게 조회하기 위한 인덱스
ALTER TABLE miniproject_location ADD INDEX idx_name (name);

-- 2. Weather 테이블 인덱스
-- location_no + predc_ymd + predc_noon_se_cd 조합 조회 최적화
ALTER TABLE miniproject_weather ADD INDEX idx_location_predc_ymd_noon (location_no, predc_ymd, predc_noon_se_cd);

-- location_no + predc_ymd 조회 최적화
ALTER TABLE miniproject_weather ADD INDEX idx_location_predc_ymd (location_no, predc_ymd);

-- 3. Fish 테이블 인덱스
-- location_no + weather_no + name 조합 조회 최적화
ALTER TABLE miniproject_fish ADD INDEX idx_location_weather_name (location_no, weather_no, name);

-- location_no 조회 최적화
ALTER TABLE miniproject_fish ADD INDEX idx_location_no (location_no);

-- name 조회 최적화
ALTER TABLE miniproject_fish ADD INDEX idx_name (name);

-- 4. FishDetail 테이블 인덱스
-- name 조회 최적화
ALTER TABLE miniproject_fish_detail ADD INDEX idx_name (name);

-- 5. 복합 조회 최적화
-- Fish 테이블의 weather_no 외래키 인덱스 확인
ALTER TABLE miniproject_fish ADD INDEX idx_weather_no (weather_no);

-- 데이터베이스 통계 업데이트 (MySQL)
ANALYZE TABLE miniproject_location;
ANALYZE TABLE miniproject_weather;
ANALYZE TABLE miniproject_fish;
ANALYZE TABLE miniproject_fish_detail;
ANALYZE TABLE miniproject_fetch_log;
