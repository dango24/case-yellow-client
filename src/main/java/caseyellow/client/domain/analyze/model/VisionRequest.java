package caseyellow.client.domain.analyze.model;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static caseyellow.client.common.Utils.createImageBase64Encode;

public class VisionRequest {

    private Image image;
    private ImageContext imageContext;
    private List<Feature> features;

    public VisionRequest() {
        imageContext = ImageContext.createImageContextDefaultValues();
        features = Arrays.asList(Feature.createDefaultFeature());
    }

    public VisionRequest(String imgPath) throws IOException {
        this(imgPath, null);
    }

    public VisionRequest(String imgPath, String md5) throws IOException {
        this();
        image = new Image(new String(createImageBase64Encode(imgPath), "UTF-8"), md5);
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public ImageContext getImageContext() {
        return imageContext;
    }

    public void setImageContext(ImageContext imageContext) {
        this.imageContext = imageContext;
    }

    public List<Feature> getFeatures() {
        return features;
    }

    public void setFeatures(List<Feature> features) {
        this.features = features;
    }
}
