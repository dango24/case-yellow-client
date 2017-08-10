package caseyellow.client.infrastructre.image.comparison;
import caseyellow.client.exceptions.InternalFailureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

@Component
public class ImageComparison implements ImageComparisonService {

	private ImageChecker imageChecker;
	private double comparisonResult;

	public ImageComparison() {
		imageChecker = new ImageChecker();
	}

	@Override
	public boolean compare(String imgPath, String subImgPath, double comparisionThreshold) {

		comparisionThreshold = comparisionThreshold == 0 ? 0.05 : comparisionThreshold;
		try {
			BufferedImage image = ImageIO.read(new File(imgPath));
			BufferedImage subImage = ImageIO.read(new File(subImgPath));

			if (image.getWidth() + image.getHeight() >= subImage.getWidth() + subImage.getHeight()) {
				imageChecker.setOne(image);
				imageChecker.setTwo(subImage);
			} else {
				throw new InternalFailureException("Sub image is larger then main image");
			}

			comparisonResult = imageChecker.compareImages();
			return comparisonResult < 0.1;

		} catch (Exception e) {
			throw new InternalFailureException("Failure to compare images, " + e.getMessage(), e);
		}
	}

	@Override
	public double getComparisionResult() {
		return comparisonResult;
	}
}
