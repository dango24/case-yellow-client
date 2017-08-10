package caseyellow.client.infrastructre.image.comparison;

public interface ImageComparisonService {
    boolean compare(String imgPath, String subImgPath, double comparisionThreshold);
    double getComparisionResult();
}
