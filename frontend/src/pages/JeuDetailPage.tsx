import { useEffect, useState } from 'react'
import { useParams, Link } from 'react-router-dom'
import { ArrowLeft, Calendar, Tag, Monitor, Gamepad2 } from 'lucide-react'
import { Badge } from '@/components/ui/badge'
import { Skeleton } from '@/components/ui/skeleton'
import { Separator } from '@/components/ui/separator'
import { StarRating } from '@/components/StarRating'
import { ReviewCard } from '@/components/ReviewCard'
import { ReviewForm } from '@/components/ReviewForm'
import { useAuth } from '@/context/AuthContext'
import { useClassificationColor } from '@/hooks/useClassifications'
import api from '@/lib/api'
import type { JeuDtoOut, AvisDtoOut } from '@/types'

export function JeuDetailPage() {
  const { id } = useParams<{ id: string }>()
  const { isAuthenticated, isModo, joueur } = useAuth()
  const getColor = useClassificationColor()

  const [jeu, setJeu] = useState<JeuDtoOut | null>(null)
  const [avis, setAvis] = useState<AvisDtoOut[]>([])
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    if (!id) return
    Promise.all([
      api.get<JeuDtoOut>(`/jeux/${id}`),
      api.get<AvisDtoOut[]>(`/avis/jeu/${id}`),
    ]).then(([{ data: jeuData }, { data: avisData }]) => {
      setJeu(jeuData)
      setAvis(avisData)
    }).finally(() => setLoading(false))
  }, [id])

  const handleNewAvis = (newAvis: AvisDtoOut) => {
    setAvis((prev) => [newAvis, ...prev])
  }

  const handleDeleteAvis = async (avisId: number) => {
    await api.delete(`/avis/${avisId}`)
    setAvis((prev) => prev.filter((a) => a.id !== avisId))
  }

  const avgNote = avis.length > 0
    ? avis.reduce((s, a) => s + a.note, 0) / avis.length
    : null

  if (loading) {
    return (
      <main className="max-w-5xl mx-auto px-4 sm:px-6 py-8">
        <Skeleton className="h-5 w-24 mb-6" />
        <div className="flex gap-6">
          <Skeleton className="w-48 aspect-[3/4] rounded-xl shrink-0" />
          <div className="flex flex-col gap-3 flex-1">
            <Skeleton className="h-8 w-2/3" />
            <Skeleton className="h-4 w-1/3" />
            <Skeleton className="h-20 w-full" />
          </div>
        </div>
      </main>
    )
  }

  if (!jeu) {
    return (
      <main className="max-w-5xl mx-auto px-4 sm:px-6 py-8">
        <p className="text-muted-foreground">Jeu introuvable.</p>
      </main>
    )
  }

  return (
    <main className="max-w-5xl mx-auto px-4 sm:px-6 py-8">
      {/* Back */}
      <Link
        to="/"
        className="inline-flex items-center gap-1.5 text-sm text-muted-foreground hover:text-foreground mb-6 transition-colors"
      >
        <ArrowLeft size={14} />
        Retour au catalogue
      </Link>

      {/* Game header */}
      <div className="flex flex-col sm:flex-row gap-6 mb-8">
        {/* Cover */}
        <div className="w-full sm:w-44 aspect-[3/4] sm:aspect-auto sm:h-60 rounded-xl border border-border bg-muted overflow-hidden shrink-0 flex items-center justify-center">
          {jeu.image ? (
            <img src={jeu.image} alt={jeu.nom} className="w-full h-full object-cover" />
          ) : (
            <Gamepad2 size={40} className="text-muted-foreground/30" />
          )}
        </div>

        {/* Info */}
        <div className="flex flex-col gap-3 flex-1 min-w-0">
          <div>
            <h1 className="text-2xl sm:text-3xl font-bold tracking-tight">{jeu.nom}</h1>
            <p className="text-muted-foreground text-sm mt-0.5">{jeu.editeurNom}</p>
          </div>

          {/* Meta badges */}
          <div className="flex flex-wrap gap-2">
            <Badge variant="secondary" className="gap-1">
              <Tag size={10} />
              {jeu.genreNom}
            </Badge>
            <Badge
              style={{ backgroundColor: getColor(jeu.classificationNom), color: '#fff', borderColor: 'transparent' }}
            >
              {jeu.classificationNom}
            </Badge>
            <Badge variant="outline" className="gap-1">
              <Calendar size={10} />
              {new Date(jeu.dateDeSortie).toLocaleDateString('fr-FR', { year: 'numeric', month: 'long', day: 'numeric' })}
            </Badge>
          </div>

          {/* Platforms */}
          <div className="flex items-center gap-1.5 flex-wrap">
            <Monitor size={12} className="text-muted-foreground" />
            {jeu.plateformes.map((p) => (
              <span key={p} className="text-xs text-muted-foreground bg-muted px-2 py-0.5 rounded-md">
                {p}
              </span>
            ))}
          </div>

          {/* Price + rating */}
          <div className="flex items-center gap-4 mt-1">
            <span className="text-xl font-bold text-primary">
              {jeu.prix === 0 ? 'Gratuit' : `${jeu.prix.toFixed(2)} €`}
            </span>
            {avgNote !== null && (
              <div className="flex items-center gap-2">
                <StarRating value={avgNote} size={16} />
                <span className="text-sm text-muted-foreground">
                  {avgNote.toFixed(1)} / 5 ({avis.length} avis)
                </span>
              </div>
            )}
          </div>

          {/* Description */}
          <p className="text-sm text-muted-foreground leading-relaxed line-clamp-3">
            {jeu.description}
          </p>
        </div>
      </div>

      <Separator className="mb-6" />

      {/* Reviews section */}
      <div className="flex items-center justify-between mb-4">
        <h2 className="text-lg font-semibold">
          Avis{avis.length > 0 && <span className="text-muted-foreground font-normal ml-2 text-base">({avis.length})</span>}
        </h2>
        {isAuthenticated && !isModo && (
          <ReviewForm jeuId={jeu.id} onSuccess={handleNewAvis} />
        )}
      </div>

      {!isAuthenticated && (
        <p className="text-sm text-muted-foreground mb-4">
          <Link to="/login" className="text-primary hover:underline">Connecte-toi</Link> pour rédiger un avis.
        </p>
      )}

      {avis.length === 0 ? (
        <div className="py-12 text-center rounded-xl border border-dashed border-border">
          <p className="text-muted-foreground">Aucun avis pour l'instant. Sois le premier !</p>
        </div>
      ) : (
        <div className="flex flex-col gap-3">
          {avis.map((a) => (
            <ReviewCard
              key={a.id}
              avis={a}
              canDelete={joueur?.pseudo === a.joueurPseudo}
              onDelete={handleDeleteAvis}
            />
          ))}
        </div>
      )}
    </main>
  )
}
