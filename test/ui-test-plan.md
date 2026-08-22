# Console UI test plan

Run these cases with `python .opencode/skills/test-ui/scripts/run_ui_tests.py` from the repository root. Compile the program first with Java 25 (`javac -d build/classes $(find src/main/java -name "*.java")`).

Because Echo restores saved tasks at startup, every case deletes the save file before it starts (`rm -rf data &&`), so cases are independent of execution order and leftovers.

## Dates and times (Level-8)

Deadline and event times are entered as `yyyy-MM-dd HHmm`, e.g. `2019-12-02 1800`. Echo parses them into `java.time.LocalDateTime` objects and displays them differently, e.g. `Dec 02 2019, 6:00 PM`. Any other shape, including the slash style `2/12/2019 1800` or a date without a time, is rejected with a format error naming the accepted format; see the "date values are validated and formatted" case.

## Side effects: automatic saving and startup loading

Every successful `todo`, `deadline`, `event`, `mark`, `unmark`, or `delete` command silently rewrites `./data/echo.txt`, relative to the working directory. The file stores one pipe-delimited line per task: type letter (`T`/`D`/`E`), completion flag (`1` = done / `0` = not done), description, then the deadline's due date or the event's start and end times, each written as ISO-8601 text (`yyyy-MM-ddTHH:mm`). Running the "add and list all task types" case from a clean state must leave:

```text
T | 0 | borrow book
D | 0 | return book | 2019-12-02T18:00
E | 0 | project meeting | 2019-12-02T14:00 | 2019-12-02T16:00
```

On startup Echo reads `data/echo.txt` (if present) and restores those tasks before greeting; a missing file starts an empty list, and a malformed line reports a storage error instead of aborting (see the last two cases). Task details must not contain `|` because it separates saved fields; inputs that do are rejected outright (see the "pipe characters are rejected" case). To verify persistence manually from a known state:

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

- **Aim:** Verify that ToDos, Deadlines, and Events are stored polymorphically, accept `yyyy-MM-dd HHmm` dates, and display them reformatted.
- **Command:** `rm -rf data && java -cp build/classes Echo`
- **Inputs:**
```text
todo borrow book
deadline return book /by 2019-12-02 1800
event project meeting /from 2019-12-02 1400 /to 2019-12-02 1600
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
[D][ ] return book (by: Dec 02 2019, 6:00 PM)
Now you have 2 tasks in the list.
============================================================
============================================================
Got it. I've added this task:
[E][ ] project meeting (from: Dec 02 2019, 2:00 PM to: Dec 02 2019, 4:00 PM)
Now you have 3 tasks in the list.
============================================================
============================================================
Here are the tasks in your list:
1.[T][ ] borrow book
2.[D][ ] return book (by: Dec 02 2019, 6:00 PM)
3.[E][ ] project meeting (from: Dec 02 2019, 2:00 PM to: Dec 02 2019, 4:00 PM)
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
OOPS!!! Invalid deadline command or date. Format: deadline <description> /by <yyyy-MM-dd HHmm>, e.g., deadline return book /by 2019-12-02 1800
============================================================
============================================================
OOPS!!! Invalid event command or dates. Format: event <description> /from <yyyy-MM-dd HHmm> /to <yyyy-MM-dd HHmm>, e.g., event party /from 2019-12-02 1800 /to 2019-12-02 2100
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

- **Aim:** Verify that rejected commands, including dates in an invalid format such as month 13, leave the task list unchanged while valid commands before and after them still work.
- **Command:** `rm -rf data && java -cp build/classes Echo`
- **Inputs:**
```text
todo
todo read book
mark 1
deadline submit report
deadline bad /by 2019-13-45 9999
event team sync /from 2019-08-06 1000 /to 2019-08-06 1100
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
OOPS!!! Invalid deadline command or date. Format: deadline <description> /by <yyyy-MM-dd HHmm>, e.g., deadline return book /by 2019-12-02 1800
============================================================
============================================================
OOPS!!! Invalid deadline command or date. Format: deadline <description> /by <yyyy-MM-dd HHmm>, e.g., deadline return book /by 2019-12-02 1800
============================================================
============================================================
Got it. I've added this task:
[E][ ] team sync (from: Aug 06 2019, 10:00 AM to: Aug 06 2019, 11:00 AM)
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
2.[E][ ] team sync (from: Aug 06 2019, 10:00 AM to: Aug 06 2019, 11:00 AM)
============================================================
============================================================
Bye!
============================================================
```

## Test case: malformed task fields do not consume task numbers

- **Aim:** Verify that unparseable or incomplete deadline and event fields, plus a command with an unrecognised keyword, do not affect later valid tasks or their numbers.
- **Command:** `rm -rf data && java -cp build/classes Echo`
- **Inputs:**
```text
deadline plan /by someday
todo practice Java
event workshop /from 2019-10-01 0900 /to
deadline pay bill /by 2019-09-06 1200
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
OOPS!!! Invalid deadline command or date. Format: deadline <description> /by <yyyy-MM-dd HHmm>, e.g., deadline return book /by 2019-12-02 1800
============================================================
============================================================
Got it. I've added this task:
[T][ ] practice Java
Now you have 1 tasks in the list.
============================================================
============================================================
OOPS!!! Invalid event command or dates. Format: event <description> /from <yyyy-MM-dd HHmm> /to <yyyy-MM-dd HHmm>, e.g., event party /from 2019-12-02 1800 /to 2019-12-02 2100
============================================================
============================================================
Got it. I've added this task:
[D][ ] pay bill (by: Sep 06 2019, 12:00 PM)
Now you have 2 tasks in the list.
============================================================
============================================================
Nice! I've marked this task as done:
[D][X] pay bill (by: Sep 06 2019, 12:00 PM)
============================================================
============================================================
OOPS!!! Unknown command. I'm sorry, but I don't know what that means :(
============================================================
============================================================
Here are the tasks in your list:
1.[T][ ] practice Java
2.[D][X] pay bill (by: Sep 06 2019, 12:00 PM)
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
deadline return book /by 2019-06-06 1800
event project meeting /from 2019-08-06 1400 /to 2019-08-06 1600
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
[D][ ] return book (by: Jun 06 2019, 6:00 PM)
Now you have 2 tasks in the list.
============================================================
============================================================
Got it. I've added this task:
[E][ ] project meeting (from: Aug 06 2019, 2:00 PM to: Aug 06 2019, 4:00 PM)
Now you have 3 tasks in the list.
============================================================
============================================================
Got it. I've added this task:
[T][ ] borrow book
Now you have 4 tasks in the list.
============================================================
============================================================
Noted. I've removed this task:
[E][ ] project meeting (from: Aug 06 2019, 2:00 PM to: Aug 06 2019, 4:00 PM)
Now you have 3 tasks in the list.
============================================================
============================================================
Here are the tasks in your list:
1.[T][ ] read book
2.[D][ ] return book (by: Jun 06 2019, 6:00 PM)
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

## Test case: date values are validated and formatted

- **Aim:** Verify that Level-8 date handling rejects unsupported formats (the slash style `2/12/2019 1800`, non-date words, and dates without a time) for both deadlines and events, keeps the list unchanged, and shows accepted dates in a friendlier format than they were entered.
- **Command:** `rm -rf data && java -cp build/classes Echo`
- **Inputs:**
```text
deadline return book /by 2/12/2019 1800
deadline return book /by banana
deadline return book /by 2019-12-02
event party /from 2019-12-02 1800 /to apple
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
OOPS!!! Invalid deadline command or date. Format: deadline <description> /by <yyyy-MM-dd HHmm>, e.g., deadline return book /by 2019-12-02 1800
============================================================
============================================================
OOPS!!! Invalid deadline command or date. Format: deadline <description> /by <yyyy-MM-dd HHmm>, e.g., deadline return book /by 2019-12-02 1800
============================================================
============================================================
OOPS!!! Invalid deadline command or date. Format: deadline <description> /by <yyyy-MM-dd HHmm>, e.g., deadline return book /by 2019-12-02 1800
============================================================
============================================================
OOPS!!! Invalid event command or dates. Format: event <description> /from <yyyy-MM-dd HHmm> /to <yyyy-MM-dd HHmm>, e.g., event party /from 2019-12-02 1800 /to 2019-12-02 2100
============================================================
============================================================
Here are the tasks in your list:

============================================================
============================================================
Bye!
============================================================
```

## Test case: corrupted save lines report storage errors on load

- **Aim:** Verify that a saved line containing an unparsable date is reported at startup as a storage error instead of crashing, after which the session continues with the remaining valid tasks discarded.
- **Command:** `mkdir -p data && printf 'T | 0 | read book\nD | 1 | return book | Sunday\n' > data/echo.txt && java -cp build/classes Echo`
- **Inputs:**
```text
list
bye
```
- **Expected output:**
```text
============================================================
OOPS!!! Storage exception occurred. Save file is corrupted. I could not understand this line: D | 1 | return book | Sunday
============================================================
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
Here are the tasks in your list:

============================================================
============================================================
Bye!
============================================================
```

