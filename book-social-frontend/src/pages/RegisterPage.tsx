// Bu, bir önceki derste yazdığımız kayıt olma formudur.
import { useState } from 'react';
import axios from 'axios';

const REGISTER_URL = "https://api.kayraatalay.me/rest/api/book-social/register";

export function RegisterPage() {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState<string | null>(null);
  const [success, setSuccess] = useState<boolean>(false);

  const handleSubmit = async (event: React.FormEvent) => {
    event.preventDefault();
    setError(null);
    setSuccess(false);

    try {
      const response = await axios.post(REGISTER_URL, {
        username: username,
        password: password,
      });

      console.log("Kayıt başarılı:", response.data);
      setSuccess(true); 

    } catch (err: any) {
      console.error("Kayıt hatası:", err);
      setError(err.response?.data?.exception?.message || "Bir hata oluştu.");
    }
  };

  return (
    <div>
      <h2>Kayıt Ol</h2>
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
        <button type="submit">Kayıt Ol</button>
      </form>

      {error && <div style={{ color: 'red' }}>Hata: {error}</div>}
      {success && <div style={{ color: 'green' }}>Kayıt başarıyla oluşturuldu! Şimdi giriş yapabilirsiniz.</div>}
    </div>
  );
}