package co.edu.ustavillavicencio.impostor.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@Component
public class WordBankService {

    private Map<String, List<String>> wordsByCategory = new HashMap<>();

    @PostConstruct
    public void init() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ClassPathResource resource = new ClassPathResource("data/palabras.json");
        try (InputStream is = resource.getInputStream()) {
            wordsByCategory = mapper.readValue(is, new TypeReference<>() {});
        }
    }

    public Set<String> getAvailableCategories() {
        return wordsByCategory.keySet();
    }

    public boolean categoryExists(String category) {
        return wordsByCategory.containsKey(category.toUpperCase());
    }

    public String pickRandomWord(String category) {
        List<String> words = wordsByCategory.get(category.toUpperCase());
        if (words == null || words.isEmpty()) {
            throw new IllegalStateException("No hay palabras para la categoria: " + category);
        }
        int idx = ThreadLocalRandom.current().nextInt(words.size());
        return words.get(idx);
    }

    /**
     * Escoge una palabra aleatoria de una categoria DIFERENTE a la indicada.
     * Se usa para asignarle una palabra diferente al impostor.
     */
    public String pickRandomWordFromDifferentCategory(String excludeCategory) {
        List<String> otherCategories = wordsByCategory.keySet().stream()
                .filter(cat -> !cat.equalsIgnoreCase(excludeCategory))
                .toList();

        if (otherCategories.isEmpty()) {
            return null; // si solo hay una categoria, impostor sin palabra
        }

        String randomCategory = otherCategories.get(
                ThreadLocalRandom.current().nextInt(otherCategories.size()));

        return pickRandomWord(randomCategory);
    }
}
