import { Star } from 'lucide-react'
import { cn } from '@/lib/utils'

interface StarRatingProps {
  value: number        // 0–5
  max?: number
  size?: number
  className?: string
  interactive?: false
}

interface InteractiveStarRatingProps {
  value: number
  max?: number
  size?: number
  className?: string
  interactive: true
  onChange: (value: number) => void
}

type Props = StarRatingProps | InteractiveStarRatingProps

export function StarRating({ value, max = 5, size = 16, className, ...rest }: Props) {
  const interactive = 'interactive' in rest && rest.interactive

  return (
    <div className={cn('flex items-center gap-0.5', className)}>
      {Array.from({ length: max }, (_, i) => {
        const filled = i < Math.floor(value)
        const partial = !filled && i < value

        return (
          <button
            key={i}
            type="button"
            disabled={!interactive}
            onClick={() => {
              if (interactive && 'onChange' in rest) rest.onChange(i + 1)
            }}
            className={cn(
              'relative transition-transform',
              interactive && 'cursor-pointer hover:scale-110',
              !interactive && 'cursor-default'
            )}
            style={{ width: size, height: size }}
          >
            {/* Background star (empty) */}
            <Star
              size={size}
              className="absolute inset-0 text-muted-foreground/30"
              fill="currentColor"
              strokeWidth={0}
            />
            {/* Filled star */}
            {(filled || partial) && (
              <Star
                size={size}
                className="absolute inset-0 text-amber-400"
                fill="currentColor"
                strokeWidth={0}
                style={partial ? { clipPath: `inset(0 ${(1 - (value % 1)) * 100}% 0 0)` } : undefined}
              />
            )}
          </button>
        )
      })}
    </div>
  )
}
