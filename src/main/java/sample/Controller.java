package sample;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.DirectoryChooser;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Controller {

    @FXML
    TextField urlTF;
    @FXML
    TextField saveToTF;

    @FXML
    TextField watermarkTF;
    public void download()
    {
        try{

            Document document = Jsoup.connect(urlTF.getText().trim())
                    .timeout(0)
                    .maxBodySize(0)
                    .ignoreContentType(true)
                    .ignoreHttpErrors(true)
                    .get();

            Elements links = document.select("ul.tb-img").first().select("li a");
            Elements thumnails = document.select("ul.tb-thumb").first().select( "li img");

            for (Element e : links)
            {
                try {
                System.out.println(e.attr("style"));
                String imageLink = e.attr("style").replace("background:url(", "https:");

                if (!imageLink.contains(")"))
                    continue;
                imageLink = imageLink.substring(0, imageLink.indexOf(")"));
                imageLink = imageLink.replace("_30x30.jpg", "");

                System.out.println(imageLink);

                    downloadSingleImage(imageLink);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }

            for (Element e2: thumnails)
            {
                try {
                String imageLink = e2.attr("data-src").replace("_50x50.jpg", "");
                if(!imageLink.startsWith("https:"))
                    imageLink = "https:" + imageLink;

                    downloadSingleImage(imageLink);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }

        } catch (Exception ex)
        {
            ex.printStackTrace();
        }

    }

    @FXML
    public void initialize()
    {
        watermarkTF.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                DirectoryChooser chooser = new DirectoryChooser();
                chooser.setTitle("Select folder to water");
                File dir =  chooser.showDialog(watermarkTF.getScene().getWindow());

                if (dir!=null)
                    watermarkTF.setText(dir.getAbsolutePath());
            }
        });
    }

    private void downloadSingleImage(String imageLocation) throws IOException
    {
        //Open a URL Stream
        String imageName = imageLocation.split("/")[imageLocation.split("/").length -1];
        System.out.println("image location: " + imageLocation);
        Connection.Response resultImageResponse = Jsoup.connect(imageLocation)
                .timeout(0)
                .ignoreHttpErrors(true)
                .ignoreContentType(true).execute();

// output heresaveToTFf
        String path;
        if (!saveToTF.getText().trim().equals(""))
            path = saveToTF.getText();
        else
            path = "C:\\Users\\MYN\\Desktop\\lab\\";

        FileOutputStream out = (new FileOutputStream(new java.io.File(path + imageName)));
        out.write(resultImageResponse.bodyAsBytes());  // resultImageResponse.body() is where the image's contents are.
        out.close();
    }

    public void watermark()
    {
        String dir = watermarkTF.getText();
        if (dir.equals(""))
            return;
        watermarkTF.setOpacity(0.3);
        Watermark.mass(dir);
         watermarkTF.setOpacity(1);
    }
}
