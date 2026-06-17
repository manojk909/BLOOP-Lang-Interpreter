import { useEffect, useRef } from 'react'

export default function OutputPanel({ result, loading }) {
  const scrollRef = useRef(null)

  // Auto-scroll to bottom whenever output changes
  useEffect(() => {
    if (scrollRef.current) {
      scrollRef.current.scrollTop = scrollRef.current.scrollHeight
    }
  }, [result])

  // ── States ───────────────────────────────────────────────────────────────

  if (loading) {
    return (
      <div className="flex flex-col h-full bg-[#0D1117]">
        <PanelHeader />
        <div className="flex-1 flex items-center justify-center">
          <div className="flex flex-col items-center gap-3">
            <div className="spinner" />
            <span className="text-[#8B949E] text-sm font-mono">Running…</span>
          </div>
        </div>
      </div>
    )
  }

  if (!result) {
    return (
      <div className="flex flex-col h-full bg-[#0D1117]">
        <PanelHeader />
        <div className="flex-1 flex items-center justify-center">
          <div className="text-center select-none">
            <div className="text-4xl mb-3 opacity-30">▶</div>
            <p className="text-[#8B949E] text-sm">
              Press <kbd className="px-1.5 py-0.5 bg-[#21262D] border border-[#30363D] rounded text-xs font-mono">Ctrl+Enter</kbd> or click <span className="text-[#3FB950] font-medium">Run</span> to execute
            </p>
          </div>
        </div>
      </div>
    )
  }

  const { stdout, stderr, success, executionTimeMs } = result
  const hasOutput = stdout && stdout.trim().length > 0
  const hasError  = stderr && stderr.trim().length > 0

  return (
    <div className="flex flex-col h-full bg-[#0D1117]">
      <PanelHeader success={success} hasResult />

      {/* Output area */}
      <div
        ref={scrollRef}
        className="flex-1 overflow-y-auto custom-scroll p-4 font-mono text-sm leading-relaxed"
      >
        {/* Success output */}
        {success && hasOutput && (
          <div className="animate-fade-in">
            {stdout.split('\n').map((line, i) => (
              <div key={i} className="text-[#E6EDF3] whitespace-pre-wrap break-words">
                {line || <span>&nbsp;</span>}
              </div>
            ))}
          </div>
        )}

        {/* Success but empty output */}
        {success && !hasOutput && (
          <div className="animate-fade-in text-[#8B949E] italic text-sm">
            No output
          </div>
        )}

        {/* Error output */}
        {!success && (
          <div className="animate-fade-in">
            {/* stdout before the error (if any) */}
            {hasOutput && (
              <div className="mb-3">
                {stdout.split('\n').map((line, i) => (
                  <div key={i} className="text-[#E6EDF3] whitespace-pre-wrap break-words">
                    {line || <span>&nbsp;</span>}
                  </div>
                ))}
              </div>
            )}

            {/* Error block */}
            <div className="flex items-start gap-2 p-3 bg-[#21131388] border border-[#F85149]/40 rounded-lg">
              <span className="text-[#F85149] mt-px flex-shrink-0 text-base">✕</span>
              <div className="text-[#F85149] whitespace-pre-wrap break-words flex-1 leading-relaxed">
                {stderr || 'An unknown error occurred.'}
              </div>
            </div>
          </div>
        )}
      </div>

      {/* Execution time badge */}
      <div className="flex-shrink-0 px-4 py-2 border-t border-[#21262D] flex items-center justify-between">
        <span className="text-[#8B949E] text-xs">
          {success
            ? <span className="text-[#3FB950]">✓ Success</span>
            : <span className="text-[#F85149]">✗ Error</span>
          }
        </span>
        <span className="text-[#8B949E] text-xs font-mono bg-[#21262D] px-2 py-0.5 rounded">
          {executionTimeMs}ms
        </span>
      </div>
    </div>
  )
}

function PanelHeader({ success, hasResult }) {
  return (
    <div className="flex-shrink-0 flex items-center justify-between px-4 py-2.5 border-b border-[#21262D]">
      <span className="text-xs font-semibold text-[#8B949E] uppercase tracking-wider">
        Output
      </span>
      {hasResult && (
        <div
          className={`w-2 h-2 rounded-full ${success ? 'bg-[#3FB950]' : 'bg-[#F85149]'}`}
          title={success ? 'Success' : 'Error'}
        />
      )}
    </div>
  )
}
