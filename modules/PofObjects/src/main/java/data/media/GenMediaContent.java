package data.media;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by jackeylv on 2016/7/18.
 */
public class GenMediaContent {

    public static MediaContent build(int type){
        assert type==1 || type == 2 : "type should only be 1 or 2.";

        Media media = new Media();
        List<Image> images = new LinkedList<>();
        switch (type){
            case 1:
                media.setUri("http://javaone.com/keynote.mpg");
                media.setTitle("Javaone Keynote");
                media.setWidth(640);
                media.setHeight(480);
                media.setFormat("video/mpg4");
                media.setDuration(18000000); // half hour in milliseconds
                media.setSize(58982400); // bitrate * duration in seconds / 8 bits per byte
                media.setPersons(Arrays.asList("Bill Gates", "Steve Jobs"));
                media.setPlayer(Media.Player.JAVA);
                media.setCopyright(null);


                images.add(new Image("http://javaone.com/keynote_large.jpg",
                        "Javaone Keynote",
                        1024,
                        768,
                        Image.Size.LARGE
                        ));

                images.add(new Image("http://javaone.com/keynote_small.jpg",
                        "Javaone Keynote",
                        320,
                        240,
                        Image.Size.SMALL
                ));

                break;
            case 2:
                media.setUri("http://javaone.com/keynote.ogg");
                media.setTitle(null);
                media.setWidth(641);
                media.setHeight(481);
                media.setFormat("video/theora");
                media.setDuration(18000001);
                media.setSize(58982401);
                media.setPersons(Arrays.asList("Bill Gates, Jr.", "Steven Jobs"));
                media.setPlayer(Media.Player.FLASH);
                media.setCopyright("2009, Scooby Doo");


                images.add(new Image("http://javaone.com/keynote_huge.jpg",
                        "Javaone Keynote",
                        32000,
                        24000,
                        Image.Size.LARGE
                ));

                images.add(new Image("http://javaone.com/keynote_large.jpg",
                        null,
                        1024,
                        768,
                        Image.Size.LARGE
                ));

                images.add(new Image("http://javaone.com/keynote_small.jpg",
                        null,
                        320,
                        240,
                        Image.Size.SMALL
                ));

                break;
            default:
                throw new AssertionError("type should only be 1 or 2");
        }

        return new MediaContent(media, images);
    }
}
