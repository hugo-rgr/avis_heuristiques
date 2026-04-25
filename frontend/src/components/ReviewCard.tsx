import { Trash2, ShieldCheck } from 'lucide-react'
import { StarRating } from '@/components/StarRating'
import { Button } from '@/components/ui/button'
import { cn } from '@/lib/utils'
import type { AvisDtoOut } from '@/types'

interface ReviewCardProps {
  avis: AvisDtoOut
  canDelete?: boolean
  onDelete?: (id: number) => void
  showGame?: boolean
  className?: string
}

function noteColor(note: number) {
  if (note >= 4) return 'text-emerald-400 bg-emerald-400/10'
  if (note >= 3) return 'text-amber-400 bg-amber-400/10'
  if (note >= 2) return 'text-orange-400 bg-orange-400/10'
  return 'text-red-400 bg-red-400/10'
}

export function ReviewCard({ avis, canDelete, onDelete, showGame = false, className }: ReviewCardProps) {
  const date = new Date(avis.dateDEnvoi).toLocaleDateString('fr-FR', {
    day: 'numeric',
    month: 'short',
    year: 'numeric',
  })

  return (
    <div className={cn('flex flex-col gap-3 rounded-xl border border-border bg-card p-4', className)}>
      {/* Header */}
      <div className="flex items-start justify-between gap-2">
        <div className="flex flex-col gap-0.5">
          <div className="flex items-center gap-2">
            {/* Avatar placeholder */}
            <div className="w-7 h-7 rounded-full bg-primary/20 flex items-center justify-center text-xs font-bold text-primary uppercase">
              {avis.joueurPseudo.charAt(0)}
            </div>
            <span className="text-sm font-semibold">{avis.joueurPseudo}</span>
            {avis.moderateurPseudo && (
              <span className="flex items-center gap-0.5 text-[10px] text-emerald-400 font-medium">
                <ShieldCheck size={10} />
                validé
              </span>
            )}
          </div>
          {showGame && (
            <span className="text-xs text-muted-foreground ml-9">{avis.jeuNom}</span>
          )}
        </div>

        <div className="flex items-center gap-2 shrink-0">
          <span
            className={cn(
              'text-sm font-bold px-2 py-0.5 rounded-md',
              noteColor(avis.note)
            )}
          >
            {avis.note.toFixed(1)}
          </span>
          {canDelete && onDelete && (
            <Button
              variant="ghost"
              size="icon"
              className="h-7 w-7 text-muted-foreground hover:text-destructive"
              onClick={() => onDelete(avis.id)}
            >
              <Trash2 size={14} />
            </Button>
          )}
        </div>
      </div>

      {/* Stars */}
      <StarRating value={avis.note} size={13} />

      {/* Content */}
      <p className="text-sm text-muted-foreground leading-relaxed">{avis.description}</p>

      {/* Footer */}
      <span className="text-[11px] text-muted-foreground/60">{date}</span>
    </div>
  )
}
