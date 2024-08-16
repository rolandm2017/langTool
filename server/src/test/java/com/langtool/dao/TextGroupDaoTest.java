import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TextGroupDaoTest {

    @InjectMocks
    private TextGroupDao textGroupDao;

    @Mock
    private TextGroupRepository textGroupRepository;

    @Mock
    private WordRepository wordRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddWordToTextGroup() {
        TextGroupEntity textGroup = new TextGroupEntity();
        textGroup.setId(1L);

        when(textGroupRepository.findById(1L)).thenReturn(Optional.of(textGroup));
        when(textGroupRepository.save(any(TextGroupEntity.class))).thenAnswer(i -> i.getArguments()[0]);

        textGroupDao.addWordToTextGroup(1L, "test");

        assertEquals(1, textGroup.getWords().size());
        verify(textGroupRepository, times(1)).save(textGroup);
    }

    @Test
    void testWriteNewTextGroupToDb() {
        PhotoEntity photo = new PhotoEntity();
        WordEntity word1 = new WordEntity();
        WordEntity word2 = new WordEntity();

        when(wordRepository.save(any(WordEntity.class))).thenAnswer(i -> i.getArguments()[0]);
        when(textGroupRepository.save(any(TextGroupEntity.class))).thenAnswer(i -> i.getArguments()[0]);

        textGroupDao.writeNewTextGroupToDb(1L, photo, Arrays.asList(word1, word2));

        verify(wordRepository, times(2)).save(any(WordEntity.class));
        verify(textGroupRepository, times(1)).save(any(TextGroupEntity.class));
    }

    @Test
    void testFindAllTextGroups() {
        TextGroupEntity textGroup1 = new TextGroupEntity();
        TextGroupEntity textGroup2 = new TextGroupEntity();
        when(textGroupRepository.findAll()).thenReturn(Arrays.asList(textGroup1, textGroup2));

        List<TextGroupEntity> result = textGroupDao.findAllTextGroups();

        assertEquals(2, result.size());
        verify(textGroupRepository, times(1)).findAll();
    }
}