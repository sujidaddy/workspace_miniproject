import type { NextConfig } from "next";

// next.config.ts
const nextConfig = {
  images: { // 이미지 저장 위치
    remotePatterns: [
      {
        protocol: "https",
        hostname: "localhost:3000",
        port: "",
        pathname: "/uploadingImgs/files/cntnts/**",
      }, 
    ], 
  },
};

export default nextConfig;

