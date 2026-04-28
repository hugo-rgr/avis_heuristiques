import { Link, useNavigate } from 'react-router-dom'
import { Gamepad2, LogOut, ShieldAlert } from 'lucide-react'
import { Button } from '@/components/ui/button'
import { buttonVariants } from '@/components/ui/button'
import { cn } from '@/lib/utils'
import { useAuth } from '@/context/AuthContext'

export function Navbar() {
  const { isAuthenticated, isModo, joueur, logout } = useAuth()
  const navigate = useNavigate()

  const handleLogout = () => {
    logout()
    navigate('/login')
  }

  return (
    <header className="sticky top-0 z-40 border-b border-border/50 bg-background/80 backdrop-blur-md">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 h-14 flex items-center justify-between gap-4">
        {/* Logo */}
        <Link to="/" className="flex items-center gap-2 text-primary font-bold text-lg tracking-tight shrink-0">
          <Gamepad2 size={22} />
          <span>Excali<span className="text-foreground">bur</span></span>
        </Link>

        {/* Nav links */}
        <nav className="hidden sm:flex items-center gap-1">
          <Link
            to="/"
            className="text-sm text-muted-foreground hover:text-foreground px-3 py-1.5 rounded-md hover:bg-accent transition-colors"
          >
            Jeux
          </Link>
          {isModo && (
            <Link
              to="/dashboard"
              className="flex items-center gap-1.5 text-sm text-primary hover:text-primary/80 px-3 py-1.5 rounded-md hover:bg-primary/10 transition-colors font-medium"
            >
              <ShieldAlert size={14} />
              Dashboard
            </Link>
          )}
        </nav>

        {/* Auth */}
        <div className="flex items-center gap-2">
          {isAuthenticated ? (
            <>
              {/* Badge rôle */}
              {isModo && (
                <span className="hidden sm:inline text-[10px] font-bold uppercase tracking-wider text-primary bg-primary/10 border border-primary/20 px-2 py-0.5 rounded-full">
                  Modérateur
                </span>
              )}

              <Link
                to={isModo ? '/dashboard' : '/profil'}
                className="flex items-center gap-1.5 text-sm text-muted-foreground hover:text-foreground px-2 py-1 rounded-md hover:bg-accent transition-colors"
              >
                <div className={cn(
                  'w-6 h-6 rounded-full flex items-center justify-center text-[10px] font-bold uppercase',
                  isModo ? 'bg-primary/20 text-primary' : 'bg-primary/20 text-primary'
                )}>
                  {joueur?.pseudo.charAt(0)}
                </div>
                <span className="hidden sm:inline">{joueur?.pseudo}</span>
              </Link>

              <Button
                variant="ghost"
                size="icon"
                className="h-8 w-8 text-muted-foreground hover:text-foreground"
                onClick={handleLogout}
              >
                <LogOut size={15} />
              </Button>
            </>
          ) : (
            <>
              <Link to="/login" className={cn(buttonVariants({ variant: 'ghost', size: 'sm' }))}>
                Connexion
              </Link>
              <Link to="/inscription" className={cn(buttonVariants({ size: 'sm' }))}>
                S'inscrire
              </Link>
            </>
          )}
        </div>
      </div>
    </header>
  )
}
