/**
 * Shows every task currently in the list.
 */
public class ListCommand extends Command {
    /**
     * Prints the numbered listing of all tasks. Nothing is saved because
     * listing does not change any task.
     */
    @Override
    public void execute(TaskList taskList, Ui ui, Storage storage) {
        ui.echo(taskList.asListText());
    }
}
