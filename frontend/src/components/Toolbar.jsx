export default function Toolbar({ onRun, onClear, loading, result }) {
  const isMac = navigator.platform.toUpperCase().includes('MAC')
  const shortcut = isMac ? '⌘↵' : 'Ctrl+↵'

  return (
    <div className="flex-shrink-0 flex items-center gap-3 px-4 py-2.5 bg-[#161B22] border-t border-[#21262D]">

      {/* Run button */}
      <button
        onClick={onRun}
        disabled={loading}
        className={`
          flex items-center gap-2 px-4 py-1.5 rounded-md text-sm font-semibold
          transition-all duration-150 select-none
          ${loading
            ? 'bg-[#238636]/60 text-white/60 cursor-not-allowed'
            : 'bg-[#238636] hover:bg-[#2EA043] active:scale-95 text-white shadow-sm cursor-pointer'
          }
        `}
        title={`Run (${shortcut})`}
        aria-label="Run BLOOP code"
      >
        {loading
          ? <><div className="spinner" /> Running…</>
          : <><span className="text-base leading-none">▶</span> Run</>
        }
        <span className="hidden sm:inline text-[10px] text-white/50 ml-1 font-mono">
          {shortcut}
        </span>
      </button>

      {/* Clear button */}
      <button
        onClick={onClear}
        disabled={loading}
        className="
          flex items-center gap-1.5 px-3 py-1.5 rounded-md text-sm font-medium
          text-[#8B949E] hover:text-[#E6EDF3] hover:bg-[#21262D]
          transition-all duration-150 select-none disabled:opacity-40 disabled:cursor-not-allowed
          cursor-pointer
        "
        title="Clear output"
        aria-label="Clear output"
      >
        <span>⌫</span>
        <span className="hidden sm:inline">Clear</span>
      </button>

      {/* Spacer */}
      <div className="flex-1" />

      {/* Execution time — only shown after a successful run */}
      {result && !loading && (
        <div className="flex items-center gap-2 text-xs text-[#8B949E] font-mono animate-fade-in">
          <span
            className={`w-1.5 h-1.5 rounded-full ${result.success ? 'bg-[#3FB950]' : 'bg-[#F85149]'}`}
          />
          ran in {result.executionTimeMs}ms
        </div>
      )}
    </div>
  )
}
