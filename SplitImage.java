import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class SplitImage {
  public static void main(String[] args) {
    try {
      // Load the combined image
      BufferedImage combinedImage = ImageIO.read(new File("src/resources/anime2.png"));

      // Define the number of frames
      int totalFrames = 4; // Change this to the number of frames you need
      int frameWidth = combinedImage.getWidth() / totalFrames;
      int frameHeight = combinedImage.getHeight();

      // Split and save each frame
      for (int i = 0; i < totalFrames; i++) {
        BufferedImage frame = combinedImage.getSubimage(i * frameWidth, 0, frameWidth, frameHeight);
        File outputfile = new File("src/resources/flash" + (i + 1) + ".png");
        ImageIO.write(frame, "png", outputfile);
      }

      System.out.println("Frames split successfully!");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
