import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class MLPTranslation {
    private static MLPTranslation instance;
    private final Map<String, Map<String, String>> translations = new HashMap<>();
    private final List<String> availableLanguages = new ArrayList<>();
    private String currentLanguage = "English";

    private MLPTranslation() {
        loadTranslations();
    }

    public static MLPTranslation getInstance() {
        if (instance == null) {
            instance = new MLPTranslation();
        }
        return instance;
    }

    private void loadTranslations() {
        try (BufferedReader reader = new BufferedReader(new FileReader("java-fx/src/main/resources/translation.csv", StandardCharsets.UTF_8))) {
            String line = reader.readLine();
            if (line != null) {
                String[] languages = line.split(",");
                for (String s : languages) {
                    String language = s.trim();
                    availableLanguages.add(language);
                    translations.put(language, new HashMap<>());
                }

                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length >= languages.length) {
                        String key = parts[0].trim();
                        for (int i = 0; i < languages.length; i++) {
                            String language = languages[i].trim();
                            String translation = parts[i].trim();
                            translations.get(language).put(key, translation);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String translate(String text) {
        if (currentLanguage.equals("English")) {
            return text;
        }

        Map<String, String> languageTranslations = translations.get(currentLanguage);
        if (languageTranslations != null && languageTranslations.containsKey(text)) {
            return languageTranslations.get(text);
        }
        return text;
    }

    public List<String> getAvailableLanguages() {
        return availableLanguages;
    }

    public void setCurrentLanguage(String language) {
        if (availableLanguages.contains(language)) {
            this.currentLanguage = language;
        }
    }
    public String getCurrentLanguage() {
        return currentLanguage;
    }
}