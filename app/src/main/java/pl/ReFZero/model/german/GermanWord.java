package pl.ReFZero.model.german;

public class GermanWord {
    private Integer id;
    private String polish;
    private String german;

    public GermanWord() {
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

    public String getGerman() {
        return german;
    }

    public void setGerman(String german) {
        this.german = german;
    }
}
