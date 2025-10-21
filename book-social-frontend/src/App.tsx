// 'React' import'unu kaldırdık, sadece 'useState' ve 'useEffect' kaldı.
import { useState, useEffect } from 'react';
import axios from 'axios';

// 'import' yerine 'import type' kullanarak bunun bir tip olduğunu belirttik.
import type { DtoBook } from './types'; 

// BU URL'İ KENDİ CONTROLLER'INA GÖRE GÜNCELLEDİĞİNDEN EMİN OL!
const API_URL = "https://api.kayraatalay.me/rest/api/book-social/book/list/pageable";

function App() {
  const [books, setBooks] = useState<DtoBook[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const fetchBooks = async () => {
      try {
        const response = await axios.get(API_URL);
        
        // Backend'den gelen veri RootEntity<RestPageableEntity<DtoBook>>
        // O yüzden 'payload.content' içindeki listeyi alıyoruz.
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

  
  if (loading) {
    return <div>Yükleniyor...</div>;
  }

  if (error) {
    return <div>Hata: {error}</div>;
  }

  return (
    <div>
      <h1>Book Social Kitap Listesi</h1>
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

export default App;