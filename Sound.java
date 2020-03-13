import javafx.scene.media.AudioClip;

/**
 * The class Sound stores the sound clip that is played when the ball hits a brick.
 * I have used AudioClip as this is specifically for a segment of audio, which is
 * ideal for my program as the sound is only played when a certain condition is met.
 *
 * AudioClip Reference: Junggle, 03/12/2006, https://freesound.org/s/26777/
 * 
 */

public class Sound
{   
    public static void soundClip() {
       AudioClip collisionSound = new AudioClip(Model.class.getResource("breakoutCollisionSound.mp3").toString());
       collisionSound.play();
       // Audio clip reference noted in the Javadoc comments at the top of this class
    }
}
