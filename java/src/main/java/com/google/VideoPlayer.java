package com.google;

import java.util.*;

public class VideoPlayer {

  private final VideoLibrary videoLibrary;
  private boolean isPlaying;                   //keep track of if a video is currently playing
  private String currentVideo = null;          //keep track of which video is currently playing if any
  private final HashMap<String, String> flaggedVideos = new HashMap<>();
  private final ArrayList<VideoPlaylist> playLists = new ArrayList<>();

  public VideoPlayer() {
    this.videoLibrary = new VideoLibrary();
  }

  public void numberOfVideos() {
    System.out.printf("%s videos in the library%n", videoLibrary.getVideos().size());
  }

  public void showAllVideos() {
    System.out.println("Here's a list of all available videos:");
    List<Video> allVideos = videoLibrary.getVideos();
    ArrayList<String> vids = new ArrayList<>();
    for (Video v: allVideos){
      if (flaggedVideos.containsKey(v.getVideoId())){
        vids.add(v.getVideo() + " - FLAGGED (reason: " + flaggedVideos.get(v.getVideoId()) + ")");
        continue;
      }
      vids.add(v.getVideo());
    }
    Collections.sort(vids);
    for (String vid: vids){
      System.out.println("  " + vid);
    }
  }

  public void playVideo(String videoId) {
    String vid = null;
    List<Video> allVideos = videoLibrary.getVideos();
    for (Video v: allVideos){
      if (v.getVideoId().equals(videoId)){
        vid = v.getTitle();
        break;
      }
    }
    if (vid == null){
      System.out.println("Cannot play video: Video does not exist");
      return;
    }
    if (isPlaying || currentVideo != null){      //stop any currently playing videos
      System.out.println("Stopping video: " + currentVideo);
    }
    if (flaggedVideos.containsKey(videoId)){
      System.out.println("Cannot play video: Video is currently flagged (reason: " + flaggedVideos.get(videoId) + ")");
      return;
    }
    System.out.println("Playing video: " + vid);
    isPlaying = true;
    currentVideo = vid;
  }

  public void stopVideo() {
    if (isPlaying || currentVideo != null){
      System.out.println("Stopping video: " + currentVideo);
      isPlaying = false;
      currentVideo = null;
    }
    else{
      System.out.println("Cannot stop video: No video is currently playing");
    }
  }

  public void playRandomVideo() {
    if (isPlaying){
      stopVideo();
    }
    List<Video> allVideos = videoLibrary.getVideos();
    if (flaggedVideos.size() == allVideos.size()){
      System.out.println("No videos available");
      return;
    }
    Random rand = new Random();
    int vid = rand.nextInt(allVideos.size());
    System.out.println("Playing video: " + allVideos.get(vid).getTitle());
    isPlaying = true;
    currentVideo = allVideos.get(vid).getTitle();
  }

  public void pauseVideo() {
    if (isPlaying){
      System.out.println("Pausing video: " + currentVideo);
      isPlaying = false;
    }
    else if (currentVideo != null){
      System.out.println("Video already paused: " + currentVideo);
    }
    else{
      System.out.println("Cannot pause video: No video is currently playing");
    }
  }

  public void continueVideo() {
    if (isPlaying){
      System.out.println("Cannot continue video: Video is not paused");
    }
    else if (currentVideo != null){
      System.out.println("Continuing video: " + currentVideo);
      isPlaying = true;
    }
    else{
      System.out.println("Cannot continue video: No video is currently playing");
    }
  }

  public void showPlaying() {
    String vid = null;
    List<Video> allVideos = videoLibrary.getVideos();
    for (Video v: allVideos){
      if (v.getTitle().equals(currentVideo)){
        vid = v.getVideo();       //get details of currently playing video
      }
    }
    if (currentVideo != null){
      if (isPlaying){
        System.out.println("Currently playing: " + vid);
      }
      else{
        System.out.println("Currently playing: " + vid + " - PAUSED");
      }
    }
    else{
      System.out.println("No video is currently playing");
    }
  }

  public void createPlaylist(String playlistName) {
    for (VideoPlaylist pl: playLists){
      if (pl.getName().equalsIgnoreCase(playlistName)){
        System.out.println("Cannot create playlist: A playlist with the same name already exists");
        return;
      }
    }
    VideoPlaylist newPlaylist = new VideoPlaylist(playlistName);
    playLists.add(newPlaylist);
    System.out.println("Successfully created new playlist: " + playlistName);
  }

  public void addVideoToPlaylist(String playlistName, String videoId) {
    List<Video> allVideos = videoLibrary.getVideos();
    Video video = null;
    for (Video v : allVideos){
      if (v.getVideoId().equals(videoId)){
        video = v;
      }
    }
    if (flaggedVideos.containsKey(videoId)){
      System.out.println("Cannot add video to " + playlistName + ": Video is currently flagged (reason: " + flaggedVideos.get(videoId) + ")");
      return;
    }
    for (VideoPlaylist pl : playLists){
      if (pl.getName().equalsIgnoreCase(playlistName)){
        if (video == null){
          System.out.println("Cannot add video to " + playlistName + ": Video does not exist");
          return;
        }
        if (pl.videoAlreadyAdded(videoId)){
          System.out.println("Cannot add video to " + playlistName + ": Video already added");
          return;
        }
        pl.addVideo(video);
        System.out.println("Added video to " + playlistName + ": " + video.getTitle());
        return;
      }
    }
    System.out.println("Cannot add video to " + playlistName + ": Playlist does not exist");
  }

  public void showAllPlaylists() {
    if (playLists.isEmpty()){
      System.out.println("No playlists exist yet");
      return;
    }
    ArrayList<String> playlistNames = new ArrayList<>();
    System.out.println("Showing all playlists: ");
    for (VideoPlaylist pl : playLists) {
      playlistNames.add(pl.getName());
    }
    Collections.sort(playlistNames);
    for (String name : playlistNames){
      System.out.println("  " + name);
    }
  }

  public void showPlaylist(String playlistName) {
    for (VideoPlaylist pl : playLists){
      if (pl.getName().equalsIgnoreCase(playlistName)){
        System.out.println("Showing playlist: " + playlistName);
        if (pl.getSize() == 0){
          System.out.println("  No videos here yet");
          return;
        }
        for (Video vid: pl.getPlayList()){
          List<Video> allVideos = videoLibrary.getVideos();
          for (Video v: allVideos){
            if (v == vid){
              if (flaggedVideos.containsKey(v.getVideoId())){
                System.out.println("  " + v.getVideo() + " - FLAGGED (reason: " + flaggedVideos.get(v.getVideoId()) + ")");
                continue;
              }
              System.out.println("  " + v.getVideo());
            }
          }
        }
        return;
      }
    }
    System.out.println("Cannot show playlist " + playlistName + ": Playlist does not exist");
  }

  public void removeFromPlaylist(String playlistName, String videoId) {
    List<Video> allVideos = videoLibrary.getVideos();
    Video video = null;
    for (Video v: allVideos){
      if (v.getVideoId().equals(videoId)){
        video = v;
      }
    }
    for (VideoPlaylist pl: playLists){
      if (pl.getName().equalsIgnoreCase(playlistName)){
        if (video == null){
          System.out.println("Cannot remove video from " + playlistName + ": Video does not exist");
          return;
        }
        for (Video vid : pl.getPlayList()){
          if (video == vid){
            pl.removeVideo(video);
            System.out.println("Removed video from " + playlistName + ": " + video.getTitle());
            return;
          }
        }
        System.out.println("Cannot remove video from " + playlistName + ": Video is not in playlist");
        return;
      }
    }
    System.out.println("Cannot remove video from " + playlistName + ": Playlist does not exist");
  }

  public void clearPlaylist(String playlistName) {
    for (VideoPlaylist pl : playLists){
      if (pl.getName().equalsIgnoreCase(playlistName)){
        if (pl.getSize() == 0){
          System.out.println("Playlist already empty");
          return;
        }
        pl.clearPlaylist();
        System.out.println("Successfully removed all videos from " + playlistName);
        return;
      }
    }
    System.out.println("Cannot clear playlist " + playlistName + ": Playlist does not exist");
  }

  public void deletePlaylist(String playlistName) {
    for (int i = 0; i< playLists.size(); i++){
      if (playLists.get(i).getName().equalsIgnoreCase(playlistName)){
        playLists.remove(i);
        System.out.println("Deleted playlist: " + playlistName);
        return;
      }
    }
    System.out.println("Cannot delete playlist " + playlistName + ": Playlist does not exist");
  }

  public void searchVideos(String searchTerm) {
    List<Video> allVideos = videoLibrary.getVideos();
    ArrayList<String> results = new ArrayList<>();
    for (Video v: allVideos){
      if (flaggedVideos.containsKey(v.getVideoId())){
        continue;
      }
      String vid = v.getVideo();
      if (vid.toLowerCase().contains(searchTerm.toLowerCase())){
        results.add(vid);
      }
    }
    search(searchTerm, results);
  }

  public void searchVideosWithTag(String videoTag) {
    List<Video> allVideos = videoLibrary.getVideos();
    ArrayList<String> results = new ArrayList<>();
    for (Video v: allVideos){
      if (flaggedVideos.containsKey(v.getVideoId())){
        continue;
      }
      List<String> tags = v.getTags();
      if (tags.contains(videoTag)){
        results.add(v.getVideo());
      }
    }
    search(videoTag, results);
  }

  private void search(String searchTerm, ArrayList<String> results) {
    if (results.isEmpty()){
      System.out.println("No search results for " + searchTerm);
      return;
    }
    Collections.sort(results);
    System.out.println("Here are the results for " + searchTerm + ":");
    for (int i=0; i<results.size(); i++){
      System.out.printf("  %d) " + results.get(i) + "\n", i+1);
    }
    System.out.println("Would you like to play any of the above? If yes, specify the number of the video.");
    System.out.println("If your answer is not a valid number, we will assume it's a no.");
    Scanner sc = new Scanner(System.in);
    if (sc.hasNextInt()){
      int number = sc.nextInt();
      if (number > results.size() || number < 1){  //check for valid number from user
        return;
      }
      System.out.println("Playing video: " + results.get(number-1));
      isPlaying = true;
      currentVideo = results.get(number-1);
    }
  }

  public void flagVideo(String videoId) {
    if (flaggedVideos.containsKey(videoId)){
      System.out.println("Cannot flag video: Video is already flagged");
      return;
    }
    List<Video> allVideos = videoLibrary.getVideos();
    for (Video v: allVideos){
      if (v.getVideoId().equals(videoId)){
        flaggedVideos.put(videoId, "Not supplied");
        if (currentVideo != null && currentVideo.equals(v.getTitle())){
          stopVideo();
        }
        System.out.println("Successfully flagged video: " + v.getTitle() + " (reason: Not supplied)");
        return;
      }
    }
    System.out.println("Cannot flag video: Video does not exist");
  }

  public void flagVideo(String videoId, String reason) {
    if (flaggedVideos.containsKey(videoId)){
      System.out.println("Cannot flag video: Video is already flagged");
      return;
    }
    List<Video> allVideos = videoLibrary.getVideos();
    for (Video v: allVideos){
      if (v.getVideoId().equals(videoId)){
        flaggedVideos.put(videoId, reason);
        if (currentVideo != null && currentVideo.equals(v.getTitle())){
          stopVideo();
        }
        System.out.println("Successfully flagged video: " + v.getTitle() + " (reason: " + reason + ")");
        return;
      }
    }
    System.out.println("Cannot flag video: Video does not exist");
  }

  public void allowVideo(String videoId) {
    List<Video> allVideos = videoLibrary.getVideos();
    for (Video v: allVideos){
      if (v.getVideoId().equals(videoId)){
        if (flaggedVideos.containsKey(videoId)){
          flaggedVideos.remove(videoId);
          System.out.println("Successfully removed flag from video: " + v.getTitle());
          return;
        }
        System.out.println("Cannot remove flag from video: Video is not flagged");
        return;
      }
    }
    System.out.println("Cannot remove flag from video: Video does not exist");
  }
}