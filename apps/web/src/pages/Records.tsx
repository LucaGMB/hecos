import { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { getRecords } from '../api'
import type { HealthRecord } from '../api'
import { getSession } from '../auth'
import { TYPE_LABELS, getDefaultRangePref, getPageSizePref, rangeToDates } from '../constants'
import NavBar from '../components/NavBar'
import styles from './Records.module.css'

export default function Records() {
  const navigate = useNavigate()
  const session = getSession()

  const [type, setType] = useState('')
  const [from, setFrom] = useState('')
  const [to, setTo] = useState('')
  const [size, setSize] = useState(getPageSizePref())
  const [page, setPage] = useState(0)

  const [records, setRecords] = useState<HealthRecord[]>([])
  const [totalPages, setTotalPages] = useState(0)
  const [totalElements, setTotalElements] = useState(0)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)
  const [expandedId, setExpandedId] = useState<string | null>(null)

  useEffect(() => {
    if (!session) { navigate('/'); return }
    const initial = rangeToDates(getDefaultRangePref())
    if (initial.from) setFrom(toDateInput(initial.from))
    if (initial.to) setTo(toDateInput(initial.to))
  }, [])

  useEffect(() => {
    if (!session) return
    setLoading(true)
    getRecords({
      type: type || undefined,
      from: from ? new Date(from).toISOString() : undefined,
      to: to ? new Date(to).toISOString() : undefined,
      page,
      size,
    })
      .then((res) => {
        setRecords(res.content)
        setTotalPages(res.totalPages)
        setTotalElements(res.totalElements)
        setError(null)
      })
      .catch(() => setError('No se pudieron cargar los registros. ¿Está el backend online?'))
      .finally(() => setLoading(false))
  }, [type, from, to, size, page])

  function toDateInput(iso: string) {
    return iso.slice(0, 10)
  }

  function resetPageAnd<T>(setter: (v: T) => void) {
    return (value: T) => {
      setPage(0)
      setter(value)
    }
  }

  return (
    <div className={styles.layout}>
      <NavBar />

      <main className={styles.main}>
        <h2 className={styles.title}>Registros</h2>

        <div className={styles.filters}>
          <label className={styles.field}>
            <span>Tipo</span>
            <select value={type} onChange={(e) => resetPageAnd(setType)(e.target.value)}>
              <option value="">Todos</option>
              {Object.entries(TYPE_LABELS).map(([slug, label]) => (
                <option key={slug} value={slug}>{label}</option>
              ))}
            </select>
          </label>

          <label className={styles.field}>
            <span>Desde</span>
            <input type="date" value={from} onChange={(e) => resetPageAnd(setFrom)(e.target.value)} />
          </label>

          <label className={styles.field}>
            <span>Hasta</span>
            <input type="date" value={to} onChange={(e) => resetPageAnd(setTo)(e.target.value)} />
          </label>

          <label className={styles.field}>
            <span>Filas por página</span>
            <select value={size} onChange={(e) => resetPageAnd(setSize)(Number(e.target.value))}>
              {[10, 20, 50, 100].map((n) => <option key={n} value={n}>{n}</option>)}
            </select>
          </label>
        </div>

        {loading && <p className={styles.status}>Cargando registros…</p>}
        {error && <p className={styles.error}>{error}</p>}

        {!loading && !error && (
          <>
            <p className={styles.count}>{totalElements.toLocaleString()} registros encontrados</p>

            <div className={styles.tableWrapper}>
              <table className={styles.table}>
                <thead>
                  <tr>
                    <th>Tipo</th>
                    <th>Origen</th>
                    <th>Recibido</th>
                    <th>Datos</th>
                  </tr>
                </thead>
                <tbody>
                  {records.map((r) => (
                    <tr key={r.id}>
                      <td>{TYPE_LABELS[r.type] ?? r.type}</td>
                      <td>{r.sourceApp || '—'}</td>
                      <td>{new Date(r.receivedAt).toLocaleString()}</td>
                      <td>
                        <button
                          className={styles.expandBtn}
                          onClick={() => setExpandedId(expandedId === r.id ? null : r.id)}
                        >
                          {expandedId === r.id ? 'Ocultar' : 'Ver JSON'}
                        </button>
                        {expandedId === r.id && (
                          <pre className={styles.json}>{formatData(r.data)}</pre>
                        )}
                      </td>
                    </tr>
                  ))}
                  {records.length === 0 && (
                    <tr><td colSpan={4} className={styles.empty}>No hay registros para los filtros seleccionados.</td></tr>
                  )}
                </tbody>
              </table>
            </div>

            <div className={styles.pagination}>
              <button disabled={page <= 0} onClick={() => setPage((p) => p - 1)}>Anterior</button>
              <span>Página {totalPages === 0 ? 0 : page + 1} de {totalPages}</span>
              <button disabled={page >= totalPages - 1} onClick={() => setPage((p) => p + 1)}>Siguiente</button>
            </div>
          </>
        )}
      </main>
    </div>
  )
}

function formatData(data: string) {
  try {
    return JSON.stringify(JSON.parse(data), null, 2)
  } catch {
    return data
  }
}
