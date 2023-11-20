package org.example.app.entity;

import java.util.HashMap;
import java.util.List;

public class NftMarketplace {
    private String name;
    private HashMap<String, List<NftCollection>> nftInPeriod;

    public NftMarketplace() {
        this.nftInPeriod = new HashMap<String, List<NftCollection>>();
    }

    public NftMarketplace(String name) {
        this.name = name;
        this.nftInPeriod = new HashMap<String, List<NftCollection>>();
    }

    public NftMarketplace(String name, HashMap<String, List<NftCollection>> nftInPeriod) {
        this.name = name;
        this.nftInPeriod = nftInPeriod;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public HashMap<String, List<NftCollection>> getNftInPeriod() {
        return nftInPeriod;
    }

    public void setNftInPeriod(HashMap<String, List<NftCollection>> nftInPeriod) {
        this.nftInPeriod = nftInPeriod;
    }

    @Override
    public String toString() {
        return "{ \'name\':" + name + ",\n\'nftInPeriod\':" + nftInPeriod + ",\n}";
    }
}
