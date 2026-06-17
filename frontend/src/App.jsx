import { useState, useCallback } from 'react'
import Editor       from './components/Editor'
import OutputPanel  from './components/OutputPanel'
import Toolbar      from './components/Toolbar'
import ExamplesMenu from './components/ExamplesMenu'
import DocsModal    from './components/DocsModal'
import { useRunBloop, useRunShortcut } from './hooks/useRunBloop'
import { EXAMPLES } from './constants/examples'

// Default code shown when the playground first loads
const DEFAULT_CODE = EXAMPLES[0].code

export default function App() {
  const [code,      setCode]      = useState(DEFAULT_CODE)
  const [docsOpen,  setDocsOpen]  = useState(false)

  const { runCode, result, loading, clearResult } = useRunBloop()

  // Trigger a run with the current editor content
  const handleRun = useCallback(() => {
    runCode(code)
  }, [code, runCode])

  // Clear both the output panel and the editor
  const handleClear = useCallback(() => {
    clearResult()
  }, [clearResult])

  // Load an example into the editor and clear stale output
  const handleExampleSelect = useCallback((exampleCode) => {
    setCode(exampleCode)
    clearResult()
  }, [clearResult])

  // Global keyboard shortcut (outside Monaco focus)
  useRunShortcut(handleRun, loading)

  return (
    <div className="flex flex-col h-screen overflow-hidden bg-[#0D1117]">

      {/* ── Header ─────────────────────────────────────────────────────────── */}
      <header className="flex-shrink-0 flex items-center justify-between px-4 py-3 bg-[#161B22] border-b border-[#21262D]">
        {/* Logo / brand */}
        <div className="flex items-center gap-2.5">
          <div className="w-7 h-7 rounded-md bg-[#7C3AED] flex items-center justify-center flex-shrink-0">
            <span className="text-white font-bold text-sm font-mono leading-none">B</span>
          </div>
          <div className="flex items-baseline gap-2">
            <span className="text-[#E6EDF3] font-bold text-base tracking-tight">
              BLOOP
            </span>
            <span className="text-[#8B949E] text-sm font-medium hidden sm:inline">
              Playground
            </span>
          </div>
          {/* Version badge */}
          <span className="hidden md:inline-flex items-center px-1.5 py-0.5 text-[10px] font-mono font-semibold bg-[#7C3AED]/20 text-[#A78BFA] border border-[#7C3AED]/30 rounded">
            v1.0
          </span>
        </div>

        {/* Nav actions */}
        <nav className="flex items-center gap-1">
          {/* Docs button */}
          <button
            onClick={() => setDocsOpen(true)}
            className="
              flex items-center gap-1.5 px-3 py-1.5 rounded-md text-sm font-medium
              text-[#8B949E] hover:text-[#E6EDF3] hover:bg-[#21262D]
              transition-all duration-150 select-none cursor-pointer
            "
            aria-label="Open language reference"
          >
            <svg className="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={2}>
              <path strokeLinecap="round" strokeLinejoin="round"
                d="M12 6.253v13m0-13C10.832 5.477 9.246 5 7.5 5S4.168 5.477 3 6.253v13C4.168 18.477 5.754 18 7.5 18s3.332.477 4.5 1.253m0-13C13.168 5.477 14.754 5 16.5 5c1.747 0 3.332.477 4.5 1.253v13C19.832 18.477 18.247 18 16.5 18c-1.746 0-3.332.477-4.5 1.253"
              />
            </svg>
            <span className="hidden sm:inline">Docs</span>
          </button>

          {/* Examples dropdown */}
          <ExamplesMenu onSelect={handleExampleSelect} />

          {/* GitHub link */}
          <a
            href="https://github.com/manojk909/BLOOP-Lang-Interpreter"
            target="_blank"
            rel="noopener noreferrer"
            className="
              flex items-center gap-1.5 px-3 py-1.5 rounded-md text-sm font-medium
              text-[#8B949E] hover:text-[#E6EDF3] hover:bg-[#21262D]
              transition-all duration-150 select-none ml-1
            "
            aria-label="View source on GitHub"
          >
            <svg className="w-4 h-4" viewBox="0 0 24 24" fill="currentColor">
              <path d="M12 2C6.477 2 2 6.484 2 12.017c0 4.425 2.865 8.18 6.839 9.504.5.092.682-.217.682-.483 0-.237-.008-.868-.013-1.703-2.782.605-3.369-1.343-3.369-1.343-.454-1.158-1.11-1.466-1.11-1.466-.908-.62.069-.608.069-.608 1.003.07 1.531 1.032 1.531 1.032.892 1.53 2.341 1.088 2.91.832.092-.647.35-1.088.636-1.338-2.22-.253-4.555-1.113-4.555-4.951 0-1.093.39-1.988 1.029-2.688-.103-.253-.446-1.272.098-2.65 0 0 .84-.27 2.75 1.026A9.564 9.564 0 0112 6.844c.85.004 1.705.115 2.504.337 1.909-1.296 2.747-1.027 2.747-1.027.546 1.379.202 2.398.1 2.651.64.7 1.028 1.595 1.028 2.688 0 3.848-2.339 4.695-4.566 4.943.359.309.678.92.678 1.855 0 1.338-.012 2.419-.012 2.747 0 .268.18.58.688.482A10.019 10.019 0 0022 12.017C22 6.484 17.522 2 12 2z" />
            </svg>
          </a>
        </nav>
      </header>

      {/* ── Main split layout ───────────────────────────────────────────────── */}
      <main className="flex-1 flex overflow-hidden min-h-0">

        {/* Left: Monaco Editor (~55%) */}
        <div className="flex flex-col w-[55%] min-w-0 border-r border-[#21262D]">
          {/* Editor header bar */}
          <div className="flex-shrink-0 flex items-center justify-between px-4 py-2 border-b border-[#21262D] bg-[#161B22]">
            <div className="flex items-center gap-2">
              <span className="w-2.5 h-2.5 rounded-full bg-[#F85149]" />
              <span className="w-2.5 h-2.5 rounded-full bg-[#F0883E]" />
              <span className="w-2.5 h-2.5 rounded-full bg-[#3FB950]" />
              <span className="ml-2 text-[#8B949E] text-xs font-mono">
                main.bloop
              </span>
            </div>
            <span className="text-[10px] text-[#6B7280] font-mono">BLOOP</span>
          </div>

          {/* Editor fills remaining height */}
          <div className="flex-1 min-h-0 monaco-host">
            <Editor
              value={code}
              onChange={setCode}
              onRun={handleRun}
            />
          </div>
        </div>

        {/* Right: Output panel (~45%) */}
        <div className="flex flex-col w-[45%] min-w-0">
          <OutputPanel result={result} loading={loading} />
        </div>
      </main>

      {/* ── Toolbar ─────────────────────────────────────────────────────────── */}
      <Toolbar
        onRun={handleRun}
        onClear={handleClear}
        loading={loading}
        result={result}
      />

      {/* ── Docs modal ──────────────────────────────────────────────────────── */}
      {docsOpen && <DocsModal onClose={() => setDocsOpen(false)} />}
    </div>
  )
}
