'use client';
import Image from 'next/image';
// import img_wave_01 from '../';
import { motion } from 'framer-motion';

export default function WaveHero() {
  const scrollToDashboard = () => {
    const dashboard = document.getElementById('dashboard-section');
    dashboard?.scrollIntoView({ behavior: 'smooth' });
  };

  return (
    <div className="relative w-full h-screen overflow-hidden bg-slate-900">
      <div className="relative w-full h-screen overflow-hidden">
        <Image
          src="/images/img_wave_01.png"  // public/images/img_wave_01.png 경로
          alt="바다 낚시"
          fill
          className="object-cover"
          priority
        />
        <div
          className="absolute inset-0 bg-cover bg-center transition-transform duration-[20s] scale-110 animate-pulse-slow"
          style={{
            backgroundImage: "url(/images/img_wave_01.png)", // public 폴더에 위치 확인
          }}
        >
          <div className="absolute inset-0 bg-gradient-to-b from-black/20 via-transparent to-black/40"></div>
        </div>

        {/* 컨텐츠 레이어 */}
        <div className="relative z-20 flex flex-col items-center justify-center h-full text-white px-6 text-center">
          <motion.p
            initial={{ opacity: 0, y: -20 }}
            animate={{ opacity: 1, y: 0 }}
            className="text-3xl tracking-[0.4em] mb-4 font-light drop-shadow-lg"
          >
            K-Digital PNU  25-3회차
          </motion.p>

          <motion.h1
            initial={{ opacity: 0, scale: 0.9 }}
            animate={{ opacity: 1, scale: 1 }}
            transition={{ delay: 0.2 }}
            className="text-5xl text-blue-800 md:text-4xl font-black mb-6 leading-tight tracking-tighter"
          >
            푸른 바다를 낚는 사람들,
            여러분이 진정한 Winner입니다.
            <br />
            <span className="text-white-400 drop-shadow-[0_0_15px_rgba(34,211,238,0.8)]">
              <p className="text-white text-2xl ...">바다낚시의 즐거움을 선사합니다.</p>
            </span>
          </motion.h1>

          <motion.p
            initial={{ opacity: 0 }}
            animate={{ opacity: 1 }}
            transition={{ delay: 0.4 }}
            className="text-lg md:text-2xl font-light max-w-3xl opacity-90 drop-shadow-md"
          >
            실시간 기상 정보와 낚시 포인트를 한눈에 확인하세요.
          </motion.p>

          {/* 스크롤 유도 버튼 */}
          <button
            onClick={scrollToDashboard}
            className="mt-16 animate-bounce group flex flex-col items-center"
          >
            <span className="text-xs tracking-widest mb-2 opacity-50 group-hover:opacity-100 transition-opacity">SCROLL DOWN</span>
            <div className="p-3 bg-white/10 rounded-full backdrop-blur-md border border-white/20 group-hover:bg-white/20 transition-all">
              <svg className="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M19 14l-7 7m0 0l-7-7m7 7V3" />
              </svg>
            </div>
          </button>
        </div>

        {/* 넘실대는 파도 SVG 레이어 (활기찬 Cyan 톤) */}
        <div className="absolute bottom-0 left-0 w-full overflow-hidden leading-none z-10">
          <svg className="relative block w-full h-[150px] md:h-[200px]" viewBox="0 24 150 28" preserveAspectRatio="none">
            <defs>
              <path id="gentle-wave" d="M-160 44c30 0 58-18 88-18s 58 18 88 18 58-18 88-18 58 18 88 18 v44h-352z" />
            </defs>
            <g className="parallax">
              {/* 파도 1: 가장 뒤쪽 (연한 청록색) */}
              <use xlinkHref="#gentle-wave" x="48" y="0" fill="rgba(34, 211, 238, 0.3)" className="animate-wave-1" />
              {/* 파도 2: 중간 (더 활기찬 톤) */}
              <use xlinkHref="#gentle-wave" x="48" y="3" fill="rgba(6, 182, 212, 0.5)" className="animate-wave-2" />
              {/* 파도 3: 대시보드 배경색과 이어지는 부분 (Solid White/Slate) */}
              <use xlinkHref="#gentle-wave" x="48" y="7" fill="#F8F9FA" />
            </g>
          </svg>
        </div>

        <style jsx>{`
        .animate-wave-1 { animation: move-forever 25s cubic-bezier(.55,.5,.45,.5) infinite; }
        .animate-wave-2 { animation: move-forever 10s cubic-bezier(.55,.5,.45,.5) infinite; }
        @keyframes move-forever {
          0% { transform: translate3d(-90px,0,0); }
          100% { transform: translate3d(85px,0,0); }
        }
        @keyframes pulse-slow {
          0%, 100% { transform: scale(1.1); }
          50% { transform: scale(1.05); }
        }
      `}</style>
      </div>
  </div>  );
}
