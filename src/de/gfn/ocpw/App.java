package de.gfn.ocpw;

import de.gfn.ocpw.entityw.Scout;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Paths;
import java.util.List;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.ArrayList;
import javafx.collections.ObservableList;

/**
 *
 * @author student
 */
public class App extends Application {
    
    private static final String SER_FILE = "data.db";
    
    private final Label lFirstname = new Label("Vorname");
    private final TextField tFirstname = new TextField();
    private final Label lLastname = new Label("Nachname");
    private final TextField tLastname = new TextField();
    private final Button bSave = new Button("Speichern");
    private final Button bDelete = new Button("Löschen");
    
    private final TableView<Scout> table = new TableView<>(); 
    private final List<Scout> scouts = new ArrayList<>();    
    
    @Override
    public void start(Stage primaryStage) {
        
        loadDataFromFile();
        
        //Scout s = new Scout();
        //scouts.add(new Scout(1, "Max", "Mustermann"));
        
        //Vlad: Assertions?
        
        defineTableCols();
        //showColumns();
        
        bSave.setOnAction((e) -> {
            //Daten validieren
            
            scouts.add(new Scout(0, tFirstname.getText(), tLastname.getText()));
            showColumns();
            clearForm();
        });
        
        bDelete.setOnAction((e) -> {
            //Daten validieren
            int pos = table.getSelectionModel().getSelectedIndex();
            scouts.remove(pos);
            showColumns();
        });
        
//        bSave.setOnAction(new EventHandler<ActionEvent>(){
//            public void handle(ActionEvent event){
//                
//            }
//        });
        
        lFirstname.getStyleClass().add("label");
        lLastname.getStyleClass().add("label");
        tFirstname.getStyleClass().add("textfield");
        tLastname.getStyleClass().add("textfield");
        bSave.getStyleClass().add("btn");
        //bSave.setStyle("-fx-min-width:80;"); //inline StyleSheet
        
        VBox form = new VBox(10,
          new HBox(0, lFirstname, tFirstname),  
          new HBox(10, lLastname, tLastname),  
          new HBox(0, bSave) 
        );
        
        VBox box = new VBox(5,
          form,
          table,
          bDelete
        );     
        
//        Button btn = new Button();
//        btn.setText("Say 'Hello World'");
//        btn.setOnAction(new EventHandler<ActionEvent>() {
//            
//            @Override
//            public void handle(ActionEvent event) {
//                System.out.println("Hello World!");
//            }
//        });
        
//        StackPane root = new StackPane();
//        root.getChildren().add(form);
//        root.getChildren().add(table);
        
        //Scene scene = new Scene(root, 300, 250);
        Scene scene = new Scene(box, 500, 500);
        
//        String css = this.getClass().getResource("double_slider.css").toExternalForm(); 
        //scene.getStylesheets().add(css);

        String css = Paths.get("styles.css").toUri().toString();
        scene.getStylesheets().add(css);
        //scene.getStylesheets().add("styles.css");
        
        
        primaryStage.setOnCloseRequest(e -> {
            saveDataToFile();
        });
        
        primaryStage.setTitle("Hello World!");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    private void showColumns() {
        ObservableList<Scout> list = FXCollections.observableArrayList(scouts);
        table.getItems().clear();
        table.setItems(list);
    }
    
    private void defineTableCols(){
        
        TableColumn<Scout, Integer> c1 = new TableColumn("ID");
        c1.setCellValueFactory(new PropertyValueFactory("id"));
        
        TableColumn<Scout, String> c2 = new TableColumn("Firstname");
        c2.setCellValueFactory(new PropertyValueFactory("firstname"));
        
        TableColumn<Scout, String> c3 = new TableColumn("Lastname");
        c3.setCellValueFactory(new PropertyValueFactory("lastname"));
        
        table.getColumns().addAll(c1, c2, c3);
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    private void loadDataFromFile() {
        //try with ressources
        try(ObjectInputStream input = new ObjectInputStream(
                new BufferedInputStream(new FileInputStream(SER_FILE)))) {
            scouts.addAll((List<Scout>)input.readObject());
            showColumns();
        }
        catch(Exception ex) {
            System.out.println(ex);
        }       
    }

    private void saveDataToFile() {
        //
        try(ObjectOutputStream output = new ObjectOutputStream(
                new BufferedOutputStream(new FileOutputStream(SER_FILE)))) {
            output.writeObject(scouts);
        }
        catch(Exception ex) {
            System.out.println(ex);
        }  
    }

    private void clearForm() {
        tFirstname.setText("");
        tLastname.setText("");
    }
    
}
