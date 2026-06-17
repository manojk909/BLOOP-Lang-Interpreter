import { useEffect } from 'react'
import { BLOOP_DOCS } from '../constants/bloopDocs'

export default function DocsModal({ onClose }) {
  // Close on Escape
  useEffect(() => {
    function handleKey(e) {
      if (e.key === 'Escape') onClose()
    }
    document.addEventListener('keydown', handleKey)
    return () => document.removeEventListener('keydown', handleKey)
  }, [onClose])

  return (
    <div className="modal-overlay" onClick={onClose} role="dialog" aria-modal aria-label="BLOOP Language Reference">
      {/* Stop click-through to overlay */}
      <div
        className="
          relative w-full max-w-2xl max-h-[88vh] flex flex-col
          bg-[#161B22] border border-[#30363D] rounded-xl
          shadow-2xl shadow-black/70 overflow-hidden
        "
        onClick={e => e.stopPropagation()}
      >
        {/* Modal header */}
        <div className="flex-shrink-0 flex items-center justify-between px-6 py-4 border-b border-[#21262D]">
          <div>
            <h2 className="text-[#E6EDF3] font-bold text-lg">BLOOP Language Reference</h2>
            <p className="text-[#8B949E] text-xs mt-0.5">A complete guide to the BLOOP toy language</p>
          </div>
          <button
            onClick={onClose}
            className="
              w-8 h-8 flex items-center justify-center rounded-md
              text-[#8B949E] hover:text-[#E6EDF3] hover:bg-[#21262D]
              transition-colors duration-100 cursor-pointer text-xl leading-none
            "
            aria-label="Close docs"
          >
            ×
          </button>
        </div>

        {/* Scrollable content */}
        <div className="flex-1 overflow-y-auto custom-scroll px-6 py-5 space-y-7">
          {BLOOP_DOCS.sections.map(section => (
            <section key={section.id}>
              {/* Section heading */}
              <h3 className="text-[#E6EDF3] font-semibold text-sm mb-2">
                {section.title}
              </h3>

              {/* Description */}
              <p
                className="text-[#8B949E] text-sm mb-3 leading-relaxed"
                dangerouslySetInnerHTML={{ __html: section.content }}
              />

              {/* Operator table */}
              {section.table && (
                <div className="overflow-x-auto mb-3">
                  <table className="docs-table">
                    <thead>
                      <tr>
                        {section.table.headers.map(h => (
                          <th key={h}>{h}</th>
                        ))}
                      </tr>
                    </thead>
                    <tbody>
                      {section.table.rows.map(([op, meaning, ex]) => (
                        <tr key={op} className="hover:bg-[#21262D] transition-colors duration-75">
                          <td><code className="text-[#F9A8D4]">{op}</code></td>
                          <td className="text-[#8B949E]">{meaning}</td>
                          <td><code className="text-[#A78BFA]">{ex}</code></td>
                        </tr>
                      ))}
                    </tbody>
                  </table>
                </div>
              )}

              {/* Code examples */}
              {section.examples && section.examples.length > 0 && (
                <div className="space-y-2">
                  {section.examples.map((ex, i) => (
                    <pre
                      key={i}
                      className="
                        bg-[#0D1117] border border-[#21262D] rounded-md
                        px-4 py-3 text-[#86EFAC] text-xs font-mono
                        leading-relaxed overflow-x-auto
                      "
                    >
                      {ex}
                    </pre>
                  ))}
                </div>
              )}
            </section>
          ))}

          {/* Quick-reference cheatsheet */}
          <section>
            <h3 className="text-[#E6EDF3] font-semibold text-sm mb-2">⚡ Quick Reference</h3>
            <pre className="bg-[#0D1117] border border-[#21262D] rounded-md px-4 py-3 text-xs font-mono leading-relaxed overflow-x-auto">
              <span className="text-[#8B949E]"># Variables</span>{'\n'}
              <span className="text-[#C084FC]">put</span>{' '}<span className="text-[#FCA5A5]">42</span>{' '}<span className="text-[#C084FC]">into</span>{' x\n'}
              <span className="text-[#C084FC]">put</span>{' '}<span className="text-[#86EFAC]">"hello"</span>{' '}<span className="text-[#C084FC]">into</span>{' name\n\n'}

              <span className="text-[#8B949E]"># Output</span>{'\n'}
              <span className="text-[#C084FC]">print</span>{' x\n'}
              <span className="text-[#C084FC]">print</span>{' '}<span className="text-[#86EFAC]">"Hi "</span>{' + name\n\n'}

              <span className="text-[#8B949E]"># Conditions</span>{'\n'}
              <span className="text-[#C084FC]">if</span>{' x > '}<span className="text-[#FCA5A5]">0</span>{' '}<span className="text-[#C084FC]">then</span>{':\n'}
              {'    '}<span className="text-[#C084FC]">print</span>{' '}<span className="text-[#86EFAC]">"positive"</span>{'\n'}
              <span className="text-[#C084FC]">else</span>{':\n'}
              {'    '}<span className="text-[#C084FC]">print</span>{' '}<span className="text-[#86EFAC]">"non-positive"</span>{'\n\n'}

              <span className="text-[#8B949E]"># Loops</span>{'\n'}
              <span className="text-[#C084FC]">repeat</span>{' '}<span className="text-[#FCA5A5]">5</span>{' '}<span className="text-[#C084FC]">times</span>{':\n'}
              {'    '}<span className="text-[#C084FC]">print</span>{' '}<span className="text-[#86EFAC]">"loop"</span>
            </pre>
          </section>
        </div>

        {/* Modal footer */}
        <div className="flex-shrink-0 px-6 py-3 border-t border-[#21262D] flex justify-end">
          <button
            onClick={onClose}
            className="
              px-4 py-1.5 bg-[#21262D] hover:bg-[#30363D]
              text-[#E6EDF3] text-sm font-medium rounded-md
              transition-colors duration-150 cursor-pointer
            "
          >
            Close
          </button>
        </div>
      </div>
    </div>
  )
}
