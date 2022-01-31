package tk.shardsoftware.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

import java.util.Random;

import tk.shardsoftware.screens.GameScreen;

/**
 * Manages playing sounds/music, adjusting volume, muting e.t.c
 * @author Hector Woods
 */
public class SoundManager {

    /** The universal game volume */
    public static float gameVolume = 1f;
    public static boolean isMuted = false;
    public static float lastGameVolume = gameVolume;
    public static int currentSongIndex = 0;
    static Music[] songs = {Gdx.audio.newMusic(Gdx.files.internal("audio/music/the-pyre.mp3")), Gdx.audio.newMusic(Gdx.files.internal("audio/music/folk-round.mp3"))};


    public static void playSound(Sound sound){
        if(isMuted){
            return;
        }
        sound.play(gameVolume);
    }

    public static void playSound(Sound sound, float volume){
        if(isMuted){
            return;
        }
        sound.play(gameVolume*volume);
    }

    public static void muteVolume() {
        lastGameVolume = gameVolume;
        isMuted = true;
        gameVolume = 0;
        stopMusic();
    }

    public static void playRandomMusic(){
        //Set completion listener for each song so that it plays the next song in Songs[]
        for (int i = 0; i < songs.length; i++) {
            songs[i].setOnCompletionListener(new Music.OnCompletionListener() {
                @Override
                public void onCompletion(Music music) {
                    currentSongIndex = (currentSongIndex + 1) % songs.length;
                    System.out.println("Song Finished. Now playing song " + currentSongIndex);
                    songs[currentSongIndex].play();
                }
            });
        }
        //Choose a random song in the list and play it
        currentSongIndex = new Random().nextInt(songs.length);
        System.out.println("Playing song " + currentSongIndex);
        songs[currentSongIndex].play();
    }
    public static void stopMusic(){
        for(int i = 0; i < songs.length; i++){
            if(songs[i].isPlaying()){
                songs[i].stop();
            }
        }
    }


    public static void toggleMute(){
        System.out.println("fuck my life");
        isMuted = !isMuted;
        if(isMuted){
            muteVolume();
        }else{
            gameVolume = lastGameVolume;
            playRandomMusic();
        }
    }

    public static void setVolume(float volume) {
        gameVolume = volume;
    }

}
