import { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { BarChart, Bar, XAxis, YAxis, Tooltip, ResponsiveContainer } from 'recharts'
import { getSummary } from '../api'
import type { SummaryResponse } from '../api'
import { clearSession, getSession } from '../auth'
import styles from './Dashboard.module.css'

const TYPE_LABELS: Record<string, string> = {
  steps: 'Pasos',
  distance: 'Distancia',
  'heart-rate': 'Freq. cardíaca',
  'resting-heart-rate': 'FC en reposo',
  'heart-rate-variability': 'HRV',
  'blood-pressure': 'Presión arterial',
  'blood-glucose': 'Glucemia',
  'oxygen-saturation': 'SpO₂',
  'respiratory-rate': 'Freq. respiratoria',
  'body-temperature': 'Temperatura',
  weight: 'Peso',
  height: 'Altura',
  'body-fat': 'Grasa corporal',
  'lean-body-mass': 'Masa magra',
  'bone-mass': 'Masa ósea',
  'body-water-mass': 'Agua corporal',
  'basal-metabolic-rate': 'TMB',
  'total-calories-burned': 'Calorías totales',
  'active-calories-burned': 'Calorías activas',
  'exercise-session': 'Ejercicio',
  'floors-climbed': 'Pisos',
  'elevation-gained': 'Elevación',
  vo2max: 'VO₂ máx',
  'sleep-session': 'Sueño',
  nutrition: 'Nutrición',
  hydration: 'Hidratación',
}

const COLORS = ['#1a73e8','#34a853','#fbbc04','#ea4335','#9c27b0','#00bcd4','#ff9800','#607d8b']

export default function Dashboard() {
  const navigate = useNavigate()
  const session = getSession()
  const [summary, setSummary] = useState<SummaryResponse | null>(null)
  const [error, setError] = useState<string | null>(null)
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    if (!session) { navigate('/'); return }
    getSummary()
      .then(setSummary)
      .catch(() => setError('No se pudo cargar el resumen. ¿Está el backend online?'))
      .finally(() => setLoading(false))
  }, [])

  function signOut() {
    clearSession()
    navigate('/')
  }

  const chartData = summary
    ? Object.entries(summary.byType)
        .sort((a, b) => b[1] - a[1])
        .slice(0, 12)
        .map(([type, count]) => ({ name: TYPE_LABELS[type] ?? type, count }))
    : []

  return (
    <div className={styles.layout}>
      <header className={styles.header}>
        <span className={styles.headerLogo}>HECOS</span>
        <div className={styles.headerRight}>
          <span className={styles.email}>{session?.email}</span>
          <button className={styles.signOut} onClick={signOut}>Cerrar sesión</button>
        </div>
      </header>

      <main className={styles.main}>
        <h2 className={styles.title}>Panel de salud</h2>

        {loading && <p className={styles.status}>Cargando datos…</p>}
        {error && <p className={styles.error}>{error}</p>}

        {summary && (
          <>
            <div className={styles.statCard}>
              <span className={styles.statNumber}>{summary.total.toLocaleString()}</span>
              <span className={styles.statLabel}>registros sincronizados en total</span>
            </div>

            <h3 className={styles.sectionTitle}>Registros por tipo</h3>
            <div className={styles.chartCard}>
              <ResponsiveContainer width="100%" height={320}>
                <BarChart data={chartData} margin={{ top: 8, right: 16, left: 0, bottom: 60 }}>
                  <XAxis dataKey="name" angle={-40} textAnchor="end" tick={{ fontSize: 12 }} />
                  <YAxis tick={{ fontSize: 12 }} />
                  <Tooltip />
                  <Bar dataKey="count" radius={[6, 6, 0, 0]} fill={COLORS[0]} />
                </BarChart>
              </ResponsiveContainer>
            </div>

            <h3 className={styles.sectionTitle}>Detalle</h3>
            <div className={styles.grid}>
              {Object.entries(summary.byType)
                .sort((a, b) => b[1] - a[1])
                .map(([type, count], i) => (
                  <div key={type} className={styles.typeCard}>
                    <span className={styles.dot} style={{ background: COLORS[i % COLORS.length] }} />
                    <div>
                      <p className={styles.typeName}>{TYPE_LABELS[type] ?? type}</p>
                      <p className={styles.typeCount}>{count.toLocaleString()} registros</p>
                    </div>
                  </div>
                ))}
            </div>
          </>
        )}
      </main>
    </div>
  )
}
