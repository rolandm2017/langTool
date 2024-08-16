import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CollectionDaoTest {

    @InjectMocks
    private CollectionDao collectionDao;

    @Mock
    private CollectionRepository collectionRepository;

    @Mock
    private PhotoRepository photoRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testWriteNewCollectionOrUpdate() {
        PhotoEntity photo = new PhotoEntity();
        photo.setFilePath("test.jpg");
        LocalDateTime now = LocalDateTime.now();

        when(collectionRepository.findById(1L)).thenReturn(Optional.empty());

        collectionDao.writeNewCollectionOrUpdate(1L, now, photo);

        verify(collectionRepository, times(1)).save(any(CollectionEntity.class));
        verify(photoRepository, times(1)).save(photo);
    }

    @Test
    void testFindAllCollections() {
        CollectionEntity collection1 = new CollectionEntity();
        CollectionEntity collection2 = new CollectionEntity();
        when(collectionRepository.findAll()).thenReturn(Arrays.asList(collection1, collection2));

        List<CollectionEntity> result = collectionDao.findAllCollections();

        assertEquals(2, result.size());
        verify(collectionRepository, times(1)).findAll();
    }

    @Test
    void testUpdateCollectionLabel() {
        CollectionEntity collection = new CollectionEntity();
        collection.setId(1L);
        when(collectionRepository.findById(1L)).thenReturn(Optional.of(collection));

        boolean result = collectionDao.updateCollectionLabel(1L, "New Label");

        assertTrue(result);
        assertEquals("New Label", collection.getLabel());
        verify(collectionRepository, times(1)).save(collection);
    }
}