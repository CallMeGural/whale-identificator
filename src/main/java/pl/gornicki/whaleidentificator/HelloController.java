package pl.gornicki.whaleidentificator;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class HelloController {

    @FXML
    private Stage stage;

    @FXML
    private ImageView imageView;

    @FXML
    private Label outputLabel;
    @FXML
    private ProgressIndicator loadingIndicator;
    @FXML
    private VBox parentContainer;
    @FXML
    private Label selectFileLabel;

    @FXML
    protected void browseFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open File");
        FileChooser.ExtensionFilter imageFilter = new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg");
        fileChooser.getExtensionFilters().add(imageFilter);
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            selectFileLabel.setVisible(true);
            Image image = new Image(selectedFile.toURI().toString());
            imageView.setImage(image);
            imageView.fitWidthProperty().bind(parentContainer.widthProperty().multiply(0.8));
            imageView.fitHeightProperty().bind(parentContainer.heightProperty().multiply(0.8));

            imageView.setVisible(true);
            imageView.setManaged(true);
            loadingIndicator.setVisible(true);
            loadingIndicator.setManaged(true);
            new Thread(() -> {
                // Execute Python script and pass the file path as argument
                try {
                    Thread.sleep(2000);
                    ProcessBuilder processBuilder = new ProcessBuilder("python", "your_script.py", selectedFile.getAbsolutePath());
                    Process process = processBuilder.start();

                    // Read output from the Python script
                    BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                    StringBuilder output = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        output.append(line).append("\n");
                    }

                    // Display output on the screen
                    Platform.runLater(() -> outputLabel.setText(output.toString()));

                    // Wait for the process to complete
                    int exitCode = process.waitFor();
                    if (exitCode != 0) {
                        // Handle error
                        Platform.runLater(() -> outputLabel.setText("Python script returned non-zero exit code: " + exitCode));

                    }
                } catch (IOException | InterruptedException e) {
                    Platform.runLater(() -> outputLabel.setText(e.getMessage()));
                }
                loadingIndicator.setVisible(false);
                loadingIndicator.setManaged(false);
            }).start();
        }
    }
}