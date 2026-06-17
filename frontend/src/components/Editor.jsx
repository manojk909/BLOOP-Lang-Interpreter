import { useRef, useEffect } from 'react'
import MonacoEditor, { loader } from '@monaco-editor/react'

// ── BLOOP language definition ─────────────────────────────────────────────────
function registerBloopLanguage(monaco) {
  // Only register once
  const existing = monaco.languages.getLanguages().find(l => l.id === 'bloop')
  if (existing) return

  monaco.languages.register({ id: 'bloop' })

  monaco.languages.setMonarchTokensProvider('bloop', {
    keywords: ['put', 'into', 'print', 'if', 'then', 'else', 'repeat', 'times'],

    tokenizer: {
      root: [
        // Comments
        [/#.*$/, 'comment'],

        // String literals
        [/"[^"]*"/, 'string'],

        // Numbers (integer and float)
        [/\b\d+(\.\d+)?\b/, 'number'],

        // Keywords
        [
          /\b(?:put|into|print|if|then|else|repeat|times)\b/,
          {
            cases: {
              '@keywords': 'keyword',
              '@default':  'identifier',
            },
          },
        ],

        // Operators
        [/[+\-*/]/, 'operator'],
        [/>=|<=|==|!=|>|</, 'operator'],

        // Colon (block opener)
        [/:/, 'delimiter'],

        // Identifiers (variable names)
        [/[a-zA-Z_][a-zA-Z0-9_]*/, 'identifier'],

        // Whitespace
        [/\s+/, 'white'],
      ],
    },
  })

  // Visual theme that matches the dark UI
  monaco.editor.defineTheme('bloop-dark', {
    base: 'vs-dark',
    inherit: true,
    rules: [
      { token: 'keyword',    foreground: 'C084FC', fontStyle: 'bold' },
      { token: 'string',     foreground: '86EFAC' },
      { token: 'number',     foreground: 'FCA5A5' },
      { token: 'comment',    foreground: '6B7280', fontStyle: 'italic' },
      { token: 'operator',   foreground: 'F9A8D4' },
      { token: 'delimiter',  foreground: 'FCD34D' },
      { token: 'identifier', foreground: 'E2E8F0' },
    ],
    colors: {
      'editor.background':           '#0D1117',
      'editor.foreground':           '#E6EDF3',
      'editor.lineHighlightBackground': '#161B22',
      'editorLineNumber.foreground': '#3D444D',
      'editorLineNumber.activeForeground': '#8B949E',
      'editor.selectionBackground':  '#2D4A6E',
      'editorCursor.foreground':     '#A78BFA',
      'editor.findMatchBackground':  '#7C3AED44',
    },
  })

  // Minimal autocomplete for keywords
  monaco.languages.registerCompletionItemProvider('bloop', {
    provideCompletionItems: (model, position) => {
      const word  = model.getWordUntilPosition(position)
      const range = {
        startLineNumber: position.lineNumber,
        endLineNumber:   position.lineNumber,
        startColumn:     word.startColumn,
        endColumn:       word.endColumn,
      }
      const keywords = ['put', 'into', 'print', 'if', 'then', 'else', 'repeat', 'times']
      return {
        suggestions: keywords.map(k => ({
          label:           k,
          kind:            monaco.languages.CompletionItemKind.Keyword,
          insertText:      k,
          range,
        })),
      }
    },
  })
}

// ── Component ─────────────────────────────────────────────────────────────────

export default function Editor({ value, onChange, onRun }) {
  const editorRef = useRef(null)

  // Handle Ctrl/Cmd+Enter inside the Monaco editor itself
  useEffect(() => {
    if (!editorRef.current) return
    const editor = editorRef.current
    editor.addCommand(
      // Monaco key codes: 2048 = Ctrl, 3 = Enter; 256 = meta (Cmd)
      monaco.KeyMod.CtrlCmd | monaco.KeyCode.Enter,
      () => onRun?.()
    )
  // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [onRun])

  function handleBeforeMount(monaco) {
    registerBloopLanguage(monaco)
  }

  function handleMount(editor) {
    editorRef.current = editor
    // Attach keyboard shortcut once mounted
    const monaco = editor._themeService?._theme ? window.monaco : null
    editor.addCommand(
      // 2048 = Ctrl/Cmd modifier
      (2048 /* CtrlCmd */ | 3 /* Enter */),
      () => onRun?.()
    )
    editor.focus()
  }

  return (
    <MonacoEditor
      height="100%"
      language="bloop"
      theme="bloop-dark"
      value={value}
      onChange={onChange}
      beforeMount={handleBeforeMount}
      onMount={handleMount}
      options={{
        fontSize:             14,
        fontFamily:           '"JetBrains Mono", "Fira Code", monospace',
        fontLigatures:        true,
        lineNumbers:          'on',
        minimap:              { enabled: false },
        scrollBeyondLastLine: false,
        wordWrap:             'on',
        tabSize:              4,
        insertSpaces:         true,
        automaticLayout:      true,
        padding:              { top: 16, bottom: 16 },
        renderLineHighlight:  'line',
        cursorBlinking:       'smooth',
        smoothScrolling:      true,
        contextmenu:          false,
        overviewRulerLanes:   0,
        hideCursorInOverviewRuler: true,
        scrollbar: {
          verticalScrollbarSize:   6,
          horizontalScrollbarSize: 6,
        },
      }}
    />
  )
}
