import { GoogleLogin } from '@react-oauth/google'
import { saveSession, authGoogle } from '../auth'
import { useNavigate } from 'react-router-dom'
import styles from './Login.module.css'

export default function Login() {
  const navigate = useNavigate()

  async function handleSuccess(credentialResponse: { credential?: string }) {
    if (!credentialResponse.credential) return
    try {
      const session = await authGoogle(credentialResponse.credential)
      saveSession(session.token, session.email, session.name)
      navigate('/dashboard')
    } catch {
      alert('Error al autenticar. Intentá de nuevo.')
    }
  }

  return (
    <div className={styles.container}>
      <div className={styles.card}>
        <h1 className={styles.logo}>HECOS</h1>
        <p className={styles.subtitle}>Health Connect Server</p>
        <p className={styles.desc}>
          Visualizá todos tus datos de salud en un solo lugar.
        </p>
        <div className={styles.googleBtn}>
          <GoogleLogin
            onSuccess={handleSuccess}
            onError={() => alert('Error al iniciar sesión')}
          />
        </div>
      </div>
    </div>
  )
}
