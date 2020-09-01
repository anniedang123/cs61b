# Standard commands and definitions
> init
<<<
# A status log header RE.  Captures the commit id in its sole group.
D UID "[a-f0-9]+"
D COMMIT_HEAD "commit (${UID})"
D DATE "Date: \w\w\w \w\w\w \d+ \d\d:\d\d:\d\d \d\d\d\d [-+]\d\d\d\d"
# A full log entry.  Captures the entry. Assume logs messages don't contain
# ===
D COMMIT_LOG "(===[ ]*\ncommit [a-f0-9]+[ ]*\n${DATE}[ ]*\n(?:.|\n)*?(?=\Z|\n===))"
# An arbitrary line of text (works even with ?s)
D ARBLINE "[^\n]*(?=\n|\Z)"
# Zero or more arbitrary full lines of text.
D ARBLINES "(?:(?:.|\n)*(?:\n|\Z)|\A|\Z)"