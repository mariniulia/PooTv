package fileio;

public final class ActionsInput {
    private String type;
    private String page;
    private String movie;
    private String feature;
    private CredentialsInput credentials;
    private String startsWith;
    private Filter filters;
    private Integer count;
    private Integer rate;

    private MovieInput addedMovie;

    private String deletedMovie;

    private String subscribedGenre;


    public ActionsInput() {
    }

    public String getType() {
        return type;
    }

    public void setType(final String type) {
        this.type = type;
    }

    public String getPage() {
        return page;
    }

    public void setPage(final String page) {
        this.page = page;
    }

    public String getMovie() {
        return movie;
    }

    public void setMovie(final String movie) {
        this.movie = movie;
    }

    public String getFeature() {
        return feature;
    }

    public void setFeature(final String feature) {
        this.feature = feature;
    }

    public CredentialsInput getCredentials() {
        return credentials;
    }

    public void setCredentials(final CredentialsInput credentials) {
        this.credentials = credentials;
    }

    public String getStartsWith() {
        return startsWith;
    }

    public Filter getFilters() {
        return filters;
    }

    public Integer getCount() {
        return count;
    }

    public Integer getRate() {
        return rate;
    }

    public MovieInput getAddedMovie() {
        return addedMovie;
    }

    public String getDeletedMovie() {
        return deletedMovie;
    }

    public String getSubscribedGenre() {
        return subscribedGenre;
    }

}
