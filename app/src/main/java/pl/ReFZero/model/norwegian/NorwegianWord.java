package pl.ReFZero.model.norwegian;

public class NorwegianWord  {
    private Integer id;
    private String polish;
    private String norwegian;

    public NorwegianWord() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPolish() {
        return polish;
    }

    public void setPolish(String polish) {
        this.polish = polish;
    }

    public String getNorwegian() {
        return norwegian;
    }

    public void setNorwegian(String norwegian) {
        this.norwegian = norwegian;
    }
}
