/**
 * Updates the completion status of the task at a zero-based index.
 * Marking done and marking not done share this class because they differ
 * only in the status written and the wording of the confirmation.
 */
public class MarkCommand extends Command {
    private final int index;
    private final boolean done;

    /**
     * Creates a command that updates one task's completion status.
     *
     * @param index zero-based index of the task to update
     * @param done true to mark as done, false to mark as not done
     */
    public MarkCommand(int index, boolean done) {
        this.index = index;
        this.done = done;
    }

    /**
     * Updates the task's status, saves the list, and confirms the change.
     */
    @Override
    public void execute(TaskList taskList, Ui ui, Storage storage) throws EchoException {
        Task task = taskList.getTask(index);
        if (done) {
            task.markDone();
            ui.echo("Nice! I've marked this task as done:\n" + task.toString());
        } else {
            task.markUnDone();
            ui.echo("OK, I've marked this task as not done yet:\n" + task.toString());
        }
        storage.save(taskList.getAll());
    }
}
