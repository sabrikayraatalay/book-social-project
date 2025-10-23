// Gerekli React Router Dom (sayfa yönlendirici) parçalarını import ediyoruz
import { Routes, Route, Link } from "react-router-dom";

// Oluşturduğumuz "sayfa" komponentlerini import ediyoruz
import { HomePage } from "./pages/HomePage";
import { LoginPage } from "./pages/LoginPage";
import { RegisterPage } from "./pages/RegisterPage";

// Ana App komponentimiz (fonksiyonumuz)
function App() {
  
  // Ekrana çizilecek HTML (JSX) kodumuz
  return (
    <div>
      {/* 1. Navigasyon (Her zaman görünen linkler) */}
      <nav>
        <ul>
          <li>
            <Link to="/">Ana Sayfa (Kitaplar)</Link>
          </li>
          <li>
            <Link to="/login">Giriş Yap</Link>
          </li>
          <li>
            <Link to="/register">Kayıt Ol</Link>
          </li>
        </ul>
      </nav>

      <hr />

      {/* 2. Rota Alanı (URL'e göre içeriği değişen alan) */}
      <Routes>
        {/* URL'de '/' (ana dizin) yazıyorsa, HomePage komponentini göster */}
        <Route path="/" element={<HomePage />} />
        
        {/* URL'de '/login' yazıyorsa, LoginPage komponentini göster */}
        <Route path="/login" element={<LoginPage />} />
        
        {/* URL'de '/register' yazıyorsa, RegisterPage komponentini göster */}
        <Route path="/register" element={<RegisterPage />} />
      </Routes>
    </div>
  );
}

// BU SATIR ÇOK ÖNEMLİ: App komponentimizi projenin kullanabilmesi için dışa aktarıyoruz.
export default App;