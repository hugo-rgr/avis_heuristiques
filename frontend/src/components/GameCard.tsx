import { Link } from 'react-router-dom'
import { Gamepad2 } from 'lucide-react'
import { Badge } from '@/components/ui/badge'
import { StarRating } from '@/components/StarRating'
import { cn } from '@/lib/utils'
import { useClassificationColor } from '@/hooks/useClassifications'
import type { JeuDtoOut } from '@/types'

interface GameCardProps {
  jeu: JeuDtoOut
  avgNote?: number
  avisCount?: number
  className?: string
}

export function GameCard({ jeu, avgNote, avisCount, className }: GameCardProps) {
  const getColor = useClassificationColor()
  const pegiColor = getColor(jeu.classificationNom)

  return (
    <Link
      to={`/jeux/${jeu.id}`}
      className={cn(
        'group flex flex-col rounded-xl border border-border bg-card overflow-hidden',
        'transition-all duration-200 hover:border-primary/50 hover:shadow-[0_0_24px_oklch(0.66_0.22_255/0.15)]',
        className
      )}
    >
      {/* Cover image area */}
      <div className="relative aspect-[3/4] bg-muted overflow-hidden">
        {jeu.image ? (
          <img
            src={jeu.image}
            alt={jeu.nom}
            className="w-full h-full object-cover transition-transform duration-300 group-hover:scale-105"
          />
        ) : (
          <div className="w-full h-full flex items-center justify-center">
            <Gamepad2 size={48} className="text-muted-foreground/30" />
          </div>
        )}

        {/* Gradient overlay */}
        <div className="absolute inset-0 bg-gradient-to-t from-black/70 via-transparent to-transparent" />

        {/* Classification badge */}
        <span
          className="absolute top-2 right-2 text-[10px] font-bold px-1.5 py-0.5 rounded text-white uppercase tracking-wide"
          style={{ backgroundColor: pegiColor }}
        >
          {jeu.classificationNom}
        </span>

        {/* Price */}
        <span className="absolute bottom-2 left-2 text-sm font-semibold text-white">
          {jeu.prix === 0 ? 'Gratuit' : `${jeu.prix.toFixed(2)} €`}
        </span>

        {/* Rating overlay */}
        {avgNote !== undefined && (
          <span className="absolute bottom-2 right-2 text-xs font-semibold text-amber-400">
            ★ {avgNote.toFixed(1)}
          </span>
        )}
      </div>

      {/* Card body */}
      <div className="flex flex-col gap-2 p-3 flex-1">
        <p className="font-semibold text-sm leading-snug line-clamp-2 text-foreground group-hover:text-primary transition-colors">
          {jeu.nom}
        </p>

        <div className="flex items-center gap-1.5 flex-wrap mt-auto">
          <Badge variant="secondary" className="text-[10px] px-1.5 py-0">
            {jeu.genreNom}
          </Badge>
          {jeu.plateformes.slice(0, 2).map((p) => (
            <Badge key={p} variant="outline" className="text-[10px] px-1.5 py-0 border-border/60">
              {p}
            </Badge>
          ))}
          {jeu.plateformes.length > 2 && (
            <span className="text-[10px] text-muted-foreground">+{jeu.plateformes.length - 2}</span>
          )}
        </div>

        {avgNote !== undefined ? (
          <div className="flex items-center gap-1.5">
            <StarRating value={avgNote} size={12} />
            {avisCount !== undefined && (
              <span className="text-[11px] text-muted-foreground">({avisCount})</span>
            )}
          </div>
        ) : (
          <span className="text-[11px] text-muted-foreground">Aucun avis</span>
        )}
      </div>
    </Link>
  )
}
