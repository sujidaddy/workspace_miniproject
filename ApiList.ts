'use client'
import { getJWTToken } from "@/components/JWTToken";

//const baseUrl = 'http://10.125.121.176:8080/api/v1'
const baseUrl = 'http://192.168.219.105:8080/api/v1'

/* 1 ID/비밀번호 로그인 */
export const fetchLoginId = async (userid: string, password: string) => {
    try {
        const response = await fetch(`${baseUrl}/loginid`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ userid : userid, password :password }),
        });

        if (!response.ok)
            return null;

        return await response.json();
    } catch (error) {
        console.log(error);
    }
}

/* 2, 3, 4 소셜 로그인 provider : 'google', 'naver', 'kakao' */
export const fetchLoginProvider = async (provider: string, token: string) => {
    try {
        const response = await fetch(`${baseUrl}/login${provider}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ text : token }),
        });

        if (!response.ok)
            return null;

        return await response.json();
    } catch (error) {
        console.log(error);
    }
}

/* 5 ID 중복 확인 */
export const fetchJoinValidateID = async (user_id : string) => {
    try {
        const response = await fetch(`${baseUrl}/join/validateID`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ text : user_id }),
        });

        if (!response.ok)
            return null;

        return await response.json();
    } catch(error) {
        console.log(error);
    }
}

/* 6 이메일 중복 확인 */
export const fetchJoinValidateEmail = async (email : string) => {
    try {
        const response = await fetch(`${baseUrl}/join/validateEmail`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ text : email }),
        });

        if (!response.ok)
            return null;

        return await response.json();
    } catch(error) {
        console.log(error);
    }
}

/* 7, 8, 9 소셜계정 중복 확인 */
export const fetchJoinValidateProvider = async (provider : string, user: string) => {
    try {
        const response = await fetch(`${baseUrl}/join/validate${provider}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ text : user }),
        });

        if (!response.ok)
            return null;

        return await response.json();
    } catch(error) {
        console.log(error);
    }
}

/* 10 회원 가입 */
export const fetchJoinJoinUser = async (
userid: string, 
password: string, 
username: string, 
email: string,
google: string | null,
naver: string | null,
kakao: string | null) => {
    try {
        const response = await fetch(`${baseUrl}/join/joinUser`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                userid: userid,
                password: password,
                username: username,
                email: email,
                google: google,
                naver: naver,
                kakao: kakao,
            }),
        });

        if (!response.ok)
            return null;

        return await response.json();
    } catch(error) {
        console.log(error);
    }
}

/* 11 회원 정보 수정 */
export const fetchJoinModifyUser = async (currentPassword: string, newPassword: string, newUsername: string) => {
    try {
        const response = await fetch(`${baseUrl}/join/modifyUser`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': await getJWTToken(),
            },
                body: JSON.stringify({
                    currentPassword: currentPassword,
                    newPassword: newPassword,
                    newUsername: newUsername,
                }),
            });

        if (!response.ok)
            return null;

        return await response.json();
    } catch(error) {
        console.log(error);
    }
}

/* 12 권역 구분 정보 조회 */
export const fetchAreaData = async () => {
    try {
        const response = await fetch(`${baseUrl}/area`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
            },
        });

        if (!response.ok)
            return null;

        return await response.json();
    } catch (error) {
        console.log(error);
    }
    return null;
};

/* 13 전체 위치 정보 조회 */
export const fetchLocationData = async () => {
    try {
        const response = await fetch(`${baseUrl}/location`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
            },
        });

        if (!response.ok)
            return null;

        return await response.json();
    } catch (error) {
        console.log(error);
    }
    return null;
};

/* 14 권역별 위치 정보 조회 */
export const fetchLocationAreaData = async (area_no : number) => {
    try {
        const response = await fetch(`${baseUrl}/location/area`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ number : area_no }),
        });

        if (!response.ok)
            return null;

        return await response.json();
    } catch (error) {
        console.log(error);
    }
    return null;
};

/* 15 위치 선택 (공공데이터의 1주일간의 예측치) */
export const fetchLocationSelectData = async (location_no: number) => {
    try {
        let response = await fetch(`${baseUrl}/location/select`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ number: location_no })
        });

        if (!response.ok)
            return null;

        return await response.json();
    } catch (error) {
        console.log(error);
    }
}

/* 16 즐겨찾기 조회 */
export const fetchLocationFavoriteData = async () => {
    try {
        const response = await fetch(`${baseUrl}/location/favorite`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': await getJWTToken(),
            },
        });

        if (!response.ok)
            return null;

        return await response.json();
    } catch (error) {
        console.log(error);
    }
    return null;
};

/* 17 즐겨찾기 추가 */
export const fetchFavoriteAdd = async(location_no: number) => {
    try {
        const response = await fetch(`${baseUrl}/location/favorite/add`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization' : await getJWTToken(),
            },
            body: JSON.stringify({number : location_no}),
        });

        if (!response.ok)
            return null;

        return await response.json();
    } catch(error) {
        console.log(error);
    }
}

/* 18 즐겨찾기 제거 */
export const fetchFavoriteRemove = async(location_no: number) => {
    try {
        const response = await fetch(`${baseUrl}/location/favorite/remove`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization' : await getJWTToken(),
            },
            body: JSON.stringify({number : location_no}),
        });

        if (!response.ok)
            return null;

        return await response.json();
    } catch(error) {
        console.log(error);
    }
}

/* 19 어종이름 검색을 통한 낚시가능어종 정보 조회 */
export const fetchFishNameSearch = async (name: string) => {
    try {
        const response = await fetch(`${baseUrl}/fish/name`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ text: name }),
        });

        if (!response.ok)
            return null;

        return await response.json();
    } catch (error) {
        console.log(error);
    }
}

/* 20 위치 별 낚시가능어종 정보 조회 */
export const fetchFishLocationSearch = async (location_no: number) => {
    try {
        const response = await fetch(`${baseUrl}/fish/location`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ number: location_no }),
        });

        if (!response.ok)
            return null;

        return await response.json();
    } catch (error) {
        console.log(error);
    }
}

/* 21 날짜 별 낚시가능어종 정보 조회 */
export const fetchFishWeatherSearch = async (location_no: number, date : string) => {
    try {
        const response = await fetch(`${baseUrl}/fish/weather`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ number: location_no, text: date }),
        });

        if (!response.ok)
            return null;

        return await response.json();
    } catch (error) {
        console.log(error);
    }
}

/* 22 낚시 가능한 전체 어종 정보 */
export const fetchAdminFishListData = async () => {
    try {
        const response = await fetch(`${baseUrl}/fishlist`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': await getJWTToken()
            },
        });

        if (!response.ok)
            return null;

        return await response.json();
    } catch (error) {
        console.log(error);
    }
}

/* 23 선택 위치의 날씨 차트 정보 */
export const fetchWeatherChartData = async(
location_no: number, 
type: string) => {
    try {
        const respone = await fetch(`${baseUrl}/weather/chart`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                number: location_no,
                text: type
            })
        });

        if(!respone.ok)
            return null;
        return await respone.json();
    } catch(error) {
        console.log(error);
    }
}

/* 24 금일 낚시 정보 갱신(공공데이터를 DB로 가져옴) */
export const fetchAdminFetch = async () => {
    try {
        const response = await fetch(`${baseUrl}/admin/fetchData`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': await getJWTToken(),
            },
        });

        if (!response.ok)
            return null;

        return await response.json();
    } catch (error) {
        console.log(error);
    }
}

/* 25 관리페이지에서 어류설명 리스트 */
export const fetchAdminFishListAllData = async () => {
    try {
        const response = await fetch(`${baseUrl}/admin/fishlistAll`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': await getJWTToken()
            },
        });

        if (!response.ok)
            return null;

        return await response.json();
    } catch (error) {
        console.log(error);
    }
}

/* 26 관리페이지에서 어류설명 수정 */
export const fetchAdminModifyFishDetail = async (
    data_no: number,
    name: string,
    detail: string,
    url: string,
) => {
    try {
        const response = await fetch(`${baseUrl}/admin/modifyFishDetail`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': await getJWTToken()
            },
            body: JSON.stringify({
                data_no: data_no,
                name: name,
                detail: detail,
                url: url,
            }),
        });

        if (!response.ok)
            return null;

        return await response.json();
    } catch (error) {
        console.log(error);
    }
}

/* 27 관리페이지에서 어류설명 삭제 */
export const fetchRemoveFishDetail = async (data_no: number) => {
    try {
        const response = await fetch(`${baseUrl}/admin/removeFishDetail`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': await getJWTToken()
            },
            body: JSON.stringify({
                data_no: data_no,
            }),
        });

        if (!response.ok)
            return;

        return await response.json();
    } catch (error) {
        console.error(error);
    }
}

/* 28 관리페이지에서 이용자 리스트 */
export const fetchAdminMemberListData = async (pageNo: number, numOfRows: number) => {
    try {
        const response = await fetch(`${baseUrl}/admin/memberlist`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': await getJWTToken()
            },
            body: JSON.stringify({
                pageNo: pageNo,
                numOfRows: numOfRows,
            }),
        });

        if (!response.ok)
            return;

        return await response.json();
    } catch (error) {
        console.error(error);
    }
}

/* 29 관리페이지에서 이용자의 활성/비활성화 */
export const fetchAdminModifyUserEnabled = async (user_id: number, enabled: boolean) => {
    try {
        const response = await fetch(`${baseUrl}/admin/modifyUserEnabled`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': await getJWTToken()
            },
            body: JSON.stringify({
                user_no: user_id,
                enabled: enabled
            }),
        });

        if (!response.ok)
            return null;

        return await response.json();
    } catch (error) {
        console.log(error);
    }
}

/* 30 관리페이지에서 데이터패치 로그 리스트(오류확인용) */
export const fetchLogListData = async (
    currentPageNo: number,
    numOfRows: number,
    dataType: string,
) => {
    try {
        //console.log('dataType:', dataType);
        const response = await fetch(`${baseUrl}/admin/fetchLogList`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': await getJWTToken()
            },
            body: JSON.stringify({
                pageNo: currentPageNo,
                numOfRows: numOfRows,
                type: dataType,
            }),
        });

        if (!response.ok)
            return null;

        return await response.json();
    } catch (error) {
        console.log(error);
    }
};

export const fetchFishTopPointData = async () => {
    try {
        const count: number = 3;
        const response = await fetch(`${baseUrl}/fish/topPoint`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ number: count }),
        });

        if (!response.ok)
            return null;

        return await response.json();
    } catch (error) {
        console.log(error);
    }
    return null;
};































