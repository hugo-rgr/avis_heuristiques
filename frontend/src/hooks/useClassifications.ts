import { useEffect, useState } from 'react'
import api from '@/lib/api'
import type { ClassificationDtoOut } from '@/types'

const cache: Record<string, string> = {}
let fetched = false
let fetchPromise: Promise<void> | null = null

export function useClassificationColor() {
  const [colorMap, setColorMap] = useState<Record<string, string>>(cache)

  useEffect(() => {
    if (fetched) {
      setColorMap({ ...cache })
      return
    }
    if (!fetchPromise) {
      fetchPromise = api.get<ClassificationDtoOut[]>('/classifications').then(({ data }) => {
        data.forEach((c) => { cache[c.nom] = c.couleurRGB })
        fetched = true
      })
    }
    fetchPromise.then(() => setColorMap({ ...cache }))
  }, [])

  return (nom: string) => colorMap[nom] ?? '#888'
}
