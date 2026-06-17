export const EXAMPLES = [
  {
    id: 'hello-world',
    label: 'Hello World',
    code: `# Classic first program
put "Hello, World!" into msg
print msg`,
  },
  {
    id: 'arithmetic',
    label: 'Arithmetic',
    code: `# Variables and math
put 10 into x
put 3 into y
put x + y * 2 into result
print "Result: " + result`,
  },
  {
    id: 'if-else',
    label: 'If / Else',
    code: `# Conditional logic
put 90 into score
if score > 80 then:
    print "Grade: A"
else:
    print "Grade: F"`,
  },
  {
    id: 'repeat-loop',
    label: 'Repeat Loop',
    code: `# Simple counter loop
put 1 into i
repeat 5 times:
    print "Line " + i
    put i + 1 into i`,
  },
  {
    id: 'nested-blocks',
    label: 'Nested Blocks',
    code: `# Nested if inside repeat
put 1 into i
repeat 5 times:
    if i > 3 then:
        print i + " is big"
    else:
        print i + " is small"
    put i + 1 into i`,
  },
  {
    id: 'grade-classifier',
    label: 'Grade Classifier',
    code: `# Multi-branch grade classification
put 92 into score
print "Score: " + score
if score >= 90 then:
    print "Grade: A"
else:
    if score >= 75 then:
        print "Grade: B"
    else:
        print "Grade: C"`,
  },
]
