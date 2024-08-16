import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WordDaoTest {

    @InjectMocks
    private WordDao wordDao;

    @Mock
    private WordRepository wordRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindWordByOrigin() {
        WordEntity word = new WordEntity();
        word.setOrigin("test");

        when(wordRepository.findByOrigin("test")).thenReturn(Optional.of(word));

        Optional<WordEntity> result = wordDao.findWordByOrigin("test");

        assertTrue(result.isPresent());
        assertEquals("test", result.get().getOrigin());
    }

    @Test
    void testFindAllByOrigins() {
        WordEntity word1 = new WordEntity();
        word1.setOrigin("test1");
        WordEntity word2 = new WordEntity();
        word2.setOrigin("test2");

        when(wordRepository.findAllByOrigins(Arrays.asList("test1", "test2")))
            .thenReturn(Arrays.asList(word1, word2));

        List<WordEntity> result = wordDao.findAllByOrigins(Arrays.asList("test1", "test2"));

        assertEquals(2, result.size());
        assertEquals("test1", result.get(0).getOrigin());
        assertEquals("test2", result.get(1).getOrigin());
    }

    @Test
    void testIncrementMentionsForWords() {
        WordEntity word1 = new WordEntity();
        word1.setOrigin("test1");
        WordEntity word2 = new WordEntity();
        word2.setOrigin("test2");

        wordDao.incrementMentionsForWords(Arrays.asList(word1, word2));

        verify(wordRepository, times(1)).incrementMentionsForWords(new String[]{"test1", "test2"});
    }

    @Test
    void testSaveAllWords() {
        WordEntity word1 = new WordEntity();
        WordEntity word2 = new WordEntity();

        when(wordRepository.saveAll(any())).thenReturn(Arrays.asList(word1, word2));

        List<WordEntity> result = wordDao.saveAllWords(new WordEntity[]{word1, word2});

        assertEquals(2, result.size());
        verify(wordRepository, times(1)).saveAll(any());
    }
}