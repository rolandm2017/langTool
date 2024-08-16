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

class PhotoDaoTest {

    @InjectMocks
    private PhotoDao photoDao;

    @Mock
    private PhotoRepository photoRepository;

    @Mock
    private CollectionRepository collectionRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testWriteNewPhotoToDb() {
        String photoFileName = "test.jpg";
        LocalDateTime now = LocalDateTime.now();
        Long collectionId = 1L;

        CollectionEntity collection = new CollectionEntity();
        collection.setId(collectionId);

        when(collectionRepository.findById(collectionId)).thenReturn(Optional.of(collection));
        when(photoRepository.save(any(PhotoEntity.class))).thenAnswer(i -> i.getArguments()[0]);

        PhotoEntity result = photoDao.writeNewPhotoToDb(photoFileName, now, collectionId);

        assertNotNull(result);
        assertEquals(photoFileName, result.getFilePath());
        assertEquals(collection, result.getCollection());
        verify(photoRepository, times(1)).save(any(PhotoEntity.class));
    }

    @Test
    void testFindAllPhotos() {
        PhotoEntity photo1 = new PhotoEntity();
        PhotoEntity photo2 = new PhotoEntity();
        when(photoRepository.findAll()).thenReturn(Arrays.asList(photo1, photo2));

        List<PhotoEntity> result = photoDao.findAllPhotos();

        assertEquals(2, result.size());
        verify(photoRepository, times(1)).findAll();
    }
}