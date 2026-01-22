// import axios from "axios";
// // import axios from '../api/axios';
// import { useContext, useEffect, useState } from 'react'
// // import styled from 'styled-components';
// // import { stateContext } from '../utils/stateContext'
// // 파도 움직이는 배경
// const instance = axios.create({
//     baseURL: 'https://api.unsplash.com/photos/random',
//     params: {
//         client_id: process.env.REACT_APP_MOTIVATION_API_KEY,
//         count: 30,
//     }
// });

// const fetchData = async () => {
//   const imgData = await axios.get('', {
//       params: {
//           query: { query }
//       }
//   });
// };
// export default instance;

// // 
// import axios from '../api/axios';
// import { useContext, useEffect, useState } from 'react'
// // import styled from 'styled-components';
// // import { stateContext } from '../utils/stateContext'

// const Background = () => {
//     const [backImgUrl, setBackImgUrl] = useState("");
//     // const { query } = useContext(stateContext);

// // 쿼리 값이 변화가 있을 때마다 fetchData() 를 불러 옵니다.
//     useEffect(() => {
//         fetchData();
//     }, [query]);

// // 이미지 크기 조절을 위한 함수
//     const settleImg = (imgData) => {
//         let index = 0;
//         while (true) {
//             if (imgData[index].width / imgData[index].height > 1) {
//                 setBackImgUrl(imgData[index].urls['full']);
//                 break;
//             }
//             index++;
//         }
//     }

//     const fetchData = async () => {
//         const imgData = await axios.get('', {
//             params: {
//                 query: { query }
//             }
//         });
//         settleImg(imgData.data);
//     };

//     return (
//         <>
//             <BackgroundImg
//                 style={{
//                     backgroundImage: `linear-gradient( rgba(0, 0, 0, 0.7), rgba(0, 0, 0, 0.7) )`,
//                     backgroundImage: `linear-gradient( rgba(0, 0, 0, 0.7), rgba(0, 0, 0, 0.7) ), url(${backImgUrl})`,
//                     backgroundPosition: "top center",
//                     backgroundSize: "cover",
//                 }} />
//         </>
//     )
// }

// export default Background;

// // const BackgroundImg = styled.div`
// //     background-size:cover;
// //     background-repeat: no-repeat;
// //     position: absolute;
// //     max-height: 100%;
// //     margin:0;
// //     padding:0;
// //     width: 100vw;
// //     height: 100vh;
// //     z-index: -1;
// //     opacity: 0.8;
// `