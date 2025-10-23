import { useState, useEffect } from 'react';
import axios from 'axios';
import type { DtoBook } from '../types'; 

const API_URL = "https://api.kayraatalay.me/rest/api/book-social/book/list/pageable";

export function HomePage() {
  const [books, setBooks] = useState<DtoBook[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const fetchBooks = async () => {
      try {
        const response = await axios.get(API_URL, {
          params: { page: 0, size: 10 }
        });
        setBooks(response.data.payload.content);
      } catch (err) {
        setError("Kitaplar yüklenirken bir hata oluştu.");
        console.error(err); 
      } finally {
        setLoading(false);
      }
    };
    fetchBooks();
  }, []); 

  if (loading) return <div>Yükleniyor...</div>;
  if (error) return <div>Hata: {error}</div>;

  return (
    <div>
      <h2>Ana Sayfa - Kitap Listesi</h2>
      <ul>
        {books.map(book => (
          <li key={book.id}>
            {book.title} - (Yazar: {book.authorName})
          </li>
        ))}
      </ul>
    </div>
  );
}