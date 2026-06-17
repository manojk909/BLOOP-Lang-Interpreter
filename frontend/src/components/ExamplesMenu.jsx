import { useState, useRef, useEffect } from 'react'
import { EXAMPLES } from '../constants/examples'

export default function ExamplesMenu({ onSelect }) {
  const [open, setOpen] = useState(false)
  const menuRef = useRef(null)

  // Close on outside click
  useEffect(() => {
    if (!open) return
    function handleClick(e) {
      if (menuRef.current && !menuRef.current.contains(e.target)) {
        setOpen(false)
      }
    }
    document.addEventListener('mousedown', handleClick)
    return () => document.removeEventListener('mousedown', handleClick)
  }, [open])

  // Close on Escape
  useEffect(() => {
    if (!open) return
    function handleKey(e) {
      if (e.key === 'Escape') setOpen(false)
    }
    document.addEventListener('keydown', handleKey)
    return () => document.removeEventListener('keydown', handleKey)
  }, [open])

  function handleSelect(example) {
    onSelect(example.code)
    setOpen(false)
  }

  return (
    <div className="relative" ref={menuRef}>
      <button
        onClick={() => setOpen(v => !v)}
        className={`
          flex items-center gap-1.5 px-3 py-1.5 rounded-md text-sm font-medium
          transition-all duration-150 select-none cursor-pointer
          ${open
            ? 'bg-[#21262D] text-[#E6EDF3]'
            : 'text-[#8B949E] hover:text-[#E6EDF3] hover:bg-[#21262D]'
          }
        `}
        aria-haspopup="listbox"
        aria-expanded={open}
      >
        Examples
        <svg
          className={`w-3.5 h-3.5 transition-transform duration-150 ${open ? 'rotate-180' : ''}`}
          fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={2.5}
        >
          <path strokeLinecap="round" strokeLinejoin="round" d="M19 9l-7 7-7-7" />
        </svg>
      </button>

      {open && (
        <div
          className="
            absolute right-0 top-full mt-1.5 z-40
            w-52 bg-[#161B22] border border-[#30363D] rounded-lg
            shadow-2xl shadow-black/60 overflow-hidden dropdown-enter
          "
          role="listbox"
          aria-label="Example programs"
        >
          <div className="py-1">
            {EXAMPLES.map((example, index) => (
              <button
                key={example.id}
                onClick={() => handleSelect(example)}
                className="
                  w-full flex items-center gap-3 px-3 py-2.5 text-sm text-left
                  text-[#8B949E] hover:text-[#E6EDF3] hover:bg-[#21262D]
                  transition-colors duration-100 cursor-pointer
                "
                role="option"
              >
                <span className="text-[#A78BFA] font-mono text-xs w-4 text-center flex-shrink-0">
                  {index + 1}
                </span>
                <span className="font-medium">{example.label}</span>
              </button>
            ))}
          </div>

          {/* Footer hint */}
          <div className="px-3 py-2 border-t border-[#21262D]">
            <p className="text-[10px] text-[#6B7280]">
              Click to load into editor
            </p>
          </div>
        </div>
      )}
    </div>
  )
}
