package de.hbrs.se2.control.Image;

import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.server.StreamResource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@Service
public class ImageService {
    public Image byteToImage(byte[] imageInByte) {
        if (imageInByte == null) {
            return new Image("https://cdn.pixabay.com/photo/2017/11/10/05/24/add-2935429_1280.png", "Placeholder Image");
        } else {
            StreamResource sr = new StreamResource("user", () -> new ByteArrayInputStream(imageInByte));
            sr.setContentType("image/png");//setting the content type of the StreamResource to "image/png". it is important for the browser to interpret the data correctly.
            return new Image(sr, "generated -image");
        }
    }

    public byte[] inputStreamToByte(InputStream inputStream) {
        byte[] toByte;
        try {
            toByte = inputStream.readAllBytes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return toByte;
    }

}
