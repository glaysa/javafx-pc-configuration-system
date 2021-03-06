package org.openjfx.controllers;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.openjfx.App;
import org.openjfx.dataCollection.ComponentsCartCollection;
import org.openjfx.dataCollection.ComponentsCollection;
import org.openjfx.dataCollection.ConfigurationCartCollection;
import org.openjfx.dataCollection.ConfigurationCollection;
import org.openjfx.dataModels.PCComponents;
import org.openjfx.dataModels.PCConfigurations;
import org.openjfx.fileUtilities.FileHandlers.FileActions;
import org.openjfx.fileUtilities.FileRestrictions;
import org.openjfx.guiUtilities.*;
import org.openjfx.guiUtilities.popupDialogs.PopupForCheckout;
import org.openjfx.guiUtilities.popupDialogs.PopupForComponents;
import org.openjfx.guiUtilities.popupDialogs.PopupForConfigurations;
import org.openjfx.guiUtilities.popupDialogs.PopupForTableView;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ControllerCustomer implements Initializable {

    @FXML private Menu menuFile;
    @FXML private TabPane tabPane;
    @FXML private Label totalPriceLabel;
    @FXML private Label filename;
    public static Label filenameStatic;
    @FXML private Label fileStatus;
    public static Label fileStatusStatic;
    @FXML private TextField searchInput;
    @FXML private TableView<PCComponents> tableViewCartComponents;
    @FXML private TableView<PCComponents> tableViewComponents;
    @FXML private TableView<PCConfigurations> tableViewCartConfigurations;
    @FXML private TableView<PCConfigurations> tableViewConfigurations;
    private final FileActions file = new FileActions();
    private final File defaultData = new File("src/main/java/database/initialComponents.txt");
    private final File defaultConfigs = new File("src/main/java/database/initialConfigurations.txt");

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Opens a file containing the default list of components.
        file.open(defaultData, "Loading products...");
        FileRestrictions.setRestrictedDataKey(null);
        // Changes tab to config tab to let user know, they saving or opening a config collection
        changeTabOnFileAction();
        // (listener) Initializes search functionality
        search();

        // (listener) Initializes detection of a change on the collections
        ComponentsCartCollection.collectionOnChange(totalPriceLabel);
        ComponentsCollection.collectionOnChange(null);
        ComponentsCollection.fillComponentTypeObsList();
        ConfigurationCartCollection.collectionOnChange(totalPriceLabel);
        ConfigurationCollection.collectionOnChange();

        // Initializes the tableviews
        ComponentsCollection.setTableView(tableViewComponents);
        ComponentsCartCollection.setTableView(tableViewCartComponents);
        ConfigurationCollection.setTableView(tableViewConfigurations);
        ConfigurationCartCollection.setTableView(tableViewCartConfigurations);
        tableViewCartComponents.setPlaceholder(new Label("Components Cart"));
        tableViewCartConfigurations.setPlaceholder(new Label("Configurations Cart"));

        // (listener) Initializes detection of double click on row of tableviews
        PopupForComponents.showComponentDetails(tableViewComponents);
        PopupForComponents.showComponentDetails(tableViewCartComponents);
        PopupForConfigurations.showConfigurationDetails(tableViewConfigurations);
        PopupForConfigurations.showConfigurationDetails(tableViewCartConfigurations);

        // Initializes tableview tooltips
        Indicators.showToolTip(tableViewComponents, "Double click to see component details");
        Indicators.showToolTip(tableViewCartComponents, "Double click to see component details");
        Indicators.showToolTip(tableViewConfigurations, "Double click to see configuration details");
        Indicators.showToolTip(tableViewCartConfigurations, "Double click to see configuration details");

        // Initializing gui variables, assigned to static vars to allow other classes to update its values
        filenameStatic = filename;
        fileStatusStatic = fileStatus;
    }

    /** Add products to cart */

    @FXML
    void addComponentToCart() {
        ObservableList<PCComponents> selected = tableViewComponents.getSelectionModel().getSelectedItems();
        if(!selected.isEmpty()) for(PCComponents toAdd : selected) ComponentsCartCollection.addToCollection(toAdd);
        else AlertDialog.showWarningDialog("Please select a component to add","");
        tableViewCartComponents.refresh();
    }

    @FXML
    void addConfigurationToCart(){
        PCConfigurations selected = tableViewConfigurations.getSelectionModel().getSelectedItem();
        if(selected != null) ConfigurationCartCollection.addToCollection(selected);
        else AlertDialog.showWarningDialog("Choose a configured PC to add", "");
        tableViewCartConfigurations.refresh();
    }

    /** Removes products from the cart */

    @FXML
    void removeFromComponentsCart() {
        PCComponents selected = tableViewCartComponents.getSelectionModel().getSelectedItem();
        ComponentsCartCollection.removeSelected(selected);
        tableViewCartComponents.refresh();
    }

    @FXML
    void removeFromConfigurationsCart() {
        PCConfigurations selected = tableViewCartConfigurations.getSelectionModel().getSelectedItem();
        ConfigurationCartCollection.removeSelected(selected);
        tableViewCartConfigurations.refresh();
    }


    /** Opens a popup to show cart summary */

    @FXML
    void checkout() {
        PopupForCheckout.showCheckout();
    }

    /** Opens a file through file chooser */

    @FXML
    void openFile() {
        // Users are not allowed to open files that contain PC Components
        FileRestrictions.setRestrictedDataKey(PCComponents.getUUID());
        FileChooser fileChooser = file.getFileChooser();
        File fileToOpen = fileChooser.showOpenDialog(new Stage());
        if(fileToOpen == null) {
            AlertDialog.showWarningDialog("No file was chosen","");
        } else {
            file.open(fileToOpen, "Opening file...");
        }
    }

    /** Saves a file through file chooser */

    @FXML
    void saveFile() {
        FileChooser fileChooser = file.getFileChooser();
        File fileToSave = fileChooser.showSaveDialog(new Stage());
        if(fileToSave == null) {
            AlertDialog.showWarningDialog("No file was chosen","");
        } else {
            file.save(ConfigurationCollection.getConfigsArrayList(), fileToSave, "Saving file...");
        }
    }

    /** Save all changes to the current opened file */

    @FXML
    void saveChanges() {
        file.saveChanges(ConfigurationCollection.getConfigsArrayList(), "Saving changes...");
    }

    /** Opens a popup window to show all components the user wants to compare */

    @FXML
    void compare(){
        ObservableList<PCComponents> selected = tableViewComponents.getSelectionModel().getSelectedItems();
        if(selected.isEmpty()) {
            AlertDialog.showWarningDialog("Choose components to compare","");
        } else if(selected.size() > 4){
            AlertDialog.showWarningDialog("You can only compare max 4 components","");
        } else if(selected.size() < 2){
            AlertDialog.showWarningDialog("You must choose at least 2 components", "");
        } else {
            PopupForComponents.compareComponent(selected);
        }
    }

    /** Opens a popup window and let's the user create their own pc configurations */

    @FXML
    void newConfiguration(){
        PopupForConfigurations.newConfiguration();
    }

    /** Opens a popup window to let the user filter the tableview */

    @FXML
    void filterTableView() {
        PopupForTableView.showFilterOptions(tableViewComponents);
    }

    /** searches through the tableview with the given search input */
    void search() {
        ComponentsCollection.collectionSearch(searchInput, tableViewComponents);
    }

    /** The tab changes to the config tab to let user know they can only save or open a config collection */
    void changeTabOnFileAction(){
        menuFile.setOnShown(event -> tabPane.getSelectionModel().selectLast());
    }

    /** Loads configuration data when user click on the configured PC tab */
    boolean tabOpened = false; // prevents from opening the file again and again when switching tabs
    @FXML
    void tabConfigurationsInit(){
        if(!tabOpened) {
            file.open(defaultConfigs, "Loading PC Configurations...");
            tabOpened = true;
        }
    }

    /** Change view to login view */

    @FXML
    void logout() throws IOException {
        // User is prompted when there are unsaved changes
        if(ConfigurationCollection.isModified()) {
            String response = AlertDialog.showConfirmDialog("Do you want to save your changes?");
            if(response.equals("Yes")) {
                file.saveChanges(ConfigurationCollection.getConfigsArrayList(), "Saving changes...");
            }
        }
        // Otherwise, the user is logged out immediately and clears all data of all tableviews
        ConfigurationCartCollection.clearCollection();
        ConfigurationCollection.clearCollection();
        ComponentsCartCollection.clearCollection();
        ComponentsCollection.clearCollection();
        ConfigurationCollection.setModified(false);
        ComponentsCollection.setModified(false);
        App.setRoot("login", "Login");
    }
}
