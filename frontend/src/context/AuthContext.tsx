import { createContext, useContext, useState, useCallback, type ReactNode } from 'react'
import api from '@/lib/api'
import type { LoginDtoIn, JoueurDtoIn, JoueurDtoOut } from '@/types'

interface AuthState {
  token: string | null
  joueur: JoueurDtoOut | null
  role: 'JOUEUR' | 'MODERATEUR' | null
}

interface AuthContextValue extends AuthState {
  login: (dto: LoginDtoIn) => Promise<void>
  register: (dto: JoueurDtoIn) => Promise<void>
  logout: () => void
  isAuthenticated: boolean
  isModo: boolean
}

const AuthContext = createContext<AuthContextValue | null>(null)

const readStored = (): AuthState => {
  try {
    const token = localStorage.getItem('token')
    const joueurRaw = localStorage.getItem('joueur')
    const role = localStorage.getItem('role') as AuthState['role']
    return {
      token,
      joueur: joueurRaw ? (JSON.parse(joueurRaw) as JoueurDtoOut) : null,
      role,
    }
  } catch {
    return { token: null, joueur: null, role: null }
  }
}

export function AuthProvider({ children }: { children: ReactNode }) {
  const [state, setState] = useState<AuthState>(readStored)

  const login = useCallback(async (dto: LoginDtoIn) => {
    const { data } = await api.post<{ token: string; role: string }>('/auth/login', dto)
    localStorage.setItem('token', data.token)

    const payload = JSON.parse(atob(data.token.split('.')[1]))
    const joueurId: number = Number(payload.id)
    const role = (payload.role ?? data.role) as AuthState['role']
    localStorage.setItem('role', role ?? '')

    // Pour un modérateur, on n'a pas d'endpoint /joueurs/{id} — on construit un profil minimal
    if (role === 'MODERATEUR') {
      const fakeJoueur: JoueurDtoOut = {
        id: joueurId,
        pseudo: payload.sub as string,
        email: payload.sub as string,
        dateDeNaissance: '',
        avatarNom: null,
      }
      localStorage.setItem('joueur', JSON.stringify(fakeJoueur))
      setState({ token: data.token, joueur: fakeJoueur, role })
      return
    }

    const { data: joueur } = await api.get<JoueurDtoOut>(`/joueurs/${joueurId}`, {
      headers: { Authorization: `Bearer ${data.token}` },
    })
    localStorage.setItem('joueur', JSON.stringify(joueur))
    setState({ token: data.token, joueur, role })
  }, [])

  const register = useCallback(async (dto: JoueurDtoIn) => {
    await api.post('/joueurs/inscription', dto)
  }, [])

  const logout = useCallback(() => {
    localStorage.removeItem('token')
    localStorage.removeItem('joueur')
    localStorage.removeItem('role')
    setState({ token: null, joueur: null, role: null })
  }, [])

  return (
    <AuthContext.Provider value={{
      ...state,
      login,
      register,
      logout,
      isAuthenticated: !!state.token,
      isModo: state.role === 'MODERATEUR',
    }}>
      {children}
    </AuthContext.Provider>
  )
}

export function useAuth() {
  const ctx = useContext(AuthContext)
  if (!ctx) throw new Error('useAuth must be used inside AuthProvider')
  return ctx
}
