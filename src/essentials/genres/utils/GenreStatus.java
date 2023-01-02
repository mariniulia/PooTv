package essentials.genres.utils;

public final class GenreStatus {
    private Integer numLikes = 0;
    private String genreName;

    public GenreStatus(final String genreName) {
        this.genreName = genreName;
    }

    public Integer getNumLikes() {
        return numLikes;
    }

    public void setNumLikes(final Integer numLikes) {
        this.numLikes = numLikes;
    }

    public String getGenreName() {
        return genreName;
    }
}
