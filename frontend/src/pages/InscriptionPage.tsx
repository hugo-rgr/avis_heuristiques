import { useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { Gamepad2 } from 'lucide-react'
import { Button } from '@/components/ui/button'
import { Input } from '@/components/ui/input'
import { Label } from '@/components/ui/label'
import { useAuth } from '@/context/AuthContext'

export function InscriptionPage() {
  const { register } = useAuth()
  const navigate = useNavigate()
  const [form, setForm] = useState({
    pseudo: '',
    email: '',
    motDePasse: '',
    dateDeNaissance: '',
  })
  const [error, setError] = useState<string | null>(null)
  const [loading, setLoading] = useState(false)

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setForm((f) => ({ ...f, [e.target.name]: e.target.value }))
  }

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    setLoading(true)
    setError(null)
    try {
      await register(form)
      navigate('/login')
    } catch {
      setError('Erreur lors de l\'inscription. Ce pseudo ou email est peut-être déjà utilisé.')
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="min-h-screen flex items-center justify-center px-4 py-12">
      <div className="fixed inset-0 pointer-events-none">
        <div className="absolute top-1/3 left-1/2 -translate-x-1/2 w-[600px] h-[400px] rounded-full bg-primary/5 blur-[100px]" />
      </div>

      <div className="w-full max-w-sm relative">
        <div className="flex flex-col items-center gap-3 mb-8">
          <div className="w-12 h-12 rounded-xl bg-primary/15 border border-primary/30 flex items-center justify-center">
            <Gamepad2 size={24} className="text-primary" />
          </div>
          <div className="text-center">
            <h1 className="text-xl font-bold">Créer un compte</h1>
            <p className="text-sm text-muted-foreground mt-0.5">Rejoins la communauté</p>
          </div>
        </div>

        <form onSubmit={handleSubmit} className="flex flex-col gap-4">
          <div className="flex flex-col gap-1.5">
            <Label htmlFor="pseudo">Pseudo</Label>
            <Input
              id="pseudo"
              name="pseudo"
              placeholder="ton_pseudo"
              value={form.pseudo}
              onChange={handleChange}
              required
            />
          </div>

          <div className="flex flex-col gap-1.5">
            <Label htmlFor="email">Email</Label>
            <Input
              id="email"
              name="email"
              type="email"
              placeholder="toi@exemple.fr"
              value={form.email}
              onChange={handleChange}
              required
            />
          </div>

          <div className="flex flex-col gap-1.5">
            <Label htmlFor="motDePasse">Mot de passe</Label>
            <Input
              id="motDePasse"
              name="motDePasse"
              type="password"
              placeholder="••••••••"
              value={form.motDePasse}
              onChange={handleChange}
              required
              minLength={6}
            />
          </div>

          <div className="flex flex-col gap-1.5">
            <Label htmlFor="dateDeNaissance">Date de naissance</Label>
            <Input
              id="dateDeNaissance"
              name="dateDeNaissance"
              type="date"
              value={form.dateDeNaissance}
              onChange={handleChange}
              required
            />
          </div>

          {error && (
            <p className="text-sm text-destructive bg-destructive/10 px-3 py-2 rounded-lg">
              {error}
            </p>
          )}

          <Button type="submit" className="w-full mt-1" disabled={loading}>
            {loading ? 'Inscription...' : 'Créer mon compte'}
          </Button>
        </form>

        <p className="text-center text-sm text-muted-foreground mt-6">
          Déjà un compte ?{' '}
          <Link to="/login" className="text-primary hover:underline font-medium">
            Se connecter
          </Link>
        </p>
      </div>
    </div>
  )
}
