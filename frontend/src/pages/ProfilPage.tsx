import { useEffect, useState } from 'react'
import { Navigate } from 'react-router-dom'
import { User, Mail, Calendar, Star } from 'lucide-react'
import { Tabs, TabsContent, TabsList, TabsTrigger } from '@/components/ui/tabs'
import { Skeleton } from '@/components/ui/skeleton'
import { Separator } from '@/components/ui/separator'
import { ReviewCard } from '@/components/ReviewCard'
import { useAuth } from '@/context/AuthContext'
import api from '@/lib/api'
import type { AvisDtoOut } from '@/types'

export function ProfilPage() {
  const { isAuthenticated, joueur } = useAuth()
  const [avis, setAvis] = useState<AvisDtoOut[]>([])
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    if (!joueur) return
    api.get<AvisDtoOut[]>(`/joueurs/${joueur.id}/avis`)
      .then(({ data }) => setAvis(data))
      .finally(() => setLoading(false))
  }, [joueur])

  if (!isAuthenticated) return <Navigate to="/login" replace />
  if (!joueur) return null

  const avgNote = avis.length > 0
    ? (avis.reduce((s, a) => s + a.note, 0) / avis.length).toFixed(1)
    : null

  const handleDeleteAvis = async (id: number) => {
    await api.delete(`/avis/${id}`)
    setAvis((prev) => prev.filter((a) => a.id !== id))
  }

  return (
    <main className="max-w-3xl mx-auto px-4 sm:px-6 py-8">
      {/* Profile header */}
      <div className="flex items-start gap-5 mb-8">
        {/* Avatar */}
        <div className="w-16 h-16 rounded-2xl bg-primary/20 border border-primary/30 flex items-center justify-center text-2xl font-bold text-primary uppercase shrink-0">
          {joueur.pseudo.charAt(0)}
        </div>

        <div className="flex flex-col gap-1 flex-1 min-w-0">
          <h1 className="text-2xl font-bold">{joueur.pseudo}</h1>

          <div className="flex flex-wrap gap-x-4 gap-y-1">
            <span className="flex items-center gap-1.5 text-sm text-muted-foreground">
              <Mail size={12} />
              {joueur.email}
            </span>
            {joueur.dateDeNaissance && (
              <span className="flex items-center gap-1.5 text-sm text-muted-foreground">
                <Calendar size={12} />
                {new Date(joueur.dateDeNaissance).toLocaleDateString('fr-FR', {
                  year: 'numeric', month: 'long', day: 'numeric',
                })}
              </span>
            )}
          </div>
        </div>
      </div>

      {/* Stats strip */}
      <div className="grid grid-cols-3 gap-3 mb-6">
        {[
          { label: 'Avis rédigés', value: avis.length, icon: User },
          { label: 'Note moyenne', value: avgNote ? `★ ${avgNote}` : '—', icon: Star },
          { label: 'Jeux commentés', value: new Set(avis.map((a) => a.jeuNom)).size, icon: User },
        ].map(({ label, value, icon: Icon }) => (
          <div key={label} className="flex flex-col gap-1 rounded-xl border border-border bg-card p-4">
            <span className="text-2xl font-bold">{value}</span>
            <span className="text-xs text-muted-foreground">{label}</span>
          </div>
        ))}
      </div>

      <Separator className="mb-6" />

      {/* Tabs */}
      <Tabs defaultValue="avis">
        <TabsList className="mb-4">
          <TabsTrigger value="avis">Mes avis ({avis.length})</TabsTrigger>
        </TabsList>

        <TabsContent value="avis">
          {loading ? (
            <div className="flex flex-col gap-3">
              {Array.from({ length: 3 }).map((_, i) => (
                <Skeleton key={i} className="h-32 rounded-xl" />
              ))}
            </div>
          ) : avis.length === 0 ? (
            <div className="py-12 text-center rounded-xl border border-dashed border-border">
              <p className="text-muted-foreground">Tu n'as pas encore rédigé d'avis.</p>
            </div>
          ) : (
            <div className="flex flex-col gap-3">
              {avis.map((a) => (
                <ReviewCard
                  key={a.id}
                  avis={a}
                  showGame
                  canDelete
                  onDelete={handleDeleteAvis}
                />
              ))}
            </div>
          )}
        </TabsContent>
      </Tabs>
    </main>
  )
}
