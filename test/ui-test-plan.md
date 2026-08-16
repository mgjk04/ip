# Console UI test plan

Run these cases with `python .codex/skills/test-ui/scripts/run_ui_tests.py` from the repository root. Compile the program first with Java 25; the commands below use the current compiled output directory.

## Test case: greeting and graceful exit

- **Aim:** Verify that Echo presents its greeting and exits cleanly when the user enters `bye`.
- **Command:** `java -cp build/classes Echo`
- **Inputs:**
```text
bye
```
- **Expected output:**
```text
============================================================
 _____     _           
| ____|___| |__   ___  
|  _| / __| '_ \ / _ \ 
| |__| (__| | | | (_) |
|_____\___|_| |_|\___/ 

Hello! I'm Echo.
How can I help?
============================================================
============================================================
Bye!
============================================================
```
