import { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { BarChart, Bar, XAxis, YAxis, Tooltip, ResponsiveContainer } from 'recharts'
import { getSummary } from '../api'
import type { SummaryResponse } from '../api'
import { getSession } from '../auth'
import { TYPE_LABELS, COLORS, getDefaultRangePref, DEFAULT_RANGE_OPTIONS } from '../constants'
import NavBar from '../components/NavBar'
import styles from './Dashboard.module.css'

export default function Dashboard() {
  const navigate = useNavigate()
  const session = getSession()
  const [summary, setSummary] = useState<SummaryResponse | null>(null)
  const [error, setError] = useState<string | null>(null)
  const [loading, setLoading] = useState(true)
  const defaultRange = getDefaultRangePref()
  const defaultRangeLabel = DEFAULT_RANGE_OPTIONS.find((o) => o.value === defaultRange)?.label ?? 'Todo'

  useEffect(() => {
    if (!session) { navigate('/'); return }
    getSummary()
      .then(setSummary)
      .catch(() => setError('No se pudo cargar el resumen. ¿Está el backend online?'))
      .finally(() => setLoading(false))
  }, [])

  const chartData = summary
    ? Object.entries(summary.byType)
        .sort((a, b) => b[1] - a[1])
        .slice(0, 12)
        .map(([type, count]) => ({ name: TYPE_LABELS[type] ?? type, count }))
    : []

  return (
    <div className={styles.layout}>
      <NavBar />

      <main className={styles.main}>
        <h2 className={styles.title}>Panel de salud</h2>
        <p className={styles.status}>Rango por defecto (Registros): {defaultRangeLabel}</p>

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
