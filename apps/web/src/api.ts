import axios from 'axios'

const api = axios.create({
  baseURL: import.meta.env.VITE_API_URL ?? 'https://hecos-api.aguilucho.ar',
})

api.interceptors.request.use((config) => {
  const token = localStorage.getItem('hecos_token')
  if (token) config.headers.Authorization = `Bearer ${token}`
  return config
})

export default api

export interface SummaryResponse {
  total: number
  byType: Record<string, number>
}

export async function getSummary(): Promise<SummaryResponse> {
  const { data } = await api.get<SummaryResponse>('/api/health/summary')
  return data
}

export async function authGoogle(idToken: string) {
  const { data } = await api.post<{ token: string; email: string; name: string }>(
    '/auth/google',
    { idToken }
  )
  return data
}
