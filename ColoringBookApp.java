import javafx.application.*;
import javafx.stage.*;
import javafx.stage.FileChooser.*;
import javafx.scene.*;
import javafx.scene.canvas.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.geometry.*;
import java.io.*;
import java.util.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.paint.*;

public class ColoringBookApp extends Application
{
    private class ImageList extends LinkedList<String>
    {
        public ImageList()
        {
            add("Dinosaurs");
            add("Rocket");
        }
    }
    private Image curr_image;
    private ImageList list = new ImageList();
    private int curr_index = 0;
    public void start(Stage mainStage)
    {
        // set the text that appears in the title bar
        mainStage.setTitle("Coloring Book!");
        mainStage.setResizable(true);
        
        // layout manager: organize window contents
        BorderPane root = new BorderPane();
        
        // set font size of objects
        root.setStyle(  "-fx-font-size: 18;"  );
        
        // May want to use a Box to add multiple items to a region of the screen
        VBox box = new VBox();
        // add padding/margin around area
        box.setPadding( new Insets(16) );
        // add space between objects
        box.setSpacing( 16 );
        // set alignment of objects (default: Pos.TOP_LEFT)
        box.setAlignment( Pos.CENTER );
        
        // Scene: contains window content
        // parameters: layout manager; width window; height window
        Scene mainScene = new Scene(root);
        // attach/display Scene on Stage (window)
        mainStage.setScene( mainScene );
        
        // Draw the image required for the coloring book. This is also
        // where the user can draw/color on.
        root.setCenter(box);
        Canvas canvas = new Canvas (500,500);
        GraphicsContext context = canvas.getGraphicsContext2D();
        curr_image = new Image("ColoringBook/" + list.get(curr_index) + ".png",600,500,true,true);
        context.drawImage(curr_image,0,0);
        
        // Default color set
        Color defaultColor = Color.RED;
        context.setFill(defaultColor);
        
        // Row to hold 
        VBox ButtonCol1 = new VBox();
        // Slider to change brush stroke
        Slider strokeSlider = new Slider(2, 24, 2);
        strokeSlider.setMinorTickCount(2);
        strokeSlider.setShowTickLabels(true);
        strokeSlider.setShowTickMarks(true);
        strokeSlider.setMajorTickUnit(2);
        
        // Color picker for stroke color
        ColorPicker picker = new ColorPicker();
        picker.setValue(defaultColor);
        
         // Brush function on canvas
        canvas.setOnMousePressed((event)->
        {
            context.setFill(picker.getValue());
            context.fillRect(event.getX()-5,event.getY()-5,strokeSlider.getValue(),strokeSlider.getValue());
        });
        canvas.setOnMouseDragged((event)->
        {
            context.setFill(picker.getValue());
            context.fillRect(event.getX()-5,event.getY()-5,strokeSlider.getValue(),strokeSlider.getValue());
        });
        ButtonCol1.getChildren().addAll(strokeSlider,picker);
        
        // Button row to switch between images
        HBox ButtonRow1 = new HBox();
        ButtonRow1.setAlignment(Pos.CENTER);
        ButtonRow1.setSpacing(10);
        // Previous image
        Button prevButton = new Button("Previous Page");
        prevButton.setOnAction((event)->{
            curr_index--;
            if (curr_index == -1)
            {
                curr_index = list.size()-1;
            }
            curr_image = new Image("ColoringBook/" + list.get(curr_index) + ".png",600,500,true,true);
            context.drawImage(curr_image,0,0);
        });
        // Next image
        Button nextButton = new Button("Next Page");
        ButtonRow1.getChildren().addAll(prevButton, nextButton);
        nextButton.setOnAction((event)->{
            curr_index++;
            if (curr_index == list.size())
            {
                curr_index = 0;
            }
            curr_image = new Image("ColoringBook/" + list.get(curr_index) + ".png",600,500,true,true);
            context.drawImage(curr_image,0,0);
        });
        
        // Add all of the application contents once done
        root.setCenter(box);
        ButtonCol1.setAlignment(Pos.CENTER);
        box.getChildren().addAll(canvas, ButtonCol1, ButtonRow1);
        
        // Top menu
        MenuBar menu = new MenuBar();
        root.setTop(menu);
        // File menu
        Menu fileMenu = new Menu("File");
        menu.getMenus().add(fileMenu);
        // Menu Item - clear
        MenuItem newItem = new MenuItem("Clear...");
        newItem.setGraphic(new ImageView(new Image("icons/page.png")));
        newItem.setOnAction((event)->
        {
            context.drawImage(curr_image,0,0);
        });
        fileMenu.getItems().add(newItem);
        // Menu item - save
        MenuItem saveItem = new MenuItem("Save...");
        saveItem.setGraphic(new ImageView(new Image("icons/disk.png")));
        fileMenu.getItems().add(saveItem);
        saveItem.setOnAction((event)->
        {
            FileChooser saveChooser = new FileChooser();
            ExtensionFilter filter = new ExtensionFilter("Image Files ", "*png");
            saveChooser.getExtensionFilters().add(filter);
            File saveFile = saveChooser.showSaveDialog(mainStage);
            try
            {
                Image tempImage = canvas.snapshot(null,null);
                BufferedImage buffImage = SwingFXUtils.fromFXImage(tempImage,null);
                ImageIO.write(buffImage, "png", saveFile);
                 
            }
            catch (Exception error){
                error.printStackTrace();
            }
        }); 
        // Menu Item - quit
        MenuItem quitItem = new MenuItem("Quit...");
        quitItem.setGraphic(new ImageView(new Image("icons/door_out.png")));
        fileMenu.getItems().add(quitItem);
        quitItem.setOnAction((event)->{
            System.exit(0);
        });
        
        // after adding all content, make the Stage visible
        mainStage.show();
	mainStage.sizeToScene();
	
	
    }
}
