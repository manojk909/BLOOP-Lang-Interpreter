export const BLOOP_DOCS = {
  title: 'BLOOP Language Reference',
  sections: [
    {
      id: 'variables',
      title: '📦 Variables',
      content: `Assign values using the <code>put ... into ...</code> syntax.`,
      examples: [
        'put 42 into x',
        'put "hello" into name',
        'put x + 1 into x',
      ],
    },
    {
      id: 'print',
      title: '🖨️ Print',
      content: `Output a value or expression to stdout.`,
      examples: [
        'print x',
        'print "Hello, " + name',
        'print 3 + 4',
      ],
    },
    {
      id: 'arithmetic',
      title: '🔢 Arithmetic',
      content: `Standard math operators on numbers. Follows operator precedence (* and / before + and -).`,
      examples: [
        'put 10 + 3 into a    # 13',
        'put 10 - 3 into b    # 7',
        'put 10 * 3 into c    # 30',
        'put 10 / 3 into d    # 3.333...',
        'put 2 + 3 * 4 into e # 14  (not 20)',
      ],
    },
    {
      id: 'strings',
      title: '📝 Strings',
      content: `String literals use double quotes. Concatenate with <code>+</code>. Numbers are auto-converted when mixed.`,
      examples: [
        'put "hello" into greeting',
        'print "Value: " + 42',
        'print "Name: " + name',
      ],
    },
    {
      id: 'comparisons',
      title: '⚖️ Comparisons',
      content: `Used in <code>if</code> conditions. All comparisons return true or false.`,
      table: {
        headers: ['Operator', 'Meaning', 'Example'],
        rows: [
          ['==', 'equals',            'x == 10'],
          ['!=', 'not equals',        'x != 0'],
          ['>',  'greater than',      'x > 5'],
          ['<',  'less than',         'x < 5'],
          ['>=', 'greater or equal',  'x >= 10'],
          ['<=', 'less or equal',     'x <= 10'],
        ],
      },
    },
    {
      id: 'if-else',
      title: '🔀 If / Else',
      content: `Conditional execution using indentation blocks. The <code>else</code> branch is optional. Blocks must be indented with spaces.`,
      examples: [
        `if score > 80 then:\n    print "Pass"\nelse:\n    print "Fail"`,
      ],
    },
    {
      id: 'repeat',
      title: '🔁 Repeat Loop',
      content: `Execute a block N times. N must be a number or numeric variable.`,
      examples: [
        `repeat 5 times:\n    print "hello"`,
        `put 3 into n\nrepeat n times:\n    print "loop"`,
      ],
    },
    {
      id: 'nesting',
      title: '📐 Nesting',
      content: `Blocks can be nested to any depth using consistent indentation (spaces recommended).`,
      examples: [
        `repeat 3 times:\n    if x > 0 then:\n        print "positive"\n    put x - 1 into x`,
      ],
    },
    {
      id: 'comments',
      title: '💬 Comments',
      content: `Lines beginning with <code>#</code> are ignored by the interpreter.`,
      examples: [
        '# This is a comment',
        'put 5 into x  # inline comment',
      ],
    },
  ],
}
