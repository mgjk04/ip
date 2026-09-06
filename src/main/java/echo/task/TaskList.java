package echo.task;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import echo.exception.InvalidTaskNumberException;
import echo.storage.Storage;



/**
 * Owns the collection of {@link Task} objects and provides operations on it,
 * such as adding, deleting, and listing tasks. It does not know about saving
 * or user interaction; callers handle those concerns.
 */
public class TaskList {
    private final ArrayList<Task> tasks;

    /**
     * Creates an empty task list.
     */
    public TaskList() {
        tasks = new ArrayList<>();
    }

    /**
     * Appends a task to the end of the list.
     *
     * @param task task to add
     */
    public void add(Task task) {
        assert task != null : "A task list must not contain null tasks.";
        tasks.add(task);
    }

    /**
     * Appends several tasks, e.g. ones loaded from the save file.
     *
     * @param newTasks tasks to append in their given order
     */
    public void addAll(List<Task> newTasks) {
        tasks.addAll(newTasks);
    }

    /**
     * Returns the number of tasks currently in the list.
     *
     * @return task count
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Returns all tasks so they can be processed elsewhere,
     * e.g. saved to disk by {@link Storage}.
     *
     * @return unmodifiable view of the tasks in list order
     */
    public List<Task> getAll() {
        return Collections.unmodifiableList(tasks);
    }

    /**
     * Returns the task at the given zero-based index after checking it
     * against the current list bounds. Parser cannot do this check because
     * it does not own the list.
     *
     * @param index zero-based index of the task
     * @return referenced task
     * @throws InvalidTaskNumberException when the index is not in the list
     */
    public Task getTask(int index) throws InvalidTaskNumberException {
        if (index < 0 || index >= tasks.size()) {
            throw new InvalidTaskNumberException();
        }
        return tasks.get(index);
    }

    /**
     * Removes the task at the given zero-based index.
     *
     * @param index zero-based index of the task to remove
     * @return the removed task
     * @throws InvalidTaskNumberException when the index is not in the list
     */
    public Task delete(int index) throws InvalidTaskNumberException {
        int originalSize = tasks.size();
        Task removed = getTask(index);
        tasks.remove(index);
        assert tasks.size() == originalSize - 1 : "Deleting a valid task must reduce the task count by one.";
        return removed;
    }

    /**
     * Formats every task as a numbered list, e.g. for the {@code list}
     * command output.
     *
     * @return multi-line text of all tasks prefixed with one-based numbers
     */
    public String asListText() {
        StringBuilder listTxt = new StringBuilder("Here are the tasks in your list:\n");
        for (int i = 0; i < tasks.size(); i++) {
            if (i > 0) {
                listTxt.append("\n");
            }
            appendNumberedTask(listTxt, i);
        }
        return listTxt.toString();
    }

    /**
     * Formats every task whose description contains the searchText as a
     * numbered list, e.g. for the {@code find} command output. Matches keep
     * their full-list numbers so they can still be used with commands such
     * as {@code mark} and {@code delete}.
     *
     * @param searchText text to search for in task descriptions
     * @return multi-line text of matching tasks prefixed with their one-based
     *         full-list numbers; header only when nothing matches
     */
    public String searchListText(String searchText) {
        return "Here are the matching tasks in your list:\n"
                + IntStream.range(0, tasks.size())
                .filter(index -> tasks.get(index).getDescription().contains(searchText))
                .mapToObj(index -> (index + 1) + "." + tasks.get(index))
                .collect(Collectors.joining("\n"));
    }

    /**
     * Formats global and current-week completion statistics.
     *
     * @param currentTime local date-time at which the statistics are requested
     * @return formatted statistics text
     */
    public String statisticsText(LocalDateTime currentTime) {
        LocalDateTime weekStart = currentTime.toLocalDate()
                .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
                .atStartOfDay();
        int totalCompleted = 0;
        int todoCompleted = 0;
        int deadlineCompleted = 0;
        int eventCompleted = 0;
        int weekCompleted = 0;
        int weekTodoCompleted = 0;
        int weekDeadlineCompleted = 0;
        int weekEventCompleted = 0;

        for (Task task : tasks) {
            if (!task.isDone()) {
                continue;
            }
            totalCompleted++;
            if (task instanceof Todo) {
                todoCompleted++;
            } else if (task instanceof Deadline) {
                deadlineCompleted++;
            } else if (task instanceof Event) {
                eventCompleted++;
            }

            if (isCompletedThisWeek(task.getCompletedAt(), weekStart, currentTime)) {
                weekCompleted++;
                if (task instanceof Todo) {
                    weekTodoCompleted++;
                } else if (task instanceof Deadline) {
                    weekDeadlineCompleted++;
                } else if (task instanceof Event) {
                    weekEventCompleted++;
                }
            }
        }

        return "Statistics:\n\n"
                + "Global:\n"
                + "Total Tasks: " + tasks.size() + "\n"
                + "Total Completed: " + totalCompleted + "\n"
                + "Total Incomplete: " + (tasks.size() - totalCompleted) + "\n"
                + "Todos Completed: " + todoCompleted + "\n"
                + "Events Completed: " + eventCompleted + "\n"
                + "Deadlines Completed: " + deadlineCompleted + "\n\n"
                + "Week:\n"
                + "Completed: " + weekCompleted + "\n"
                + "Todos Completed: " + weekTodoCompleted + "\n"
                + "Events Completed: " + weekEventCompleted + "\n"
                + "Deadlines Completed: " + weekDeadlineCompleted;
    }

    private boolean isCompletedThisWeek(LocalDateTime completedAt, LocalDateTime weekStart,
                                        LocalDateTime currentTime) {
        return !completedAt.isBefore(weekStart) && !completedAt.isAfter(currentTime);
    }

    private void appendNumberedTask(StringBuilder listTxt, int taskIndex) {
        listTxt.append(taskIndex + 1).append(".").append(tasks.get(taskIndex));
    }
}
