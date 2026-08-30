package echo.ui.components;

import echo.Echo;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
/**
 * Controller for the main GUI.
 */
public class MainWindow extends AnchorPane {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;
    @FXML
    private Button sendButton;

    private Echo echo;

    private Image userImage = new Image(this.getClass().getResourceAsStream("/images/DaUser.png"));
    private Image echoImage = new Image(this.getClass().getResourceAsStream("/images/DaEcho.png"));

    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
    }

    /** Injects the Echo instance and shows the greeting once it is set. */
    public void setEcho(Echo d) {
        echo = d;
        dialogContainer.getChildren().add(DialogBox.getEchoDialog(echo.start(), echoImage));
    }

    /**
     * Creates two dialog boxes, one echoing user input and the other containing Echo's reply and then appends them to
     * the dialog container. Clears the user input after processing.
     */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText();
        String response = echo.getResponse(input);
        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(input, userImage),
                DialogBox.getEchoDialog(response, echoImage)
        );
        userInput.clear();
        if (echo.isExit()) {
            Platform.exit();
        }
    }
}
