package com.funon.com.funon.Adapter;

import java.util.List;

public class venuePost {
String userid;
List<VenueAdapter> venueadapterlist;

    public List<VenueAdapter> getVenueadapterlist() {
        return venueadapterlist;

    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public void setVenueadapterlist(List<VenueAdapter> venueadapterlist) {
        this.venueadapterlist = venueadapterlist;
    }

    public String getUserid() {
        return userid;
    }
}
