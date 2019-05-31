import javafx.application.Platform;
import javafx.collections.MapChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Coffea Controller
 *
 * @author: Rahul Deshpande
 * @author: Siddhant Sharma
 */
public class Controller implements Initializable {
    boolean firstTime = true;
    @FXML
    private ToggleButton playPause;
    @FXML
    private MediaView player;
    @FXML
    private Button backButton, skipButton;
    @FXML
    private Label songName;
    @FXML
    private Label artistName;
    @FXML
    private ImageView albumCover;
    @FXML
    private Button chooseSong;
    private ArrayList<Media> mediaFiles = new ArrayList();
    private int counter = -1;

    @Override
    /**
     * Handles initialization
     */
    public void initialize(URL url, ResourceBundle rb) {
        Platform.runLater(() -> playPause.requestFocus());
    }

    @FXML
    /**
     * Handles when the play / pause button is clicked
     */
    private void playPauseClick(ActionEvent event) {
        if (firstTime) {
            File file;
            Stage stage = (Stage) playPause.getScene().getWindow();
            FileChooser fileChooser = new FileChooser();
            FileChooser.ExtensionFilter fileExtension = new FileChooser.ExtensionFilter("Audio files", "*.mp3", "*.wav");
            fileChooser.getExtensionFilters().add(fileExtension);
            file = fileChooser.showOpenDialog(stage);
            if (file != null) {
                Media media;
                media = new Media(file.toURI().toASCIIString());
                mediaFiles.add(media);
                MediaPlayer mediaplayer = new MediaPlayer(media);
                backButton.setDisable(false);
                skipButton.setDisable(false);
                ++counter;
                player = new MediaView(mediaplayer);
                mediaplayer.setAutoPlay(true);
                checkForChange();
                player.getMediaPlayer().setOnEndOfMedia(() ->
                {
                    playPause.setSelected(false);
                });
                firstTime = false;

            } else {
                playPause.setSelected(false);
            }
        } else {
            if (playPause.isSelected()) {
                player.getMediaPlayer().play();
            } else {
                player.getMediaPlayer().pause();
            }
        }
    }

    @FXML
    /**
     * Handles the choosing of a song file
     */
    private void chooseSong(ActionEvent event) {
        firstTime = false;
        Stage stage = (Stage) playPause.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter fileExtension = new FileChooser.ExtensionFilter("Audio files", "*.mp3", "*.wav");
        fileChooser.getExtensionFilters().add(fileExtension);
        File file = fileChooser.showOpenDialog(stage);
        Media media;
        media = new Media(file.toURI().toASCIIString());
        mediaFiles.add(media);
        MediaPlayer mediaplayer = new MediaPlayer(media);
        try {
            player.getMediaPlayer().dispose();
        } catch (Exception e) {
        }
        backButton.setDisable(false);
        skipButton.setDisable(false);
        playPause.setSelected(true);
        ++counter;
        mediaplayer.setAutoPlay(true);
        player = new MediaView(mediaplayer);
        checkForChange();
        player.getMediaPlayer().setOnEndOfMedia(() ->
        {
            playPause.setSelected(false);
        });
    }


    // This method was taken from the answer to this question on StackOverflow since we had trouble with retrieving metadata.
    // https://stackoverflow.com/questions/43143756/how-to-get-metadata-from-media-objects
    private void checkForChange() {
        player.getMediaPlayer().getMedia().getMetadata().addListener((MapChangeListener.Change<? extends String, ? extends Object> change) ->
        {
            if (change.wasAdded()) {
                if (change.getKey().equals("image")) {
                    Image art = (Image) change.getValueAdded();
                    double artWidth = art.getWidth(), viewWidth = albumCover.getFitWidth();
                    albumCover.setX(50);
                    albumCover.setImage(art);
                    albumCover.setX(50);
                }
                if (change.getKey().equals("title")) {
                    String name = (String) change.getValueAdded();
                    songName.setText(name);
                }
                if (change.getKey().equals("artist")) {
                    String name = (String) change.getValueAdded();
                    artistName.setText(name);
                }
            }
        });
    }

    @FXML
    /**
     * Handles clicking of previous button
     */
    private void backClick(ActionEvent event) {
        if (counter == 0) {
            player.getMediaPlayer().seek(Duration.ZERO);
        } else {
            player.getMediaPlayer().dispose();
            albumCover.setImage(null);
            player = new MediaView(new MediaPlayer(mediaFiles.get(--counter)));
            player.getMediaPlayer().play();
            playPause.setSelected(true);
            player.getMediaPlayer().setOnEndOfMedia(() -> {
                playPause.setSelected(false);
            });
            checkForChange();
        }
    }

    @FXML
    /**
     * Handles clicking of next button
     */
    private void skipClick(ActionEvent event) {
        if (counter + 1 == mediaFiles.size()) {
            player.getMediaPlayer().stop();
            playPause.setSelected(false);
        } else {
            player.getMediaPlayer().dispose();
            albumCover.setImage(null);
            player = new MediaView(new MediaPlayer(mediaFiles.get(++counter)));
            playPause.setSelected(true);
            player.getMediaPlayer().play();
            player.getMediaPlayer().setOnEndOfMedia(() -> {
                playPause.setSelected(false);
            });
            checkForChange();
        }
    }
}
