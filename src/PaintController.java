import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

public class PaintController {
    @FXML
    private Canvas canvas;

    @FXML
    private ColorPicker colorPicker;

    @FXML
    private TextField penSize;

    @FXML
    private CheckBox eraser;

    @FXML
    private void initialize() {
        canvas.getGraphicsContext2D().setFill(Color.WHITE);
        canvas.getGraphicsContext2D().fillRect(0, 0, Constants.getWidth(), Constants.getHeight());
        canvas.setOnMousePressed(e -> {
            double size = Double.parseDouble(penSize.getText());
            if (!eraser.isSelected()) {
                canvas.getGraphicsContext2D().setLineWidth(size);
                canvas.getGraphicsContext2D().setStroke(colorPicker.getValue());
                canvas.getGraphicsContext2D().setLineCap(StrokeLineCap.ROUND);
                canvas.getGraphicsContext2D().setLineJoin(StrokeLineJoin.ROUND);
                canvas.getGraphicsContext2D().beginPath();
                canvas.getGraphicsContext2D().lineTo(e.getX(), e.getY());
            } else {
                canvas.getGraphicsContext2D().setStroke(Color.WHITE);
                canvas.getGraphicsContext2D().fillRect(e.getX() - size / 2, e.getY() - size / 2, size, size);
            }
        });

        canvas.setOnMouseDragged(e -> {
            if (!eraser.isSelected()) {
                canvas.getGraphicsContext2D().lineTo(e.getX(), e.getY());
                canvas.getGraphicsContext2D().stroke();
            } else {
                double size = Double.parseDouble(penSize.getText());
                canvas.getGraphicsContext2D().fillRect(e.getX() - size / 2, e.getY() - size / 2, size, size);
            }
        });

        canvas.setOnMouseReleased(e -> {
            if (!eraser.isSelected()) {
                canvas.getGraphicsContext2D().lineTo(e.getX(), e.getY());
                canvas.getGraphicsContext2D().stroke();
                canvas.getGraphicsContext2D().closePath();
            }
        });
    }

    @FXML
    private void onOpen() {
        Stage stage = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open file");
        fileChooser.setInitialDirectory(new File("/."));
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Images", "*.jpg", "*.png"),
                new FileChooser.ExtensionFilter("jpg", "*.jpg"),
                new FileChooser.ExtensionFilter("png", "*.png"));
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            Image image = null;
            try {
                image = new Image(file.toURI().toURL().toString());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            //Just in case, I cleaned the canvas
            canvas.getGraphicsContext2D().setFill(Color.WHITE);
            canvas.getGraphicsContext2D().fillRect(0, 0, Constants.getWidth(), Constants.getHeight());
            canvas.getGraphicsContext2D().drawImage(image, 0, 0, Constants.getWidth(), Constants.getHeight());
        }
    }

    @FXML
    private void onSave() {
        Stage stage = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save file");
        fileChooser.setInitialDirectory(new File("/."));
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("jpg", "*.jpg"),
                new FileChooser.ExtensionFilter("png", "*.png"));
        File file = fileChooser.showSaveDialog(stage);
        if (file != null) {
            Image snapshot = canvas.snapshot(null, null);
            String fileExtension = fileChooser.getSelectedExtensionFilter().getDescription();
            try {
                ImageIO.write(SwingFXUtils.fromFXImage(snapshot, null), fileExtension,
                        new File(file.getPath() + "." + fileExtension));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void onExit() {
        Platform.exit();
    }
}