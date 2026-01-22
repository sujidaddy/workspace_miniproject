"use client";
import WaveHero from "@/components/WaveHeros";
import BoardSection from "@/app/BoardSection";

export default function Home() {
  return (
    <main className="h-screen snap-y snap-mandatory scroll-smooth bg-[#F8F9FA]">
      {/* SECTION 1: 활기찬 파도 애니메이션 */}
      <section className="w-full h-screen snap-start shrink-0">
        <WaveHero />
      </section>

      {/* SECTION 2: 대시보드 (ID를 주어 버튼 클릭 시 이동 가능하게 함) */}
      <section id="dashboard-section" className="w-full h-screen snap-start shrink-0">
        <BoardSection />
      </section>
    </main>
  );
}
