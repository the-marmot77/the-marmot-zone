import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferInt;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

// code is supposed to loop, parsing through pixels of the screen captures and clicking when it runs into a group of pixel that matches the color hash value

public class Controller extends Thread {

    private Rectangle rectangle;

    public Controller(int x, int y, int width, int height) {
        rectangle = new Rectangle(x, y, width, height);
    }

    @Override
    public void run() {
        robotTime();
    }

    public void robotTime() {
        try {
            Robot robot = new Robot();
            BufferedImage image = robot.createScreenCapture(rectangle); //takes a screen capture of the size of the rectangle object

            ImageIO.write(image, "png", new File("C:\\Users\\marmot77\\TestBot\\screenshot.png")); //saves said screen capture to destination for testing purposes

            while(true) {  //loop is always true (infinite), will be changed to take set amt of time - this uses image methods to get a raster (help the computer analyze the image capture)
                BufferedImage img = robot.createScreenCapture(rectangle);
                WritableRaster r = img.getRaster();
                DataBuffer db = r.getDataBuffer();
                DataBufferInt dbi = (DataBufferInt)db;
                int [] data=dbi.getData();

                for (int x_scale = 0; x_scale < rectangle.width; x_scale += 30) { //loop that goes through each pixel of the screen capture by increments of 30
                    for (int y_scale = 0; y_scale < rectangle.height; y_scale += 30) {
                        int rgb = data[x_scale + rectangle.width * y_scale] ; //using the data list to parse all colors found on capture
                            
                            if (rgb == -13882140 //loop enters if the color code is found during parsing
                                //insert other color hash codes here
                                ) {
                                    
                                    robot.mouseMove(rectangle.x + x_scale, rectangle.y + y_scale);

                                    robot.mousePress(InputEvent.BUTTON2_DOWN_MASK);
                                    robot.mouseRelease(InputEvent.BUTTON2_DOWN_MASK);
                                    
                                    robot.delay(1000); // 10 sec delay for testing because with no delay stopping the program that's improperly running is less manageable

                            }
                    }
                }
            }

        } catch (AWTException | IOException e) {

            e.printStackTrace();
            System.out.println("Error has occurred.");
            
        }
    }

    public static void main(String[] args) {
        new Controller(0, 0, 1920, 1080).start();
    }
}
