package caseyellow.client.infrastructre.image.comparison;
import caseyellow.client.exceptions.InternalFailureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

@Component
public class ImageComparison implements ImageComparisonService {

	@Value("${comparision-threshold}")
	private final double COMPARISION_THRESHOLD = 0.05;
	private ImageChecker imageChecker;

	public ImageComparison() {
		imageChecker = new ImageChecker();
	}

	@Override
	public boolean compare(String imgPath, String subImgPath) {

		try {
			BufferedImage image = ImageIO.read(new File(imgPath));
			BufferedImage subImage = ImageIO.read(new File(subImgPath));

			if (image.getWidth() + image.getHeight() >= subImage.getWidth() + subImage.getHeight()) {
				imageChecker.setOne(image);
				imageChecker.setTwo(subImage);
			} else {
				throw new InternalFailureException("Sub image is larger then main image");
			}

			return imageChecker.compareImages() < COMPARISION_THRESHOLD;

		} catch (Exception e) {
			throw new InternalFailureException("Failure to compare images, " + e.getMessage(), e);
		}
	}
}
