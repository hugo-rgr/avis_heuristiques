import { useEffect, useState, useMemo } from 'react'
import { Search, SlidersHorizontal } from 'lucide-react'
import { Input } from '@/components/ui/input'
import { Badge } from '@/components/ui/badge'
import { Skeleton } from '@/components/ui/skeleton'
import { GameCard } from '@/components/GameCard'
import api from '@/lib/api'
import type { JeuDtoOut, AvisDtoOut } from '@/types'

export function JeuxPage() {
  const [jeux, setJeux] = useState<JeuDtoOut[]>([])
  const [avisMap, setAvisMap] = useState<Record<number, AvisDtoOut[]>>({})
  const [loading, setLoading] = useState(true)
  const [search, setSearch] = useState('')
  const [activeGenre, setActiveGenre] = useState<string | null>(null)

  useEffect(() => {
    api.get<JeuDtoOut[]>('/jeux').then(({ data }) => {
      setJeux(data)
      setLoading(false)
      // Load reviews for each game in background
      data.forEach((jeu) => {
        api.get<AvisDtoOut[]>(`/avis/jeu/${jeu.id}`).then(({ data: avis }) => {
          setAvisMap((prev) => ({ ...prev, [jeu.id]: avis }))
        }).catch(() => {})
      })
    }).catch(() => setLoading(false))
  }, [])

  const genres = useMemo(() => {
    const set = new Set(jeux.map((j) => j.genreNom))
    return Array.from(set).sort()
  }, [jeux])

  const filtered = useMemo(() => {
    return jeux.filter((j) => {
      const matchSearch = j.nom.toLowerCase().includes(search.toLowerCase())
      const matchGenre = !activeGenre || j.genreNom === activeGenre
      return matchSearch && matchGenre
    })
  }, [jeux, search, activeGenre])

  const avgNote = (jeuId: number) => {
    const list = avisMap[jeuId]
    if (!list || list.length === 0) return undefined
    return list.reduce((s, a) => s + a.note, 0) / list.length
  }

  return (
    <main className="max-w-7xl mx-auto px-4 sm:px-6 py-8">
      {/* Hero */}
      <div className="mb-8">
        <h1 className="text-3xl font-bold tracking-tight mb-1">
          Catalogue des jeux
        </h1>
        <p className="text-muted-foreground">
          {jeux.length > 0 ? `${jeux.length} jeux référencés` : 'Découvrez et notez vos jeux favoris'}
        </p>
      </div>

      {/* Search + filters */}
      <div className="flex flex-col gap-4 mb-6">
        <div className="relative max-w-sm">
          <Search size={15} className="absolute left-3 top-1/2 -translate-y-1/2 text-muted-foreground" />
          <Input
            placeholder="Rechercher un jeu..."
            className="pl-9"
            value={search}
            onChange={(e) => setSearch(e.target.value)}
          />
        </div>

        {genres.length > 0 && (
          <div className="flex items-center gap-2 flex-wrap">
            <SlidersHorizontal size={14} className="text-muted-foreground shrink-0" />
            <Badge
              variant={activeGenre === null ? 'default' : 'secondary'}
              className="cursor-pointer"
              onClick={() => setActiveGenre(null)}
            >
              Tous
            </Badge>
            {genres.map((g) => (
              <Badge
                key={g}
                variant={activeGenre === g ? 'default' : 'secondary'}
                className="cursor-pointer"
                onClick={() => setActiveGenre(activeGenre === g ? null : g)}
              >
                {g}
              </Badge>
            ))}
          </div>
        )}
      </div>

      {/* Grid */}
      {loading ? (
        <div className="grid grid-cols-2 sm:grid-cols-3 lg:grid-cols-4 xl:grid-cols-5 gap-4">
          {Array.from({ length: 10 }).map((_, i) => (
            <div key={i} className="flex flex-col rounded-xl border border-border overflow-hidden">
              <Skeleton className="aspect-[3/4]" />
              <div className="p-3 flex flex-col gap-2">
                <Skeleton className="h-4 w-3/4" />
                <Skeleton className="h-3 w-1/2" />
              </div>
            </div>
          ))}
        </div>
      ) : filtered.length === 0 ? (
        <div className="flex flex-col items-center justify-center py-24 text-center">
          <p className="text-muted-foreground text-lg">
            {jeux.length === 0 ? 'Aucun jeu référencé pour le moment.' : 'Aucun jeu ne correspond à ta recherche.'}
          </p>
        </div>
      ) : (
        <div className="grid grid-cols-2 sm:grid-cols-3 lg:grid-cols-4 xl:grid-cols-5 gap-4">
          {filtered.map((jeu) => (
            <GameCard
              key={jeu.id}
              jeu={jeu}
              avgNote={avgNote(jeu.id)}
              avisCount={avisMap[jeu.id]?.length}
            />
          ))}
        </div>
      )}
    </main>
  )
}
