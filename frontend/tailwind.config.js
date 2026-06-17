/** @type {import('tailwindcss').Config} */
export default {
  content: [
    './index.html',
    './src/**/*.{js,jsx,ts,tsx}',
  ],
  theme: {
    extend: {
      fontFamily: {
        mono: ['"JetBrains Mono"', 'Fira Code', 'ui-monospace', 'monospace'],
        sans: ['Inter', 'system-ui', '-apple-system', 'sans-serif'],
      },
      colors: {
        bloop: {
          bg:       '#0D1117',
          panel:    '#161B22',
          border:   '#30363D',
          accent:   '#7C3AED',
          'accent-light': '#A78BFA',
          success:  '#3FB950',
          error:    '#F85149',
          text:     '#E6EDF3',
          muted:    '#8B949E',
          run:      '#238636',
          'run-hover': '#2EA043',
        },
      },
      keyframes: {
        blink: {
          '0%, 100%': { opacity: '1' },
          '50%':      { opacity: '0' },
        },
        fadeIn: {
          from: { opacity: '0', transform: 'translateY(4px)' },
          to:   { opacity: '1', transform: 'translateY(0)' },
        },
        spin: {
          to: { transform: 'rotate(360deg)' },
        },
      },
      animation: {
        blink:   'blink 1s step-end infinite',
        'fade-in': 'fadeIn 0.15s ease-out',
        spin:    'spin 0.7s linear infinite',
      },
    },
  },
  plugins: [],
}
