package com.iamhoppy.hoppy;

/* Beer class
 */
public class Beer {
    private String name;
    private String type;
    private double ibu;
    private double abv;
    private double rating;
    private String imgUrl;
    private String brewery;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getIbu() {
        return ibu;
    }

    public void setIbu(double ibu) {
        this.ibu = ibu;
    }

    public double getAbv() {
        return abv;
    }

    public void setAbv(double abv) {
        this.abv = abv;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getBrewery() {
        return brewery;
    }

    public void setBrewery(String brewery) {
        this.brewery = brewery;
    }
}
