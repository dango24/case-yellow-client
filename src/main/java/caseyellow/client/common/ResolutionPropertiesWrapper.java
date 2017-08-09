package caseyellow.client.common;


import org.sikuli.script.Match;

import java.awt.Point;

/**
 * Created by Dan on 7/22/2017.
 */

public class ResolutionPropertiesWrapper {

    private int centralized;
    private ResolutionProperties startButtonResolutionProperties;
    private ResolutionProperties finishTestResolutionProperties;

    public ResolutionPropertiesWrapper() {
    }

    public int getCentralized() {
        return centralized;
    }

    public void setCentralized(int centralized) {
        this.centralized = centralized;
    }

    public ResolutionProperties getStartButtonResolutionProperties() {
        return startButtonResolutionProperties;
    }

    public void setStartButtonResolutionProperties(ResolutionProperties startButtonResolutionProperties) {
        this.startButtonResolutionProperties = startButtonResolutionProperties;
    }

    public ResolutionProperties getFinishTestResolutionProperties() {
        return finishTestResolutionProperties;
    }

    public void setFinishTestResolutionProperties(ResolutionProperties finishTestResolutionProperties) {
        this.finishTestResolutionProperties = finishTestResolutionProperties;
    }

    public void createStartTestResolutionProperties(Match match) {
        ResolutionProperties resolutionProperties = new ResolutionProperties(new Point(match.getTarget().getX(), match.getTarget().getY()),
                                                                             new Coordinates(match.getX(), match.getY(), match.getH(), match.getW()));

        setStartButtonResolutionProperties(resolutionProperties);
    }

    public void createFinishTestResolutionProperties(Match match) {
        ResolutionProperties resolutionProperties = new ResolutionProperties(new Point(match.getTarget().getX(), match.getTarget().getY()),
                new Coordinates(match.getX(), match.getY(), match.getH(), match.getW()));

        setFinishTestResolutionProperties(resolutionProperties);
    }
}

class ResolutionProperties {

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
}
