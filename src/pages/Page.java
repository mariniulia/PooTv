package pages;

import java.util.ArrayList;
import java.util.Arrays;

public final class Page {
    private String name;
    private ArrayList<String> features = new ArrayList<>();

    public Page(final String name) {
        this.name = name;
    }

    public Page(final String name, final String... features) {
        this.name = name;
        this.features.addAll(Arrays.stream(features).toList());
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public ArrayList<String> getFeatures() {
        return features;
    }

    public void setFeatures(final ArrayList<String> features) {
        this.features = features;
    }
}
