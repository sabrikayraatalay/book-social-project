import React from 'react'
import ReactDOM from 'react-dom/client'

// 1. Router'ı (yönlendiriciyi) import etmemiz gerekiyor
import { BrowserRouter } from "react-router-dom";

import App from './App.tsx'
import './index.css'

ReactDOM.createRoot(document.getElementById('root')!).render(
  <React.StrictMode>
    {/* 2. Tüm <App /> komponentimizi <BrowserRouter> ile sarmalıyoruz.
      Bu, "Artık bu uygulamanın tamamı sayfa yönlendirmeyi kullanabilir" demektir.
    */}
    <BrowserRouter>
      <App />
    </BrowserRouter>
  </React.StrictMode>,
)