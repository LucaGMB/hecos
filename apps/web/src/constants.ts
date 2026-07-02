export const TYPE_LABELS: Record<string, string> = {
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

export const COLORS = ['#1a73e8', '#34a853', '#fbbc04', '#ea4335', '#9c27b0', '#00bcd4', '#ff9800', '#607d8b']

export const DEFAULT_RANGE_OPTIONS = [
  { value: '7', label: 'Últimos 7 días' },
  { value: '30', label: 'Últimos 30 días' },
  { value: '90', label: 'Últimos 90 días' },
  { value: 'all', label: 'Todo' },
] as const

export const PAGE_SIZE_OPTIONS = [10, 20, 50, 100]

export const PREFS_DEFAULT_RANGE_KEY = 'hecos_pref_default_range'
export const PREFS_PAGE_SIZE_KEY = 'hecos_pref_page_size'

export function getDefaultRangePref(): (typeof DEFAULT_RANGE_OPTIONS)[number]['value'] {
  const stored = localStorage.getItem(PREFS_DEFAULT_RANGE_KEY)
  if (stored && DEFAULT_RANGE_OPTIONS.some((o) => o.value === stored)) {
    return stored as (typeof DEFAULT_RANGE_OPTIONS)[number]['value']
  }
  return 'all'
}

export function getPageSizePref(): number {
  const stored = Number(localStorage.getItem(PREFS_PAGE_SIZE_KEY))
  return PAGE_SIZE_OPTIONS.includes(stored) ? stored : 20
}

export function rangeToDates(range: string): { from?: string; to?: string } {
  if (range === 'all') return {}
  const days = Number(range)
  if (!Number.isFinite(days)) return {}
  const to = new Date()
  const from = new Date()
  from.setDate(from.getDate() - days)
  return { from: from.toISOString(), to: to.toISOString() }
}
