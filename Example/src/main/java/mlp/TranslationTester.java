package mlp;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class TranslationTester extends Application {

    private final MLPWordSmith translation = MLPWordSmith.getInstance();

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("MLPWordSmith Translator");
        translation.setFilePath("Example/src/main/resources/translation.csv");

        ComboBox<String> languageSelector = new ComboBox<>();
        languageSelector.getItems().addAll(translation.getAvailableLanguages());
        languageSelector.setValue(translation.getCurrentLanguage());

        TextField inputField = new TextField();
        inputField.setPromptText("Enter text to translate");

        Button translateButton = new Button("Translate");

        Label translatedLabel = new Label();

        translateButton.setOnAction(_ -> {
            String selectedLanguage = languageSelector.getValue();
            translation.setCurrentLanguage(selectedLanguage);
            String inputText = inputField.getText();
            String translated = translation.translate(inputText);
            translatedLabel.setText("Translation: " + translated);
        });

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.getChildren().addAll(languageSelector, inputField, translateButton, translatedLabel);

        Scene scene = new Scene(layout, 400, 200);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
