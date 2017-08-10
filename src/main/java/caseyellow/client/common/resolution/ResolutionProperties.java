package caseyellow.client.common.resolution;

/**
 * Created by Dan on 8/10/2017.
 */
public class ResolutionProperties {

    private Point center;
    private Coordinates coordinates;

    public ResolutionProperties() {
    }

    public ResolutionProperties(Point center, Coordinates coordinates) {
        this.center = center;
        this.coordinates = coordinates;
    }

    public Point getCenter() {
        return center;
    }

    public void setCenter(Point center) {
        this.center = center;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }


    public int getX() {
        return coordinates.getX();
    }

    public int getY() {
        return coordinates.getY();
    }

    public int getH() {
        return coordinates.getH();
    }

    public int getW() {
        return coordinates.getW();
    }

}
