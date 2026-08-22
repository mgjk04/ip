import java.util.ArrayList;
import java.util.List;

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
     * @param t task to add
     */
    public void add(Task t) {
        tasks.add(t);
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
     * @return live view of the tasks in list order
     */
    public List<Task> getAll() {
        return tasks;
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
        Task removed = getTask(index);
        tasks.remove(index);
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
        for (int i = 1; i <= tasks.size(); ++i) {
            listTxt.append(i).append(".").append(tasks.get(i - 1).toString());
            if (i != tasks.size()) listTxt.append("\n");
        }
        return listTxt.toString();
    }
}
