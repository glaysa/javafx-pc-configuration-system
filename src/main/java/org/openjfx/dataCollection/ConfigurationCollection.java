package org.openjfx.dataCollection;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.openjfx.dataModels.PCComponents;
import org.openjfx.dataModels.PCConfigurations;
import org.openjfx.dataValidator.ConfigurationValidator;
import org.openjfx.guiUtilities.Indicators;
import org.openjfx.guiUtilities.popupDialogs.PopupForComponents;

import java.util.ArrayList;

/** This class is responsible of all methods related to the configuration tableview in the customer view */

public class ConfigurationCollection {

    private static final ObservableList<PCConfigurations> configObsList = FXCollections.observableArrayList();
    private static final ObservableList<PCComponents> itemsObsList = FXCollections.observableArrayList();
    private static boolean modified = false;

    /** Adds configured PC to configObsList */
    public static void addConfiguration(PCConfigurations toAdd){
        ConfigurationValidator.validateConfiguredPC(toAdd);
        configObsList.add(toAdd);
    }

    /** Adds selected component to itemObsList */
    public static void addConfigurationItem(PCComponents toAdd) {
        ConfigurationValidator.validateConfigurationComponent(toAdd);
        itemsObsList.add(toAdd);
    }

    /** Sets the tableview */
    public static void setTableView(TableView<PCConfigurations> tableView){
        tableView.setId("configurationTableView");
        tableView.setItems(configObsList);
    }

    /** Listens to changes */
    public static void collectionOnChange(){
        configObsList.addListener((ListChangeListener<PCConfigurations>) change -> {
            modified = true;
            Indicators.updateFileStatusAtUser(true);
        });
    }

    /** Listens to changes and updates the list of selected components */
    public static void itemCollectionOnChange(VBox vBox, Label totalPrice){
        itemsObsList.addListener((ListChangeListener<PCComponents>) change -> {
            vBox.getChildren().clear();
            for (PCComponents component : itemsObsList) {
                HBox wrapper = new HBox();
                Label type = new Label(component.getComponentType() + ": ");
                Label name = new Label(component.getComponentName() + "\t");
                Label price = new Label(Double.toString(component.getComponentPrice()));
                wrapper.setSpacing(10);
                wrapper.getChildren().add(type);
                wrapper.getChildren().add(name);
                wrapper.getChildren().add(price);
                wrapper.setStyle(
                        "-fx-background-color: #033F63; " +
                        "-fx-padding: 10px 10px 10px 10px;" +
                        "-fx-text-fill: white;" +
                        "-fx-border-width: 1px;" +
                        "-fx-border-color: #7C9885");
                vBox.getChildren().add(wrapper);
                wrapper.setOnMouseClicked((e) -> showSelectedComponent(component, e));
                Indicators.showToolTip(wrapper, "Click to see component details");
            }
            totalPrice.setText(Double.toString(getTotalPrice()));
        });
    }

    /** Shows selected component from config pc */
    static void showSelectedComponent(PCComponents component, MouseEvent event){
        if(event.getClickCount() == 2) {
            PopupForComponents.showComponent(component, null, "configurationItem");
        }
    }

    /** Calculates the total price of all the items added to configure a PC in the newConfiguration view */
    public static double getTotalPrice(){
        double totalPrice = 0;
        for(PCComponents component : itemsObsList) {
            totalPrice += component.getComponentPrice();
        }
        return totalPrice;
    }

    /** Clears collections */

    public static void clearCollection(){
        configObsList.clear();
    }

    public static void clearItemCollection(){
        itemsObsList.clear();
    }

    /** Get / Set methods */

    public static ArrayList<PCComponents> getItemsArrayList(){
        return new ArrayList<>(itemsObsList);
    }

    public static ObservableList<PCComponents> getItemsObsList() {
        return itemsObsList;
    }

    public static ArrayList<Object> getConfigsArrayList(){
        return new ArrayList<>(configObsList);
    }

    public static ObservableList<PCConfigurations> getConfigObsList(){
        return configObsList;
    }

    public static boolean isModified() {
        return modified;
    }

    public static void setModified(boolean modified) {
        ConfigurationCollection.modified = modified;
    }
}
