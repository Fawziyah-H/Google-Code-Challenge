package com.google;

import java.util.ArrayList;

/** A class used to represent a Playlist */
class VideoPlaylist {

    private final String name;
    private final ArrayList<Video> videos = new ArrayList<>();

    VideoPlaylist(String name){
        this.name = name;
    }

    public ArrayList<Video> getPlayList(){
        return videos;
    }

    public void addVideo(Video video){
        videos.add(video);
    }

    public String getName(){
        return name;
    }

    public int getSize(){
        return videos.size();
    }

    public boolean videoAlreadyAdded(String id){
        for (Video v: videos){
            if (v.getVideoId().equals(id)){
                return true;
            }
        }
        return false;
    }

    public void removeVideo(Video v){
        videos.remove(v);
    }

    public void clearPlaylist(){
        videos.removeAll(videos);
    }
}
