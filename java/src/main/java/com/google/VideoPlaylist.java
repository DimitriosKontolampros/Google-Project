package com.google;

import org.sonatype.guice.bean.containers.Main;

import java.util.ArrayList;

/** A class used to represent a Playlist */
class VideoPlaylist {

    String name=null;
    ArrayList<Video> list_of_videos=new ArrayList<Video>();

    VideoPlaylist(String name_){
        name=name_;

    }

    public ArrayList<Video>getVideos(){
        return list_of_videos;
    }
}
