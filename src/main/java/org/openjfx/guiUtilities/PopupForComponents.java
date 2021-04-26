package org.openjfx.guiUtilities;

import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.openjfx.App;
import org.openjfx.controllers.PopupCompareComponentsController;
import org.openjfx.controllers.PopupEditComponentController;
import org.openjfx.controllers.PopupShowComponentController;
import org.openjfx.dataModels.PCComponents;

public class PopupForComponents {

    /** Opens a popup window to edit components */
    public static void editComponent(PCComponents componentToEdit, TableView<PCComponents> tableview, int index) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("popupEditComponent.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();

            // Get the controller of another fxml to access it's methods
            PopupEditComponentController controller = fxmlLoader.getController();
            // Send the component to edit
            controller.setComponentToEdit(componentToEdit);
            // Listen to a click on the update button
            controller.getUpdateComponentBtn().setOnAction(event -> {
                // Whenever update button is clicked, call the updateComponent() method
                controller.updateComponent();
                // Update the changes to the tableview
                tableview.getItems().set(index, controller.getUpdatedComponent());
                // Close the popup window
                stage.close();
            });

            // Opens the popup window
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.setTitle("Edit Components");
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** Opens a popup window to show component details */
    public static void showComponent(PCComponents componentToShow) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("popupShowComponent.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();

            // Get the controller of another fxml to access it's methods
            PopupShowComponentController controller = fxmlLoader.getController();
            // Send the component to edit
            controller.setComponentToShow(componentToShow);

            // Opens the popup window
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.setTitle("Component Details");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** Opens a popup window to show components to compare */
    public static void compareComponent(ObservableList<PCComponents> components){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("popupCompareComponents.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();

            // Get the controller of another fxml to access it's methods
            PopupCompareComponentsController controller = fxmlLoader.getController();
            // Load components to compare
            for(PCComponents component: components) controller.loadComponentsToCompare(component);
            // Add to hBox as its children
            controller.compareComponents();

            // Opens the popup window
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.setResizable(true);
            stage.setTitle("Compare Components");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
