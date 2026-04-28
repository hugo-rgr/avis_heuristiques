import { useState } from 'react'
import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogTrigger } from '@/components/ui/dialog'
import { Button } from '@/components/ui/button'
import { Textarea } from '@/components/ui/textarea'
import { Label } from '@/components/ui/label'
import { StarRating } from '@/components/StarRating'
import { PenLine } from 'lucide-react'
import api from '@/lib/api'
import { useAuth } from '@/context/AuthContext'
import type { AvisDtoOut } from '@/types'

interface ReviewFormProps {
  jeuId: number
  onSuccess: (avis: AvisDtoOut) => void
}

export function ReviewForm({ jeuId, onSuccess }: ReviewFormProps) {
  const { joueur } = useAuth()
  const [open, setOpen] = useState(false)
  const [description, setDescription] = useState('')
  const [note, setNote] = useState(0)
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState<string | null>(null)

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    if (!joueur || note === 0) return
    setLoading(true)
    setError(null)
    try {
      const { data } = await api.post<AvisDtoOut>(`/joueurs/${joueur.id}/avis`, {
        description,
        jeuId,
        joueurId: joueur.id,
        note,
        moderateurId: null,
        dateDEnvoi: new Date().toISOString().split('T')[0],
      })
      onSuccess(data)
      setDescription('')
      setNote(0)
      setOpen(false)
    } catch {
      setError('Erreur lors de la soumission de l\'avis.')
    } finally {
      setLoading(false)
    }
  }

  return (
    <Dialog open={open} onOpenChange={setOpen}>
      <DialogTrigger render={<Button className="gap-2" />}>
        <PenLine size={15} />
        Rédiger un avis
      </DialogTrigger>
      <DialogContent className="sm:max-w-md">
        <DialogHeader>
          <DialogTitle>Rédiger un avis</DialogTitle>
        </DialogHeader>
        <form onSubmit={handleSubmit} className="flex flex-col gap-5 mt-2">
          <div className="flex flex-col gap-2">
            <Label>Note</Label>
            <div className="flex items-center gap-3">
              <StarRating value={note} interactive onChange={setNote} size={28} />
              {note > 0 && (
                <span className="text-sm text-muted-foreground">{note}/5</span>
              )}
            </div>
          </div>

          <div className="flex flex-col gap-2">
            <Label htmlFor="description">Votre avis</Label>
            <Textarea
              id="description"
              placeholder="Décrivez votre expérience avec ce jeu..."
              rows={4}
              value={description}
              onChange={(e) => setDescription(e.target.value)}
              required
            />
          </div>

          {error && <p className="text-sm text-destructive">{error}</p>}

          <Button type="submit" disabled={loading || note === 0} className="w-full">
            {loading ? 'Envoi...' : 'Publier l\'avis'}
          </Button>
        </form>
      </DialogContent>
    </Dialog>
  )
}
