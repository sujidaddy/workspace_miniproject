export interface TokenType {
  exp: number;
  iat: number;
  sub: string;
  // 필요에 따라 추가 필드들
  userId?: string;
  username?: string;
}