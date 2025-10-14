import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// https://vite.dev/config/
export default defineConfig({
  plugins: [react()],
  
  server: {
    port:3001,
    proxy: {
      // Proxy requests from /auth to your backend
      '/auth': {
        target: 'http://localhost:8080',
        changeOrigin: true,
      },
      // Proxy requests from /api to your backend
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true,
      },
    }
  }
})
