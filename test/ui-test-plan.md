# Console UI test plan

Run these cases with `python .codex/skills/test-ui/scripts/run_ui_tests.py` from the repository root. Compile the program first with Java 25; the commands below use the current compiled output directory.

Because Echo restores saved tasks at startup, every case deletes the save file before it starts (`rm -rf data &&`), so cases are independent of execution order and leftovers.

## Side effects: automatic saving and startup loading

Every successful `todo`, `deadline`, `event`, `mark`, `unmark`, or `delete` command silently rewrites `./data/echo.txt`, relative to the working directory. The file stores one pipe-delimited line per task: type letter (`T`/`D`/`E`), completion flag (`1` = done / `0` = not done), description, then the deadline's due date or the event's start and end times. Running the "add and list all task types" case from a clean state must leave:

```text
T | 0 | borrow book
D | 0 | return book | Sunday
E | 0 | project meeting | Mon 2pm | 4pm
```

On startup Echo reads `data/echo.txt` (if present) and restores those tasks before greeting; a missing file starts an empty list, and a malformed line reports a storage error instead of aborting. Task details must not contain `|` because it separates saved fields; inputs that do are rejected outright (see the "pipe characters are rejected" case). To verify persistence manually from a known state:

1. Delete `data/echo.txt`, run one session that adds `todo borrow book`, then quit with `bye`.
2. Start a new session entering `list`: `[T][ ] borrow book` must appear.
3. Re-running the same session after `mark 1` must show `[T][X] borrow book`.

Successful saves and loads produce no console output, so the expected transcripts below do not change.

## Test case: greeting and graceful exit

- **Aim:** Verify that Echo presents its greeting and exits cleanly when the user enters `bye`.
- **Command:** `rm -rf data && java -cp build/classes Echo`
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
- **Command:** `rm -rf data && java -cp build/classes Echo`
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

## Test case: invalid commands show helpful errors

- **Aim:** Verify that Echo catches invalid commands and malformed task inputs without terminating the session.
- **Command:** `rm -rf data && java -cp build/classes Echo`
- **Inputs:**
```text
todo
blah
deadline submit report
event project meeting /from Monday
mark one
unmark 1
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
OOPS!!! Invalid todo command. Format: todo <description>
============================================================
============================================================
OOPS!!! Unknown command. I'm sorry, but I don't know what that means :(
============================================================
============================================================
OOPS!!! Invalid deadline command. Format: deadline <description> /by <dueDate>
============================================================
============================================================
OOPS!!! Invalid event command. Format: event <description> /from <startTime> /to <endTime>
============================================================
============================================================
OOPS!!! Please provide a valid task number. Check the list and choose a valid task number.
============================================================
============================================================
OOPS!!! Please provide a valid task number. Check the list and choose a valid task number.
============================================================
============================================================
Bye!
============================================================
```

## Test case: errors do not change existing tasks

- **Aim:** Verify that rejected commands leave the task list unchanged while valid commands before and after them still work.
- **Command:** `rm -rf data && java -cp build/classes Echo`
- **Inputs:**
```text
todo
todo read book
mark 1
deadline submit report
event team sync /from 10am /to 11am
unmark 3
unmark 1
blah
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
OOPS!!! Invalid todo command. Format: todo <description>
============================================================
============================================================
Got it. I've added this task:
[T][ ] read book
Now you have 1 tasks in the list.
============================================================
============================================================
Nice! I've marked this task as done:
[T][X] read book
============================================================
============================================================
OOPS!!! Invalid deadline command. Format: deadline <description> /by <dueDate>
============================================================
============================================================
Got it. I've added this task:
[E][ ] team sync (from: 10am to: 11am)
Now you have 2 tasks in the list.
============================================================
============================================================
OOPS!!! Please provide a valid task number. Check the list and choose a valid task number.
============================================================
============================================================
OK, I've marked this task as not done yet:
[T][ ] read book
============================================================
============================================================
OOPS!!! Unknown command. I'm sorry, but I don't know what that means :(
============================================================
============================================================
Here are the tasks in your list:
1.[T][ ] read book
2.[E][ ] team sync (from: 10am to: 11am)
============================================================
============================================================
Bye!
============================================================
```

## Test case: malformed task fields do not consume task numbers

- **Aim:** Verify that incomplete deadline and event fields, plus a command with an unrecognised keyword, do not affect later valid tasks or their numbers.
- **Command:** `rm -rf data && java -cp build/classes Echo`
- **Inputs:**
```text
deadline plan /by
todo practice Java
event workshop /from 9am /to
deadline pay bill /by Friday
mark 2
todoist typo
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
OOPS!!! Invalid deadline command. Format: deadline <description> /by <dueDate>
============================================================
============================================================
Got it. I've added this task:
[T][ ] practice Java
Now you have 1 tasks in the list.
============================================================
============================================================
OOPS!!! Invalid event command. Format: event <description> /from <startTime> /to <endTime>
============================================================
============================================================
Got it. I've added this task:
[D][ ] pay bill (by: Friday)
Now you have 2 tasks in the list.
============================================================
============================================================
Nice! I've marked this task as done:
[D][X] pay bill (by: Friday)
============================================================
============================================================
OOPS!!! Unknown command. I'm sorry, but I don't know what that means :(
============================================================
============================================================
Here are the tasks in your list:
1.[T][ ] practice Java
2.[D][X] pay bill (by: Friday)
============================================================
============================================================
Bye!
============================================================
```

## Test case: delete a task and renumber the remaining list

- **Aim:** Verify that `delete` removes the specified task, reports the new task count, and leaves the remaining tasks in their correct order with consecutive list numbers.
- **Command:** `rm -rf data && java -cp build/classes Echo`
- **Inputs:**
```text
todo read book
deadline return book /by June 6th
event project meeting /from Aug 6th 2pm /to 4pm
todo borrow book
delete 3
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
[T][ ] read book
Now you have 1 tasks in the list.
============================================================
============================================================
Got it. I've added this task:
[D][ ] return book (by: June 6th)
Now you have 2 tasks in the list.
============================================================
============================================================
Got it. I've added this task:
[E][ ] project meeting (from: Aug 6th 2pm to: 4pm)
Now you have 3 tasks in the list.
============================================================
============================================================
Got it. I've added this task:
[T][ ] borrow book
Now you have 4 tasks in the list.
============================================================
============================================================
Noted. I've removed this task:
[E][ ] project meeting (from: Aug 6th 2pm to: 4pm)
Now you have 3 tasks in the list.
============================================================
============================================================
Here are the tasks in your list:
1.[T][ ] read book
2.[D][ ] return book (by: June 6th)
3.[T][ ] borrow book
============================================================
============================================================
Bye!
============================================================
```

## Test case: missing task numbers report format errors

- **Aim:** Verify that mark, unmark, and delete without a task number each report their command-specific format error instead of the generic invalid-number message.
- **Command:** `rm -rf data && java -cp build/classes Echo`
- **Inputs:**
```text
mark
unmark
delete
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
OOPS!!! Invalid mark command. Format: mark <taskNumber>
============================================================
============================================================
OOPS!!! Invalid unmark command. Format: unmark <taskNumber>
============================================================
============================================================
OOPS!!! Invalid delete command. Format: delete <taskNumber>
============================================================
============================================================
Bye!
============================================================
```

## Test case: pipe characters are rejected

- **Aim:** Verify that task details containing the reserved save-file separator '|' are rejected for every task type and leave the list unchanged.
- **Command:** `rm -rf data && java -cp build/classes Echo`
- **Inputs:**
```text
todo evil | plan
deadline x /by Sun|day
event y /from a|b /to c
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
OOPS!!! '|' cannot be used because it separates fields in the save file.
============================================================
============================================================
OOPS!!! '|' cannot be used because it separates fields in the save file.
============================================================
============================================================
OOPS!!! '|' cannot be used because it separates fields in the save file.
============================================================
============================================================
Here are the tasks in your list:

============================================================
============================================================
Bye!
============================================================
```
