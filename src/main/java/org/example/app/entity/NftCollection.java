package org.example.app.entity;

public class NftCollection {
    private String rank;
    private String name;
    private String floorPrice;
    private String floorChange;
    private String volume;
    private String volumeChange;
    private int items;
    private int owners;

    public NftCollection() {
    }

    public NftCollection(String rank, String name, String floorPrice, String floorChange, String volume,
            String volumeChange,
            int items, int owners) {
        this.rank = rank;
        this.name = name;
        this.floorPrice = floorPrice;
        this.floorChange = floorChange;
        this.volume = volume;
        this.volumeChange = volumeChange;
        this.items = items;
        this.owners = owners;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFloorPrice() {
        return floorPrice;
    }

    public void setFloorPrice(String floorPrice) {
        this.floorPrice = floorPrice;
    }

    public String getFloorChange() {
        return floorChange;
    }

    public void setFloorChange(String floorChange) {
        this.floorChange = floorChange;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getVolumeChange() {
        return volumeChange;
    }

    public void setVolumeChange(String volumeChange) {
        this.volumeChange = volumeChange;
    }

    public int getItems() {
        return items;
    }

    public void setItems(int items) {
        this.items = items;
    }

    public int getOwners() {
        return owners;
    }

    public void setOwners(int owners) {
        this.owners = owners;
    }

    public String toString() {
        return "{\nRank: " + this.rank + "\nName: " + this.name + "\nFloor Price: " + this.floorPrice
                + "\nFloor Change: " + this.floorChange + "\nVolume: " + this.volume + "\nVolume Change: "
                + this.volumeChange + "\nItems: " + this.items + "\nOwners: " + this.owners + "\n}";
    }
}
