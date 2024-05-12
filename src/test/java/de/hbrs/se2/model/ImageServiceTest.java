package de.hbrs.se2.model;

import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.server.StreamResource;
import de.hbrs.se2.control.Image.ImageService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

public class ImageServiceTest {

    @InjectMocks
    private ImageService imageService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testByteToImage() {
        // Testfall: Byte-Array ist null
        Image result = imageService.byteToImage(null);
        Image placeholder = new Image("https://cdn.pixabay.com/photo/2017/11/10/05/24/add-2935429_1280.png", "Placeholder Image");
        assertEquals(placeholder,result);
//        assertEquals("Placeholder Image", result.getElement().getAttribute("alt"));

        // Testfall: Byte-Array ist nicht null
        byte[] testBytes = new byte[]{1, 2, 3};
        StreamResource streamResource = new StreamResource("user", () -> new ByteArrayInputStream(testBytes));
        streamResource.setContentType("image/png");
        Image expectedResult = new Image(streamResource, "generated -image");

        result = imageService.byteToImage(testBytes);
        assertEquals(expectedResult.getElement().getAttribute("src"), result.getElement().getAttribute("src"));
        assertEquals(expectedResult.getElement().getAttribute("alt"), result.getElement().getAttribute("alt"));
    }

    @Test
    public void testInputStreamToByte() throws IOException {
        // Testfall: InputStream enthält Daten
        byte[] expectedBytes = new byte[]{1, 2, 3};
        InputStream testInputStream = new ByteArrayInputStream(expectedBytes);
        byte[] result = imageService.inputStreamToByte(testInputStream);
        assertArrayEquals(expectedBytes, result);

        // Testfall: InputStream ist null
        byte[] resultNullStream = imageService.inputStreamToByte(null);
        assertNull(resultNullStream);

        // Testfall: IOException wird geworfen
        InputStream mockInputStream = mock(InputStream.class);
        when(mockInputStream.readAllBytes()).thenThrow(new IOException("Test IOException"));
        try {
            imageService.inputStreamToByte(mockInputStream);
        } catch (RuntimeException e) {
            assertEquals("Test IOException", e.getCause().getMessage());
        }
    }
}
