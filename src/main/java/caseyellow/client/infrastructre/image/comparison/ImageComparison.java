package caseyellow.client.infrastructre.image.comparison;
import caseyellow.client.exceptions.InternalFailureException;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


public class ImageComparison {

	public static boolean compare(String imgPath1, String imgPath2) {
		ImageChecker imageChecker = new ImageChecker();

		try {
			BufferedImage imageOne = ImageIO.read(new File(imgPath1));
			BufferedImage imageTwo = ImageIO.read(new File(imgPath2));

			if (imageOne.getWidth() + imageOne.getHeight() >= imageTwo.getWidth() + imageTwo.getHeight()) {
				imageChecker.setOne(imageOne);
				imageChecker.setTwo(imageTwo);
			} else {
				imageChecker.setOne(imageTwo);
				imageChecker.setTwo(imageOne);
			}

			return imageChecker.compareImages();

		} catch (Exception e) {
			throw new InternalFailureException("Failure to compare images, " + e.getMessage(), e);
		}
	}
}
