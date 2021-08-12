package com.google;

import javax.swing.text.html.HTML;
import java.util.*;
import java.util.zip.InflaterInputStream;

public class VideoPlayer {
  Video playing_video=null;
  Video paused_video=null;
  private final VideoLibrary videoLibrary;
  ArrayList<VideoPlaylist> list_of_playlists=new ArrayList<VideoPlaylist>();




  public VideoPlayer() {
    this.videoLibrary = new VideoLibrary();
  }

  public void numberOfVideos() {
    System.out.printf("%s videos in the library%n", videoLibrary.getVideos().size());
  }

  public void showAllVideos() {

    System.out.println("Here's a list of all available videos:");
    ArrayList<String> available_videos=new ArrayList<String>();




    for(int x = 0; x<videoLibrary.getVideos().size(); x++){

      String adding_string="[";
      for (int i=0; i<videoLibrary.getVideos().get(x).getTags().size();i++){
        adding_string=adding_string+videoLibrary.getVideos().get(x).getTags().get(i);
        if(i!=videoLibrary.getVideos().get(x).getTags().size()-1){
          adding_string=adding_string+" ";
        }
      }
      adding_string=adding_string+"]";

      boolean flaged=false;
      int position=0;
      for (Video video:list_of_flagged_videos){
        if(video.getTitle().equals(videoLibrary.getVideos().get(x).getTitle())){
          flaged=true;
          position=list_of_flagged_videos.indexOf(video);
          break;
        }
      }
      if(flaged){
        available_videos.add(" "+videoLibrary.getVideos().get(x).getTitle()+" ("+videoLibrary.getVideos().get(x).getVideoId()+") "+adding_string+" - FLAGGED (reason: "+list_of_flagged_reasons.get(position)+")");
      }
      else {
        available_videos.add(" " + videoLibrary.getVideos().get(x).getTitle() + " (" + videoLibrary.getVideos().get(x).getVideoId() + ") " + adding_string);
      }
    }
    Collections.sort(available_videos);
    for(String video_characteristics:available_videos){
      System.out.println(video_characteristics);
    }
  }

  public void playVideo(String videoId) {
    boolean flagged=false;
    int position_in_flagged=0;
    for(Video video : list_of_flagged_videos){
      if(video.getVideoId().equals(videoId)){
        flagged=true;
        position_in_flagged=list_of_flagged_videos.indexOf(video);
        break;
      }
    }
    if(flagged){
      System.out.println("Cannot play video: Video is currently flagged (reason: "+list_of_flagged_reasons.get(position_in_flagged)+")");
    }
    else if(videoLibrary.getVideos().contains(videoLibrary.getVideo(videoId))){
      if(playing_video==null){
        playing_video=videoLibrary.getVideo(videoId);
        System.out.println("Playing video: "+playing_video.getTitle());
      }
      else{
        System.out.println("Stopping video: "+playing_video.getTitle());
        playing_video=videoLibrary.getVideo(videoId);
        System.out.println("Playing video: "+playing_video.getTitle());
        paused_video=null;
      }
    }
    else {
      System.out.println("Cannot play video: Video does not exist");
    }


  }

  public void stopVideo() {
    if(playing_video==null){
      System.out.println("Cannot stop video: No video is currently playing");
    }
    else{
      System.out.println("Stopping video: "+playing_video.getTitle());
      playing_video=null;
      paused_video=null;
    }

  }

  public void playRandomVideo() {
    Random rand=new Random();

    ArrayList<Video> all_acceptable_videos=new ArrayList<Video>();

    for(Video video:videoLibrary.getVideos()){

        if(list_of_flagged_videos.contains(video)==false){
          all_acceptable_videos.add(video);
        }

    }
    int random_video_num=0;
    if(all_acceptable_videos.size()>0){
      random_video_num=rand.nextInt(all_acceptable_videos.size());
    }


    if(all_acceptable_videos.size()!=0) {
      if (playing_video == null) {
        System.out.println("Playing video: " + all_acceptable_videos.get(random_video_num).getTitle());
        playing_video = all_acceptable_videos.get(random_video_num);
      } else {
        System.out.println("Stopping video: " + playing_video.getTitle());
        System.out.println("Playing video: " + all_acceptable_videos.get(random_video_num).getTitle());
        playing_video = all_acceptable_videos.get(random_video_num);

      }
    }
    else{
        System.out.println("No videos available");
      }
  }

  public void pauseVideo() {
    if(playing_video==null){
      System.out.println("Cannot pause video: No video is currently playing");
    }
    else if (paused_video!=null){
      System.out.println("Video already paused: "+paused_video.getTitle());
    }
    else{
      paused_video=playing_video;

      System.out.println("Pausing video: "+paused_video.getTitle());
    }
  }

  public void continueVideo() {
    if(playing_video!=null && paused_video==null){
      System.out.println("Cannot continue video: Video is not paused");
    }
    else if (playing_video==null){
      System.out.println("Cannot continue video: No video is currently playing");
    }
    else if(playing_video!=null && paused_video!=null){

      System.out.println("Continuing video: "+paused_video.getTitle());
      paused_video=null;
    }
  }

  public void showPlaying() {
    if (playing_video!=null && paused_video==null ){
      String printing_string="Currently playing: "+playing_video.getTitle()+" ("+playing_video.getVideoId()+") ";
      String adding_string="[";
      for (int i=0; i<playing_video.getTags().size();i++){
        adding_string=adding_string+playing_video.getTags().get(i);
        if(i!=playing_video.getTags().size()-1){
          adding_string=adding_string+" ";
        }
      }
      adding_string=adding_string+"]";
      System.out.println(printing_string+adding_string);

    }
    else if(playing_video!=null && paused_video!=null){

      String printing_string="Currently playing: "+playing_video.getTitle()+" ("+playing_video.getVideoId()+") ";
      String adding_string="[";
      for (int i=0; i<playing_video.getTags().size();i++){
        adding_string=adding_string+playing_video.getTags().get(i);
        if(i!=playing_video.getTags().size()-1){
          adding_string=adding_string+" ";
        }
      }
      adding_string=adding_string+"]";
      System.out.println(printing_string+adding_string+" - PAUSED");

    }
    else if(playing_video==null ){
      System.out.println("No video is currently playing");
    }
  }

  public void createPlaylist(String playlistName) {
    boolean contained=false;
    for(VideoPlaylist playlist : list_of_playlists){
      if(playlist.name.equalsIgnoreCase(playlistName)){
        contained=true;
        break;
      }
    }
    if(contained!=true){
      list_of_playlists.add(new VideoPlaylist(playlistName));
      System.out.println("Successfully created new playlist: "+playlistName);
    }
    else{
      System.out.println("Cannot create playlist: A playlist with the same name already exists");
    }

  }

  public void addVideoToPlaylist(String playlistName, String videoId) {
    boolean flagged=false;
    int position_in_flagged=0;
    for(Video video : list_of_flagged_videos){
      if(video.getVideoId().equals(videoId)){
        flagged=true;
        position_in_flagged=list_of_flagged_videos.indexOf(video);
        break;
      }
    }
    boolean playlist_exists=false;
    boolean video_exists=false;
    boolean video_already_in_playlist=false;
    Video adding_video=null;


    for(VideoPlaylist playlist : list_of_playlists){
      if(playlist.name.equalsIgnoreCase(playlistName)){
        playlist_exists=true;
        break;
      }
    }

    for(Video video : videoLibrary.getVideos()){

      if(video.getVideoId().equalsIgnoreCase(videoId)){
        video_exists=true;
        adding_video=video;
        break;
      }
    }
    for(VideoPlaylist playlist : list_of_playlists){
      if(playlist.name.equalsIgnoreCase(playlistName)){
        for(Video video :playlist.getVideos()){
          if(video.getVideoId().equalsIgnoreCase(videoId))
          {
            video_already_in_playlist=true;
            break;
          }
        }
      }
    }


    if(flagged){
      System.out.println("Cannot add video to "+playlistName+": Video is currently flagged (reason: "+list_of_flagged_reasons.get(position_in_flagged)+")");
    }
    else if(playlist_exists&&video_exists&&video_already_in_playlist==false){
      System.out.println("Added video to "+playlistName+": "+adding_video.getTitle());
      for(VideoPlaylist playlist : list_of_playlists){
        if(playlist.name.equalsIgnoreCase(playlistName)){
          playlist.list_of_videos.add(adding_video);
          }
        }
      }
    else if(playlist_exists&&video_already_in_playlist&&video_exists){
      System.out.println("Cannot add video to "+playlistName+": Video already added");
    }
    else if(playlist_exists&&video_exists==false){
      System.out.println("Cannot add video to "+playlistName+": Video does not exist");
    }
    else if(playlist_exists==false){
      System.out.println("Cannot add video to "+playlistName+": Playlist does not exist");
    }

    
  }

  public void showAllPlaylists() {
    if(list_of_playlists.size()==0){
      System.out.println("No playlists exist yet");
    }
    else{
      System.out.println("Showing all playlists:");
      ArrayList<String> list_of_playlist_names=new ArrayList<String>();
      for (VideoPlaylist playlist :list_of_playlists){
        list_of_playlist_names.add(playlist.name);
      }
      Collections.sort(list_of_playlist_names);
      for(String name:list_of_playlist_names){
        System.out.println("  "+name);
      }
    }
  }

  public void showPlaylist(String playlistName) {
    boolean playlist_exists=false;
    for(VideoPlaylist playlist : list_of_playlists){
      if(playlist.name.equalsIgnoreCase(playlistName)){
        playlist_exists=true;
        break;
        }
      }
    if(playlist_exists){
      for(VideoPlaylist playlist : list_of_playlists){
        if(playlist.name.equalsIgnoreCase(playlistName)){
          if(playlist.getVideos().size()==0){
            System.out.println("Showing playlist: "+playlistName);
            System.out.println("  No videos here yet");
          }
          else{
            System.out.println("Showing playlist: "+playlistName);
            for(Video video:playlist.getVideos()) {
              int position=0;
              for(Video video1:list_of_flagged_videos) {
                if(video.equals(video1)){
                  position=list_of_flagged_videos.indexOf(video);
                }
              }
              String adding_string = "[";
              for (int i = 0; i < video.getTags().size(); i++) {
                adding_string = adding_string + video.getTags().get(i);
                if (i != video.getTags().size() - 1) {
                  adding_string = adding_string + " ";
                }
              }
              adding_string = adding_string + "]";
              if(list_of_flagged_videos.contains(video)){
                System.out.println("  "+video.getTitle() + " (" + video.getVideoId() + ") " + adding_string+" - FLAGGED (reason: "+list_of_flagged_reasons.get(position)+")");
              }
              else
                {
                System.out.println("  " + video.getTitle() + " (" + video.getVideoId() + ") " + adding_string);
              }
            }
          }
        }
      }
    }
    else{
      System.out.println("Cannot show playlist "+playlistName+": Playlist does not exist");
    }

  }

  public void removeFromPlaylist(String playlistName, String videoId) {
    boolean playlist_exists=false;
    boolean video_exists=false;
    boolean video_in_playlist=false;
    Video removing_video=null;

    for(VideoPlaylist playlist : list_of_playlists){
      if(playlist.name.equalsIgnoreCase(playlistName)){
        playlist_exists=true;

        break;
      }
    }

    for(Video video : videoLibrary.getVideos()){

      if(video.getVideoId().equalsIgnoreCase(videoId)){
        video_exists=true;
        removing_video=video;
        break;
      }
    }
    for(VideoPlaylist playlist : list_of_playlists){
      if(playlist.name.equalsIgnoreCase(playlistName)){
        for(Video video :playlist.getVideos()){
          if(video.getVideoId().equalsIgnoreCase(videoId))
          {
            video_in_playlist=true;
            break;
          }
        }
      }
    }
    if(playlist_exists&&video_exists&&video_in_playlist){
      System.out.println("Removed video from "+playlistName+": "+removing_video.getTitle());
      for(VideoPlaylist playlist : list_of_playlists){
        if(playlist.name.equalsIgnoreCase(playlistName)){
          playlist.list_of_videos.remove(removing_video);
        }
      }



    }
    else if(playlist_exists&&video_in_playlist==false&&video_exists){
      System.out.println("Cannot remove video from "+playlistName+": Video is not in playlist");
    }
    else if(playlist_exists&&video_exists==false){
      System.out.println("Cannot remove video from "+playlistName+": Video does not exist");
    }
    else if(playlist_exists==false){
      System.out.println("Cannot remove video from "+playlistName+": Playlist does not exist");
    }



  }

  public void clearPlaylist(String playlistName) {
    boolean playlist_exists=false;

    for(VideoPlaylist playlist : list_of_playlists){
      if(playlist.name.equalsIgnoreCase(playlistName)){
        playlist_exists=true;

        break;
      }
    }

    if(playlist_exists){
      for(VideoPlaylist playlist : list_of_playlists){
        if(playlist.name.equalsIgnoreCase(playlistName)){
          playlist.getVideos().clear();
          System.out.println("Successfully removed all videos from "+playlistName);
        }
      }
    }
    else{
      System.out.println("Cannot clear playlist "+playlistName+": Playlist does not exist");
    }
  }


  public void deletePlaylist(String playlistName) {
    boolean playlist_exists = false;
    for (VideoPlaylist playlist : list_of_playlists) {
      if (playlist.name.equalsIgnoreCase(playlistName)) {
        playlist_exists = true;

        break;
      }
    }
    if (playlist_exists) {
      for (VideoPlaylist playlist : list_of_playlists) {
        if (playlist.name.equalsIgnoreCase(playlistName)) {
          list_of_playlists.remove(playlist);
          System.out.println("Deleted playlist: " + playlistName);
          break;
        }

      }
    } else {
      System.out.println("Cannot delete playlist " + playlistName + ": Playlist does not exist");
    }

  }
  public void searchVideos(String searchTerm) {
    boolean success=false;
    Integer count=1;
    ArrayList<String> search_videos=new ArrayList<String>();

    for(Video video : videoLibrary.getVideos()) {
      if(video.getTitle().toLowerCase().contains(searchTerm.toLowerCase())) {
        System.out.println("Here are the results for " + searchTerm+":");
        success=true;
        break;
      }
      else{
        System.out.println("No search results for "+searchTerm);
        break;
      }
    }
    for(Video video : videoLibrary.getVideos()){
      if(video.getTitle().toLowerCase().contains(searchTerm.toLowerCase())){


        String adding_string = "[";
        for (int i = 0; i < video.getTags().size(); i++) {
          adding_string = adding_string + video.getTags().get(i);
          if (i != video.getTags().size() - 1) {
            adding_string = adding_string + " ";
          }
        }
        adding_string = adding_string + "]";
        String final_string = video.getTitle() + " (" + video.getVideoId() + ") " + adding_string;

        if(list_of_flagged_videos.contains(video)==false){
          search_videos.add(final_string);
        }





      }

    }
    Collections.sort(search_videos);
    for(String print : search_videos){
      System.out.println("  "+count+") "+print);
      count++;
    }
    for(Video video : videoLibrary.getVideos()){
      if(video.getTitle().toLowerCase().contains(searchTerm.toLowerCase())){

        System.out.println("Would you like to play any of the above? If yes, specify the number of the video.");
        System.out.println("If your answer is not a valid number, we will assume it's a no.");
        break;
      }

    }

    if(success) {
      var scanner = new Scanner(System.in);
      try {
        Integer input = Integer.parseInt(scanner.nextLine());
        if (input <= search_videos.size() && input > 0) {
          String Title = search_videos.get(input - 1);
          for (Video video_ : videoLibrary.getVideos()) {
            if (video_.getTitle().equals(Title.substring(0, Title.indexOf("(") - 1))) {
              playing_video = video_;
              System.out.println("Playing video: " + playing_video.getTitle());
            }
          }
        }

      } catch (Exception e) {

      }

    }
  }





  public void searchVideosWithTag(String videoTag) {
    boolean success=false;
    Integer count=1;
    ArrayList<String > search_videos=new ArrayList<String>();
    for (Video video : videoLibrary.getVideos()) {
      if (video.getTags().indexOf(videoTag) >-1){
        success=true;
        break;
      }
    }

    if(success){
      System.out.println("Here are the results for "+videoTag+":");
      for (Video video : videoLibrary.getVideos()) {
        if (video.getTags().indexOf(videoTag) > -1) {
          String adding_string = "[";
          for (int i = 0; i < video.getTags().size(); i++) {
            adding_string = adding_string + video.getTags().get(i);
            if (i != video.getTags().size() - 1) {
              adding_string = adding_string + " ";
            }
          }
          adding_string = adding_string + "]";
          String final_string = video.getTitle() + " (" + video.getVideoId() + ") " + adding_string;
          if(list_of_flagged_videos.contains(video)==false){
            search_videos.add(final_string);
          }
        }
      }

      Collections.sort(search_videos);
      for (String print : search_videos) {
        System.out.println("  " + count + ") " + print);
        count++;
      }
      for (Video video : videoLibrary.getVideos()) {
        if (video.getTags().indexOf(videoTag) > -1) {

          System.out.println("Would you like to play any of the above? If yes, specify the number of the video.");
          System.out.println("If your answer is not a valid number, we will assume it's a no.");
          break;
        }
      }

      var scanner = new Scanner(System.in);
      try {
        Integer input = Integer.parseInt(scanner.nextLine());
        if (input <= search_videos.size() && input > 0) {
          String Title = search_videos.get(input - 1);
          for (Video video_ : videoLibrary.getVideos()) {
            if (video_.getTitle().equals(Title.substring(0, Title.indexOf("(") - 1))) {
              playing_video = video_;
              System.out.println("Playing video: " + playing_video.getTitle());
            }
          }
        }

      } catch (Exception e) {

      }

    }
    else{
      System.out.println("No search results for "+videoTag);
    }
  }






  ArrayList<Video> list_of_flagged_videos=new ArrayList<Video>();
  ArrayList<String> list_of_flagged_reasons=new ArrayList<String>();



  public void flagVideo(String videoId) {
    boolean video_exists=false;
    boolean video_already_flagged=false;

    for(Video video: videoLibrary.getVideos()){
      if(video.getVideoId().equals(videoId)){
        video_exists=true;
        break;
      }
    }

    for(Video video: list_of_flagged_videos){
      if(video.getVideoId().equals(videoId)){
        video_already_flagged=true;
        break;
      }
    }


    if(video_exists==false){
      System.out.println("Cannot flag video: Video does not exist");
    }
    else if(video_already_flagged){
      System.out.println("Cannot flag video: Video is already flagged");
    }
    else if(video_exists&&video_already_flagged==false){
      int position=0;
      for(Video video: videoLibrary.getVideos()){
        if(video.getVideoId().equals(videoId)){
          list_of_flagged_videos.add(video);
          position=list_of_flagged_videos.indexOf(video);
          if(playing_video!=null && playing_video.equals(video)){
            System.out.println("Stopping video: "+video.getTitle());
            playing_video=null;
          }
          break;
        }
      }

      list_of_flagged_reasons.add("Not supplied");
      System.out.println("Successfully flagged video: "+list_of_flagged_videos.get(position).getTitle()+" (reason: "+list_of_flagged_reasons.get(position)+")");
    }


  }

  public void flagVideo(String videoId, String reason) {
    boolean video_exists=false;
    boolean video_already_flagged=false;

    for(Video video: videoLibrary.getVideos()){
      if(video.getVideoId().equals(videoId)){
        video_exists=true;
        break;
      }
    }

    for(Video video: list_of_flagged_videos){
      if(video.getVideoId().equals(videoId)){
        video_already_flagged=true;
        break;
      }
    }



    if(video_exists==false){
      System.out.println("Cannot flag video: Video does not exist");
    }
    else if(paused_video!=null&& playing_video!=null&&playing_video.getVideoId().equals(videoId)&&paused_video.getVideoId().equals(videoId)==false){
      System.out.println("Stopping video: "+playing_video.getTitle());
      int position=0;
      for(Video video: videoLibrary.getVideos()){
        if(video.getVideoId().equals(videoId)){
          list_of_flagged_videos.add(video);
          position=list_of_flagged_videos.indexOf(video);
          break;
        }
      }


      list_of_flagged_reasons.add(reason);
      System.out.println("Successfully flagged video: "+list_of_flagged_videos.get(position).getTitle()+" (reason: "+list_of_flagged_reasons.get(position)+")");
    }

    else if(paused_video!=null&& playing_video!=null&&playing_video.getVideoId().equals(videoId)&&paused_video.getVideoId().equals(videoId)){
      System.out.println("Stopping video: "+playing_video.getTitle());
      int position=0;
      for(Video video: videoLibrary.getVideos()){
        if(video.getVideoId().equals(videoId)){
          list_of_flagged_videos.add(video);
          position=list_of_flagged_videos.indexOf(video);
          playing_video=null;
          break;
        }
      }


      list_of_flagged_reasons.add(reason);
      System.out.println("Successfully flagged video: "+list_of_flagged_videos.get(position).getTitle()+" (reason: "+list_of_flagged_reasons.get(position)+")");
    }
    else if(playing_video!=null&&playing_video.getVideoId().equals(videoId)){
      System.out.println("Stopping video: "+playing_video.getTitle());
      int position=0;
      for(Video video: videoLibrary.getVideos()){
        if(video.getVideoId().equals(videoId)){
          list_of_flagged_videos.add(video);
          position=list_of_flagged_videos.indexOf(video);
          playing_video=null;
          break;
        }
      }


      list_of_flagged_reasons.add(reason);
      System.out.println("Successfully flagged video: "+list_of_flagged_videos.get(position).getTitle()+" (reason: "+list_of_flagged_reasons.get(position)+")");
    }
    else if(paused_video!=null&& paused_video.getVideoId().equals(videoId)){
      System.out.println("Stopping video: "+paused_video.getTitle());
      paused_video=null;
      playing_video=null;
      int position=0;
      for(Video video: videoLibrary.getVideos()){
        if(video.getVideoId().equals(videoId)){
          list_of_flagged_videos.add(video);
          position=list_of_flagged_videos.indexOf(video);
          break;
        }
      }
      list_of_flagged_reasons.add(reason);
      System.out.println("Successfully flagged video: "+list_of_flagged_videos.get(position).getTitle()+" (reason: "+list_of_flagged_reasons.get(position)+")");
    }
    else if(video_already_flagged){
      System.out.println("Cannot flag video: Video is already flagged");
    }
    else if(video_exists&&video_already_flagged==false){
      int position=0;
      for(Video video: videoLibrary.getVideos()){
        if(video.getVideoId().equals(videoId)){
          list_of_flagged_videos.add(video);
          position=list_of_flagged_videos.indexOf(video);
          break;
        }
      }
      list_of_flagged_reasons.add(reason);
      System.out.println("Successfully flagged video: "+list_of_flagged_videos.get(position).getTitle()+" (reason: "+list_of_flagged_reasons.get(position)+")");
    }


  }

  public void allowVideo(String videoId) {
    int position=0;
    boolean flagged=false;
    boolean exists=false;
    String name="";
    for(Video video:list_of_flagged_videos){
      if(video.getVideoId().equals(videoId)){
        flagged=true;
        exists=true;
        position=list_of_flagged_videos.indexOf(video);
        name=video.getTitle();
        break;
      }
    }
    for(Video video: videoLibrary.getVideos()){
      if(video.getVideoId().equals(videoId)){
        exists=true;
        name=video.getTitle();
        break;
      }
    }



    if(exists==false){
      System.out.println("Cannot remove flag from video: Video does not exist");
    }
    else if(flagged==false){
      System.out.println("Cannot remove flag from video: Video is not flagged");
    }
    else {
      list_of_flagged_videos.remove(position);
      list_of_flagged_reasons.remove(position);
      System.out.println("Successfully removed flag from video: "+name);
    }

  }
}