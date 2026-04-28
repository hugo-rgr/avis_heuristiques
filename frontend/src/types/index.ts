export interface JeuDtoOut {
  id: number
  nom: string
  description: string
  image: string
  prix: number
  dateDeSortie: string
  genreNom: string
  editeurNom: string
  classificationNom: string
  plateformes: string[]
}

export interface AvisDtoOut {
  id: number
  description: string
  note: number
  jeuNom: string
  joueurPseudo: string
  moderateurPseudo: string | null
  dateDEnvoi: string
}

export interface AvisDtoIn {
  id?: number
  description: string
  jeuId: number
  joueurId: number
  note: number
  moderateurId?: number | null
  dateDEnvoi: string
}

export interface JoueurDtoOut {
  id: number
  pseudo: string
  email: string
  dateDeNaissance: string
  avatarNom: string | null
}

export interface JoueurDtoIn {
  pseudo: string
  email: string
  motDePasse: string
  dateDeNaissance: string
}

export interface LoginDtoIn {
  email: string
  motDePasse: string
}

export interface TokenDtoOut {
  token: string
  role: string
}

export interface GenreDtoOut {
  id: number
  nom: string
}

export interface EditeurDtoOut {
  id: number
  nom: string
}

export interface ClassificationDtoOut {
  id: number
  nom: string
  couleurRGB: string
}

export interface PlatefomeDtoOut {
  id: number
  nom: string
  dateDeSortie: string
  jeuxIds: number[]
}

export interface JeuDtoIn {
  id?: number
  nom: string
  description: string
  genreId: number
  image: string
  prix: number
  dateDeSortie: string
  editeurId: number
  classificationId: number
  plateformeIds: number[]
}
