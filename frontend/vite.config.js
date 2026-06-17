import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

export default defineConfig({
  plugins: [react()],
  server: {
    port: 3000,
    // Dev proxy so CORS isn't an issue during local development without Docker
    proxy: {
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        secure: false,
      },
    },
  },
  preview: {
    port: 3000,
  },
  // Prevent Vite from complaining about Monaco's large chunks
  build: {
    chunkSizeWarningLimit: 2000,
  },
})
