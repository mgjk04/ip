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

## Test case: add and list all task types

- **Aim:** Verify that ToDos, Deadlines, and Events are stored polymorphically and displayed in the required format.
- **Command:** `java -cp build/classes Echo`
- **Inputs:**
```text
todo borrow book
deadline return book /by Sunday
event project meeting /from Mon 2pm /to 4pm
list
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
Got it. I've added this task:
[T][ ] borrow book
Now you have 1 tasks in the list.
============================================================
============================================================
Got it. I've added this task:
[D][ ] return book (by: Sunday)
Now you have 2 tasks in the list.
============================================================
============================================================
Got it. I've added this task:
[E][ ] project meeting (from: Mon 2pm to: 4pm)
Now you have 3 tasks in the list.
============================================================
============================================================
Here are the tasks in your list:
1.[T][ ] borrow book
2.[D][ ] return book (by: Sunday)
3.[E][ ] project meeting (from: Mon 2pm to: 4pm)
============================================================
============================================================
Bye!
============================================================
```
