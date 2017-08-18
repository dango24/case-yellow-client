package caseyellow.client.domain.analyze.model;

public class WordIdentifier implements Comparable<WordIdentifier> {

    private String identifier;
    private int count; // -1 indicates there is no appearance in the given text.

    public WordIdentifier() {
    }

    public WordIdentifier(String identifier, int count) {
        this.identifier = identifier;
        this.count = count;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public int compareTo(WordIdentifier o) {
        return  identifier.compareTo(o.identifier);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WordIdentifier that = (WordIdentifier) o;

        return identifier != null ? identifier.equals(that.identifier) : that.identifier == null;
    }

    @Override
    public int hashCode() {
        return identifier != null ? identifier.hashCode() : 0;
    }
}
