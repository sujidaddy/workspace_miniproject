// src/components/layout/SearchBar.tsx
"use client";

import { Search } from "lucide-react";

export default function SearchBar() {
  return (
    <div className="relative w-full max-w-md">
      <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
        <Search className="h-5 w-5 text-gray-400" />
      </div>
      <input
        type="text"
        className="block w-full pl-10 pr-3 py-2 border-none bg-gray-100 rounded-xl leading-5 placeholder-gray-500 focus:outline-none focus:ring-2 focus:ring-orange-500 focus:bg-white sm:text-sm transition-all"
        placeholder="Search..."
      />
    </div>
  );
}