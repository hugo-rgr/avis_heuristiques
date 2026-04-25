import { BrowserRouter, Routes, Route } from 'react-router-dom'
import { AuthProvider } from '@/context/AuthContext'
import { Navbar } from '@/components/Navbar'
import { LoginPage } from '@/pages/LoginPage'
import { InscriptionPage } from '@/pages/InscriptionPage'
import { JeuxPage } from '@/pages/JeuxPage'
import { JeuDetailPage } from '@/pages/JeuDetailPage'
import { ProfilPage } from '@/pages/ProfilPage'
import { DashboardPage } from '@/pages/DashboardPage'

export default function App() {
  return (
    <AuthProvider>
      <BrowserRouter>
        <div className="min-h-screen flex flex-col">
          <Routes>
            {/* Auth pages — sans navbar */}
            <Route path="/login" element={<LoginPage />} />
            <Route path="/inscription" element={<InscriptionPage />} />

            {/* App pages — avec navbar */}
            <Route
              path="/*"
              element={
                <>
                  <Navbar />
                  <div className="flex-1">
                    <Routes>
                      <Route path="/" element={<JeuxPage />} />
                      <Route path="/jeux/:id" element={<JeuDetailPage />} />
                      <Route path="/profil" element={<ProfilPage />} />
                      <Route path="/dashboard" element={<DashboardPage />} />
                    </Routes>
                  </div>
                </>
              }
            />
          </Routes>
        </div>
      </BrowserRouter>
    </AuthProvider>
  )
}
