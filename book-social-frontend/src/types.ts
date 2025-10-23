// Backend'deki DtoBook'un TypeScript'teki karşılığı (interface)
export interface DtoBook {
  id: number;
  createTime: string;
  title: string;
  authorName: string;
  rating: number | null; // Puan null olabilir
  reviewCount: number | null;
  googleBooksId: string | null;
}

// Backend'deki AuthResponse DTO'sunun karşılığı
export interface AuthResponse {
  accessToken: string;
  refreshToken: string;
}