import { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { clearSession, getSession } from '../auth'
import {
  DEFAULT_RANGE_OPTIONS,
  PAGE_SIZE_OPTIONS,
  PREFS_DEFAULT_RANGE_KEY,
  PREFS_PAGE_SIZE_KEY,
  getDefaultRangePref,
  getPageSizePref,
} from '../constants'
import NavBar from '../components/NavBar'
import styles from './Profile.module.css'

export default function Profile() {
  const navigate = useNavigate()
  const session = getSession()

  const [defaultRange, setDefaultRange] = useState(getDefaultRangePref())
  const [pageSize, setPageSize] = useState(getPageSizePref())
  const [saved, setSaved] = useState(false)

  useEffect(() => {
    if (!session) navigate('/')
  }, [])

  function savePrefs() {
    localStorage.setItem(PREFS_DEFAULT_RANGE_KEY, defaultRange)
    localStorage.setItem(PREFS_PAGE_SIZE_KEY, String(pageSize))
    setSaved(true)
    setTimeout(() => setSaved(false), 2000)
  }

  function signOut() {
    clearSession()
    navigate('/')
  }

  return (
    <div className={styles.layout}>
      <NavBar />

      <main className={styles.main}>
        <h2 className={styles.title}>Perfil</h2>

        <div className={`${styles.card} ${styles.profileCard}`}>
          {session?.avatarUrl
            ? <img className={styles.avatar} src={session.avatarUrl} alt={session.name} />
            : <div className={styles.avatarFallback}>{(session?.name || session?.email || '?')[0]?.toUpperCase()}</div>}
          <div>
            <p className={styles.name}>{session?.name || 'Usuario'}</p>
            <p className={styles.email}>{session?.email}</p>
          </div>
        </div>

        <h3 className={styles.sectionTitle}>Preferencias de visualización</h3>
        <div className={styles.card}>
          <label className={styles.field}>
            <span>Rango de fechas por defecto</span>
            <select value={defaultRange} onChange={(e) => setDefaultRange(e.target.value as typeof defaultRange)}>
              {DEFAULT_RANGE_OPTIONS.map((o) => (
                <option key={o.value} value={o.value}>{o.label}</option>
              ))}
            </select>
          </label>

          <label className={styles.field}>
            <span>Filas por página en Registros</span>
            <select value={pageSize} onChange={(e) => setPageSize(Number(e.target.value))}>
              {PAGE_SIZE_OPTIONS.map((n) => <option key={n} value={n}>{n}</option>)}
            </select>
          </label>

          <button className={styles.saveBtn} onClick={savePrefs}>Guardar preferencias</button>
          {saved && <span className={styles.savedMsg}>Preferencias guardadas</span>}
        </div>

        <button className={styles.signOut} onClick={signOut}>Cerrar sesión</button>
      </main>
    </div>
  )
}
