import { useEffect, useState } from 'react'
import { Navigate } from 'react-router-dom'
import { Plus, Trash2, ShieldAlert, Check, X } from 'lucide-react'
import { Tabs, TabsContent, TabsList, TabsTrigger } from '@/components/ui/tabs'
import { Button } from '@/components/ui/button'
import { Input } from '@/components/ui/input'
import { Label } from '@/components/ui/label'
import { Textarea } from '@/components/ui/textarea'
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from '@/components/ui/select'
import { Skeleton } from '@/components/ui/skeleton'
import { StarRating } from '@/components/StarRating'
import { useAuth } from '@/context/AuthContext'
import api from '@/lib/api'
import type { AvisDtoOut, GenreDtoOut, EditeurDtoOut, ClassificationDtoOut, PlatefomeDtoOut } from '@/types'

// ── Composant création rapide inline ──────────────────────────────────────────
interface QuickCreateProps {
  onSave: (nom: string) => Promise<void>
}

function QuickCreate({ onSave }: QuickCreateProps) {
  const [open, setOpen] = useState(false)
  const [nom, setNom] = useState('')
  const [saving, setSaving] = useState(false)

  const handleSave = async () => {
    if (!nom.trim()) return
    setSaving(true)
    try {
      await onSave(nom.trim())
      setNom('')
      setOpen(false)
    } finally {
      setSaving(false)
    }
  }

  if (!open) {
    return (
      <button
        type="button"
        onClick={() => setOpen(true)}
        className="flex items-center gap-1 text-xs text-primary hover:underline mt-1"
      >
        <Plus size={11} /> Ajouter
      </button>
    )
  }

  return (
    <div className="flex items-center gap-1.5 mt-1">
      <Input
        autoFocus
        value={nom}
        onChange={(e) => setNom(e.target.value)}
        onKeyDown={(e) => { if (e.key === 'Enter') { e.preventDefault(); handleSave() } }}
        placeholder="Nom..."
        className="h-7 text-xs"
      />
      <button
        type="button"
        onClick={handleSave}
        disabled={saving}
        className="h-7 w-7 flex items-center justify-center rounded-md bg-primary text-primary-foreground hover:bg-primary/80 shrink-0"
      >
        <Check size={12} />
      </button>
      <button
        type="button"
        onClick={() => { setOpen(false); setNom('') }}
        className="h-7 w-7 flex items-center justify-center rounded-md border border-border text-muted-foreground hover:text-foreground shrink-0"
      >
        <X size={12} />
      </button>
    </div>
  )
}

// ── Création rapide plateforme (nom + date) ───────────────────────────────────
interface QuickCreatePlateformeProps {
  onSave: (nom: string, date: string) => Promise<void>
}

function QuickCreatePlateforme({ onSave }: QuickCreatePlateformeProps) {
  const [open, setOpen] = useState(false)
  const [nom, setNom] = useState('')
  const [date, setDate] = useState('')
  const [saving, setSaving] = useState(false)

  const handleSave = async () => {
    if (!nom.trim() || !date) return
    setSaving(true)
    try {
      await onSave(nom.trim(), date)
      setNom(''); setDate(''); setOpen(false)
    } finally {
      setSaving(false)
    }
  }

  if (!open) {
    return (
      <button
        type="button"
        onClick={() => setOpen(true)}
        className="flex items-center gap-1 text-xs text-primary hover:underline"
      >
        <Plus size={11} /> Ajouter une plateforme
      </button>
    )
  }

  return (
    <div className="flex items-center gap-1.5 flex-wrap">
      <Input autoFocus value={nom} onChange={(e) => setNom(e.target.value)} placeholder="Nom..." className="h-7 text-xs w-32" />
      <Input type="date" value={date} onChange={(e) => setDate(e.target.value)} className="h-7 text-xs w-36" />
      <button type="button" onClick={handleSave} disabled={saving} className="h-7 w-7 flex items-center justify-center rounded-md bg-primary text-primary-foreground hover:bg-primary/80 shrink-0">
        <Check size={12} />
      </button>
      <button type="button" onClick={() => { setOpen(false); setNom(''); setDate('') }} className="h-7 w-7 flex items-center justify-center rounded-md border border-border text-muted-foreground hover:text-foreground shrink-0">
        <X size={12} />
      </button>
    </div>
  )
}

// ── Dashboard ─────────────────────────────────────────────────────────────────
interface FormState {
  nom: string; description: string; image: string; prix: string
  dateDeSortie: string; genreId: string; editeurId: string
  classificationId: string; plateformeIds: number[]
}

const empty: FormState = {
  nom: '', description: '', image: '', prix: '',
  dateDeSortie: '', genreId: '', editeurId: '', classificationId: '', plateformeIds: [],
}

export function DashboardPage() {
  const { isModo, joueur } = useAuth()

  const [genres, setGenres] = useState<GenreDtoOut[]>([])
  const [editeurs, setEditeurs] = useState<EditeurDtoOut[]>([])
  const [classifications, setClassifications] = useState<ClassificationDtoOut[]>([])
  const [plateformes, setPlateformes] = useState<PlatefomeDtoOut[]>([])
  const [avis, setAvis] = useState<AvisDtoOut[]>([])
  const [loadingAvis, setLoadingAvis] = useState(true)

  const [form, setForm] = useState<FormState>(empty)
  const [submitting, setSubmitting] = useState(false)
  const [success, setSuccess] = useState<string | null>(null)
  const [error, setError] = useState<string | null>(null)

  useEffect(() => {
    Promise.all([
      api.get<GenreDtoOut[]>('/genres'),
      api.get<EditeurDtoOut[]>('/editeurs'),
      api.get<ClassificationDtoOut[]>('/classifications'),
      api.get<PlatefomeDtoOut[]>('/plateformes'),
      api.get<AvisDtoOut[]>('/avis'),
    ]).then(([g, e, c, p, a]) => {
      setGenres(g.data); setEditeurs(e.data)
      setClassifications(c.data); setPlateformes(p.data); setAvis(a.data)
    }).finally(() => setLoadingAvis(false))
  }, [])

  if (!isModo) return <Navigate to="/" replace />

  const handleField = (key: keyof FormState, value: string) =>
    setForm((f) => ({ ...f, [key]: value }))

  const togglePlateforme = (id: number) =>
    setForm((f) => ({
      ...f,
      plateformeIds: f.plateformeIds.includes(id)
        ? f.plateformeIds.filter((p) => p !== id)
        : [...f.plateformeIds, id],
    }))

  // Créations rapides
  const createGenre = async (nom: string) => {
    const { data } = await api.post<GenreDtoOut>('/genres', { nom })
    setGenres((prev) => [...prev, data])
    setForm((f) => ({ ...f, genreId: String(data.id) }))
  }

  const createEditeur = async (nom: string) => {
    const { data } = await api.post<EditeurDtoOut>('/editeurs', { nom })
    setEditeurs((prev) => [...prev, data])
    setForm((f) => ({ ...f, editeurId: String(data.id) }))
  }

  const createClassification = async (nom: string) => {
    const { data } = await api.post<ClassificationDtoOut>('/classifications', { nom, couleurRGB: '#888888' })
    setClassifications((prev) => [...prev, data])
    setForm((f) => ({ ...f, classificationId: String(data.id) }))
  }

  const createPlateforme = async (nom: string, dateDeSortie: string) => {
    const { data } = await api.post<PlatefomeDtoOut>('/plateformes', { nom, dateDeSortie })
    setPlateformes((prev) => [...prev, data])
    setForm((f) => ({ ...f, plateformeIds: [...f.plateformeIds, data.id] }))
  }

  const handleAddJeu = async (e: React.FormEvent) => {
    e.preventDefault()
    if (!joueur) return
    setSubmitting(true); setError(null); setSuccess(null)
    try {
      await api.post(`/moderateurs/${joueur.id}/jeux`, {
        nom: form.nom, description: form.description, image: form.image,
        prix: parseFloat(form.prix), dateDeSortie: form.dateDeSortie,
        genreId: Number(form.genreId), editeurId: Number(form.editeurId),
        classificationId: Number(form.classificationId),
        plateformeIds: form.plateformeIds,
      })
      setSuccess(`Jeu "${form.nom}" ajouté avec succès.`)
      setForm(empty)
    } catch {
      setError('Erreur lors de l\'ajout du jeu.')
    } finally {
      setSubmitting(false)
    }
  }

  const handleDeleteAvis = async (avisId: number) => {
    if (!joueur) return
    await api.delete(`/moderateurs/${joueur.id}/avis/${avisId}`)
    setAvis((prev) => prev.filter((a) => a.id !== avisId))
  }

  return (
    <main className="max-w-4xl mx-auto px-4 sm:px-6 py-8">
      <div className="flex items-center gap-3 mb-8">
        <div className="w-10 h-10 rounded-xl bg-primary/15 border border-primary/30 flex items-center justify-center">
          <ShieldAlert size={20} className="text-primary" />
        </div>
        <div>
          <h1 className="text-2xl font-bold">Dashboard modérateur</h1>
          <p className="text-sm text-muted-foreground">Gestion des jeux et des avis</p>
        </div>
      </div>

      <Tabs defaultValue="jeux">
        <TabsList className="mb-6">
          <TabsTrigger value="jeux">Ajouter un jeu</TabsTrigger>
          <TabsTrigger value="avis">Modérer les avis ({avis.length})</TabsTrigger>
        </TabsList>

        {/* ── Ajouter un jeu ── */}
        <TabsContent value="jeux">
          <form onSubmit={handleAddJeu} className="flex flex-col gap-5">

            {/* Nom + Prix */}
            <div className="grid sm:grid-cols-2 gap-4">
              <div className="flex flex-col gap-1.5">
                <Label htmlFor="nom">Nom du jeu *</Label>
                <Input id="nom" value={form.nom} onChange={(e) => handleField('nom', e.target.value)} required />
              </div>
              <div className="flex flex-col gap-1.5">
                <Label htmlFor="prix">Prix (€) *</Label>
                <Input id="prix" type="number" step="0.01" min="0" value={form.prix} onChange={(e) => handleField('prix', e.target.value)} required />
              </div>
            </div>

            {/* Description */}
            <div className="flex flex-col gap-1.5">
              <Label htmlFor="description">Description *</Label>
              <Textarea id="description" rows={3} value={form.description} onChange={(e) => handleField('description', e.target.value)} required />
            </div>

            {/* Image + Date */}
            <div className="grid sm:grid-cols-2 gap-4">
              <div className="flex flex-col gap-1.5">
                <Label htmlFor="image">URL de l'image</Label>
                <Input id="image" placeholder="https://..." value={form.image} onChange={(e) => handleField('image', e.target.value)} />
              </div>
              <div className="flex flex-col gap-1.5">
                <Label htmlFor="date">Date de sortie *</Label>
                <Input id="date" type="date" value={form.dateDeSortie} onChange={(e) => handleField('dateDeSortie', e.target.value)} required />
              </div>
            </div>

            {/* Genre / Éditeur / Classification */}
            <div className="grid sm:grid-cols-3 gap-4">
              {/* Genre */}
              <div className="flex flex-col gap-1.5">
                <Label>Genre *</Label>
                <Select value={form.genreId} onValueChange={(v) => handleField('genreId', v ?? '')} required>
                  <SelectTrigger>
                    <SelectValue placeholder={genres.length === 0 ? 'Aucun genre' : 'Choisir...'} />
                  </SelectTrigger>
                  {genres.length > 0 && (
                    <SelectContent>
                      {genres.map((g) => <SelectItem key={g.id} value={String(g.id)}>{g.nom}</SelectItem>)}
                    </SelectContent>
                  )}
                </Select>
                <QuickCreate onSave={createGenre} />
              </div>

              {/* Éditeur */}
              <div className="flex flex-col gap-1.5">
                <Label>Éditeur *</Label>
                <Select value={form.editeurId} onValueChange={(v) => handleField('editeurId', v ?? '')} required>
                  <SelectTrigger>
                    <SelectValue placeholder={editeurs.length === 0 ? 'Aucun éditeur' : 'Choisir...'} />
                  </SelectTrigger>
                  {editeurs.length > 0 && (
                    <SelectContent>
                      {editeurs.map((e) => <SelectItem key={e.id} value={String(e.id)}>{e.nom}</SelectItem>)}
                    </SelectContent>
                  )}
                </Select>
                <QuickCreate onSave={createEditeur} />
              </div>

              {/* Classification */}
              <div className="flex flex-col gap-1.5">
                <Label>Classification *</Label>
                <Select value={form.classificationId} onValueChange={(v) => handleField('classificationId', v ?? '')} required>
                  <SelectTrigger>
                    <SelectValue placeholder={classifications.length === 0 ? 'Aucune' : 'Choisir...'} />
                  </SelectTrigger>
                  {classifications.length > 0 && (
                    <SelectContent>
                      {classifications.map((c) => <SelectItem key={c.id} value={String(c.id)}>{c.nom}</SelectItem>)}
                    </SelectContent>
                  )}
                </Select>
                <QuickCreate onSave={createClassification} />
              </div>
            </div>

            {/* Plateformes */}
            <div className="flex flex-col gap-2">
              <Label>Plateformes</Label>
              {plateformes.length > 0 && (
                <div className="flex flex-wrap gap-2">
                  {plateformes.map((p) => (
                    <button
                      key={p.id}
                      type="button"
                      onClick={() => togglePlateforme(p.id)}
                      className={`px-3 py-1 rounded-lg text-sm border transition-colors ${
                        form.plateformeIds.includes(p.id)
                          ? 'bg-primary text-primary-foreground border-primary'
                          : 'bg-muted border-border text-muted-foreground hover:border-primary/50'
                      }`}
                    >
                      {p.nom}
                    </button>
                  ))}
                </div>
              )}
              <QuickCreatePlateforme onSave={createPlateforme} />
            </div>

            {error && <p className="text-sm text-destructive bg-destructive/10 px-3 py-2 rounded-lg">{error}</p>}
            {success && <p className="text-sm text-emerald-400 bg-emerald-400/10 px-3 py-2 rounded-lg">{success}</p>}

            <Button type="submit" disabled={submitting} className="gap-2 self-start">
              <Plus size={15} />
              {submitting ? 'Ajout...' : 'Ajouter le jeu'}
            </Button>
          </form>
        </TabsContent>

        {/* ── Modérer les avis ── */}
        <TabsContent value="avis">
          {loadingAvis ? (
            <div className="flex flex-col gap-3">
              {Array.from({ length: 5 }).map((_, i) => <Skeleton key={i} className="h-20 rounded-xl" />)}
            </div>
          ) : avis.length === 0 ? (
            <div className="py-12 text-center rounded-xl border border-dashed border-border">
              <p className="text-muted-foreground">Aucun avis à modérer.</p>
            </div>
          ) : (
            <div className="flex flex-col gap-2">
              {avis.map((a) => (
                <div key={a.id} className="flex items-start justify-between gap-3 rounded-xl border border-border bg-card px-4 py-3">
                  <div className="flex flex-col gap-1 min-w-0">
                    <div className="flex items-center gap-2 flex-wrap">
                      <span className="text-sm font-semibold">{a.joueurPseudo}</span>
                      <span className="text-xs text-muted-foreground">sur</span>
                      <span className="text-xs text-primary font-medium truncate">{a.jeuNom}</span>
                    </div>
                    <StarRating value={a.note} size={12} />
                    <p className="text-xs text-muted-foreground line-clamp-2 mt-0.5">{a.description}</p>
                  </div>
                  <Button variant="ghost" size="icon" className="shrink-0 h-8 w-8 text-muted-foreground hover:text-destructive" onClick={() => handleDeleteAvis(a.id)}>
                    <Trash2 size={14} />
                  </Button>
                </div>
              ))}
            </div>
          )}
        </TabsContent>
      </Tabs>
    </main>
  )
}
