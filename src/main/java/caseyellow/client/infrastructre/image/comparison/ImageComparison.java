package caseyellow.client.infrastructre.image.comparison;
import caseyellow.client.exceptions.InternalFailureException;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


public class ImageComparison {

	public static boolean compare(String imgPath, String subImgPath) {
		ImageChecker imageChecker = new ImageChecker();

		try {
			BufferedImage image = ImageIO.read(new File(imgPath));
			BufferedImage subImage = ImageIO.read(new File(subImgPath));

			if (image.getWidth() + image.getHeight() >= subImage.getWidth() + subImage.getHeight()) {
				imageChecker.setOne(image);
				imageChecker.setTwo(subImage);
			} else {
				throw new InternalFailureException("Sub image is larger then main image");
			}

			return imageChecker.compareImages();

		} catch (Exception e) {
			throw new InternalFailureException("Failure to compare images, " + e.getMessage(), e);
		}
	}
}
