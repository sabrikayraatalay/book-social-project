// Bu, bir önceki derste yazdığımız giriş yapma formudur.
import { useState } from 'react';
import axios from 'axios';
import type { AuthResponse } from '../types'; 

const LOGIN_URL = "https://api.kayraatalay.me/rest/api/book-social/authenticate";

export function LoginPage() {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState<string | null>(null);

  const handleSubmit = async (event: React.FormEvent) => {
    event.preventDefault();
    setError(null);
    try {
      const response = await axios.post<AuthResponse>(LOGIN_URL, {
        username: username,
        password: password,
      });

      const token = response.data.accessToken;
      console.log("Giriş başarılı! Token:", token);
      alert("Giriş başarılı! Token'ı konsolda görebilirsin.");

    } catch (err: any) {
      console.error("Giriş hatası:", err);
      setError(err.response?.data?.exception?.message || "Kullanıcı adı veya şifre hatalı.");
    }
  };

  return (
    <div>
      <h2>Giriş Yap</h2>
      <form onSubmit={handleSubmit}>
        <div>
          <label>Kullanıcı Adı:</label>
          <input 
            type="text" 
            value={username}
            onChange={(e) => setUsername(e.target.value)}
            required 
          />
        </div>
        <div>
          <label>Şifre:</label>
          <input 
            type="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required 
          />
        </div>
        <button type="submit">Giriş Yap</button>
      </form>
      {error && <div style={{ color: 'red' }}>Hata: {error}</div>}
    </div>
  );
}