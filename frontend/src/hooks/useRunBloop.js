import { useState, useCallback, useEffect } from 'react'

const API_BASE = import.meta.env.VITE_API_URL || 'http://localhost:8080'

/**
 * Custom hook encapsulating all BLOOP execution logic.
 *
 * Returns:
 *   runCode(code)  — async function to POST code to the backend
 *   result         — { stdout, stderr, success, executionTimeMs } | null
 *   loading        — boolean, true while request is in-flight
 *   error          — network-level error string | null (not BLOOP errors)
 *   clearResult    — reset result to null
 */
export function useRunBloop() {
  const [result,  setResult]  = useState(null)
  const [loading, setLoading] = useState(false)
  const [error,   setError]   = useState(null)

  const runCode = useCallback(async (code) => {
    if (loading) return
    setLoading(true)
    setError(null)

    try {
      const res = await fetch(`${API_BASE}/api/run`, {
        method:  'POST',
        headers: { 'Content-Type': 'application/json' },
        body:    JSON.stringify({ code }),
      })

      if (!res.ok) {
        const text = await res.text()
        throw new Error(`Server error ${res.status}: ${text}`)
      }

      const data = await res.json()
      setResult(data)
    } catch (err) {
      // Network/parse failures — surface as a pseudo-result so the
      // output panel renders them the same way BLOOP errors do.
      setResult({
        stdout:          '',
        stderr:          `Network error: ${err.message}`,
        success:         false,
        executionTimeMs: 0,
      })
    } finally {
      setLoading(false)
    }
  }, [loading])

  const clearResult = useCallback(() => {
    setResult(null)
    setError(null)
  }, [])

  return { runCode, result, loading, error, clearResult }
}

/**
 * Attach Ctrl+Enter / Cmd+Enter keyboard shortcut.
 * Call this hook in the component that owns the run action.
 *
 * @param {Function} onRun — called when the shortcut fires
 * @param {boolean}  disabled — when true, shortcut is suppressed
 */
export function useRunShortcut(onRun, disabled = false) {
  useEffect(() => {
    const handler = (e) => {
      if ((e.ctrlKey || e.metaKey) && e.key === 'Enter') {
        e.preventDefault()
        if (!disabled) onRun()
      }
    }
    window.addEventListener('keydown', handler)
    return () => window.removeEventListener('keydown', handler)
  }, [onRun, disabled])
}
