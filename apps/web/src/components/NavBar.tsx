import { NavLink, useNavigate } from 'react-router-dom'
import { clearSession, getSession } from '../auth'
import styles from './NavBar.module.css'

const LINKS = [
  { to: '/dashboard', label: 'Panel' },
  { to: '/records', label: 'Registros' },
  { to: '/profile', label: 'Perfil' },
]

export default function NavBar() {
  const navigate = useNavigate()
  const session = getSession()

  function signOut() {
    clearSession()
    navigate('/')
  }

  return (
    <header className={styles.header}>
      <span className={styles.headerLogo}>HECOS</span>

      <nav className={styles.nav}>
        {LINKS.map((link) => (
          <NavLink
            key={link.to}
            to={link.to}
            className={({ isActive }) => (isActive ? `${styles.link} ${styles.linkActive}` : styles.link)}
          >
            {link.label}
          </NavLink>
        ))}
      </nav>

      <div className={styles.headerRight}>
        <span className={styles.email}>{session?.email}</span>
        <button className={styles.signOut} onClick={signOut}>Cerrar sesión</button>
      </div>
    </header>
  )
}
