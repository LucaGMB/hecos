import { authGoogle as apiAuthGoogle } from './api'

export async function authGoogle(idToken: string) {
  return apiAuthGoogle(idToken)
}

export function saveSession(token: string, email: string, name: string) {
  localStorage.setItem('hecos_token', token)
  localStorage.setItem('hecos_email', email)
  localStorage.setItem('hecos_name', name)
}

export function clearSession() {
  localStorage.removeItem('hecos_token')
  localStorage.removeItem('hecos_email')
  localStorage.removeItem('hecos_name')
}

export function getSession() {
  const token = localStorage.getItem('hecos_token')
  if (!token) return null
  return {
    token,
    email: localStorage.getItem('hecos_email') ?? '',
    name: localStorage.getItem('hecos_name') ?? '',
  }
}
