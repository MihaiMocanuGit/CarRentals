package GUI;


import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.stream.StreamSupport;

import Domain.Car;
import Domain.CarReservation;
import Service.Service;
import Utils.AddExamples;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;


import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;


public class GUIController {

    private Service service;

    private void refreshCarsList()
    {
        if(!CarsMenu1Checkbox.isSelected()) {
            List<Car> cars = StreamSupport
                    .stream(service.cars().spliterator(), false)
                    .toList();

            ObservableList<Car> carObsList = FXCollections.observableList(cars);
            CarsList.setItems(carObsList);
        }
        else {
            List<Car> cars = StreamSupport
                    .stream(service.getAllAvailableCars().getAll().spliterator(), false)
                    .toList();

            ObservableList<Car> carObsList = FXCollections.observableList(cars);
            CarsList.setItems(carObsList);

            OutputLog.appendText("Showing only available cars\n");
        }
    }

    private void refreshReservationsList()
    {
        List<CarReservation> carReservations = StreamSupport
                                                .stream(service.reservations().spliterator(), false)
                                                .toList();

        ObservableList<CarReservation> reservationsObsList = FXCollections.observableList(carReservations);
        ReservationsList.setItems(reservationsObsList);
    }

    private void refreshLists()
    {
        refreshCarsList();
        refreshReservationsList();
    }
    public GUIController()
    {

        try (InputStream input = new FileInputStream("settings.properties")) {
            Properties prop = new Properties();
            prop.load(input);

            String data = prop.getProperty("data");
            String carRepoFilepath = prop.getProperty("Cars");
            String reservationRepoFilepath = prop.getProperty("Reservations");

            service =  new Service(carRepoFilepath, reservationRepoFilepath, data);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }



    @FXML
    private Button AddData;

    @FXML
    private Button CarsAdd;

    @FXML
    private ListView<Car> CarsList;

    @FXML
    private MenuButton CarsMenu;

    @FXML
    private CheckBox CarsMenu1Checkbox;

    @FXML
    private TextField CarsMenu2BrandText;

    @FXML
    private TextField CarsMenu3Price;

    @FXML
    private MenuItem CarsMenuItem1;

    @FXML
    private MenuItem CarsMenuItem2;

    @FXML
    private MenuItem CarsMenuItem3;

    @FXML
    private Button CarsRemove;

    @FXML
    private Button CarsUpdate;

    @FXML
    private Button Clear;

    @FXML
    private TextArea OutputLog;

    @FXML
    private MenuButton ReservationMenu;

    @FXML
    private Button ReservationsAdd;

    @FXML
    private ListView<CarReservation> ReservationsList;

    @FXML
    private MenuItem ReservationsMenItem1;

    @FXML
    private TextField ReservationsMenu1FamilyText;

    @FXML
    private TextField ReservationsMenu2FirstText;

    @FXML
    private MenuItem ReservationsMenuItem2;

    @FXML
    private Button ReservationsRemove;

    @FXML
    private Button ReservationsUpdate;

    @FXML
    private Button RefreshButton;

    @FXML
    void carsAddButtonReleased(MouseEvent event) {
        System.out.println("Pressed Cars Add Button");


        try {
            AddCarController controller = new AddCarController(service);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/AddCar.fxml"));
            loader.setController(controller);

            Scene scene =  new Scene(loader.load());
            Stage window = new Stage();
            window.initOwner(CarsAdd.getScene().getWindow());
            window.initModality(Modality.WINDOW_MODAL);

            window.setOnCloseRequest(new EventHandler<WindowEvent>() {
                public void handle(WindowEvent we) {
                    refreshLists();
                }
            });


            window.setTitle("Add new car");
            window.setScene(scene);


            window.show();

        } catch (IOException e) {
            OutputLog.appendText("ERROR " + e.getMessage() + "\n");
        }

    }

    @FXML
    void carsBrandChanged(KeyEvent event) {
        System.out.println("Changed car brand");

        String text = CarsMenu2BrandText.getText();
        List<Car> cars = service.getAllCarsByBrand(text);
        ObservableList<Car> carsObsList = FXCollections.observableList(cars);
        CarsList.setItems(carsObsList);

        OutputLog.appendText("Filtering cars by brands: " + CarsMenu2BrandText.getText() + "\n");

    }

    @FXML
    void carsPriceChanged(KeyEvent event) {
        System.out.println("Changed car price");

        String text = CarsMenu3Price.getText();
        try
        {
            float price = Float.parseFloat(text);

            List<Car> cars = service.getAllCarsByPriceRange(price);
            ObservableList<Car> carsObsList = FXCollections.observableList(cars);
            CarsList.setItems(carsObsList);

            OutputLog.appendText("Show only cars costing less than " + text + "\n");
        }
        catch (Exception e)
        {
            OutputLog.appendText("Could not parse: \"" + text + "\" into float\n");
        }

    }

    @FXML
    void carsRemoveButtonReleased(MouseEvent event) {
        System.out.println("Pressed Cars Remove Button, selected element: ");
        Car selectedCar =  CarsList.getSelectionModel().getSelectedItem();
        if (selectedCar != null) {
            service.removeCar(selectedCar.getId());
            refreshLists();

            OutputLog.appendText("Removed: " + selectedCar.toString() + "\n");
        }
        else{
            OutputLog.appendText("Select a car first\n");
        }
    }

    @FXML
    void carsUpdateButtonReleased(MouseEvent event) {
        System.out.println("Pressed Cars Update Button");

        try {
            Car selectedCar =  CarsList.getSelectionModel().getSelectedItem();

            if (selectedCar == null)
                OutputLog.appendText("Select a car first\n");
            else
            {
                UpdateCarController controller = new UpdateCarController(service, selectedCar);
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/AddCar.fxml"));
                loader.setController(controller);

                Scene scene =  new Scene(loader.load());
                Stage window = new Stage();
                window.initOwner(CarsUpdate.getScene().getWindow());
                window.initModality(Modality.WINDOW_MODAL);

                window.setOnCloseRequest(new EventHandler<WindowEvent>() {
                    public void handle(WindowEvent we) {
                        refreshLists();
                    }
                });


                window.setTitle("Add new car");
                window.setScene(scene);


                window.show();

            }
        } catch (IOException e) {
            OutputLog.appendText("ERROR " + e.getMessage() + "\n");
        }
    }

    @FXML
    void reservationsAddButtonReleased(MouseEvent event) {
        System.out.println("Pressed Reservations Add Button");


        try {
            Car selectedCar =  CarsList.getSelectionModel().getSelectedItem();

            if (selectedCar == null)
                OutputLog.appendText("Select a car first\n");
            else if (selectedCar.getIsReserved())
                OutputLog.appendText("Select a car that is available please\n");
            else {
                AddReservationController controller = new AddReservationController(service, selectedCar);
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/AddReservation.fxml"));
                loader.setController(controller);

                Scene scene = new Scene(loader.load());
                Stage window = new Stage();
                window.initOwner(ReservationsAdd.getScene().getWindow());
                window.initModality(Modality.WINDOW_MODAL);


                window.setOnCloseRequest(new EventHandler<WindowEvent>() {
                    public void handle(WindowEvent we) {
                        refreshLists();
                    }
                });




                window.setTitle("Add new reservation");
                window.setScene(scene);


                window.show();


            }
        } catch (IOException e) {
            OutputLog.appendText("ERROR " + e.getMessage() + "\n");
        }
    }

    @FXML
    void reservationsRemoveButtonReleased(MouseEvent event) {
        System.out.println("Pressed Reservations Remove Button");

        CarReservation selectedReservation =  ReservationsList.getSelectionModel().getSelectedItem();

        if (selectedReservation != null) {
            service.removeReservation(selectedReservation.getId());
            refreshLists();

            OutputLog.appendText("Removed: " + selectedReservation.toString() + "\n");
        }
        else {

            OutputLog.appendText("Select a car first\n");
        }
    }

    @FXML
    void reservationsUpdateButtonReleased(MouseEvent event) {
        System.out.println("Pressed Reservations Update Button");

        try {
            Car selectedCar = CarsList.getSelectionModel().getSelectedItem();
            if (selectedCar == null) {
                OutputLog.appendText("Select a car first\n");
                return;
            }

            CarReservation selectedReservation = ReservationsList.getSelectionModel().getSelectedItem();

            if (selectedReservation == null) {
                OutputLog.appendText("Select a reservation first\n");
                return;
            }

            if (selectedCar.getIsReserved() && selectedCar.getId() != selectedReservation.getId())
            {
                OutputLog.appendText("The selected car is not available\n");
                return;
            }



            UpdateReservationController controller = new UpdateReservationController(service, selectedCar, selectedReservation.getId());
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/AddReservation.fxml"));
            loader.setController(controller);

            Scene scene = new Scene(loader.load());
            Stage window = new Stage();
            window.initOwner(ReservationsUpdate.getScene().getWindow());
            window.initModality(Modality.WINDOW_MODAL);


            window.setOnCloseRequest(new EventHandler<WindowEvent>() {
                public void handle(WindowEvent we) {
                    refreshLists();
                }
            });




            window.setTitle("Update reservation");
            window.setScene(scene);


            window.show();



        }catch (Exception e)
        {
            OutputLog.appendText("ERROR " + e.getMessage() + "\n");
        }
    }

    @FXML
    void reservationsfFamilyNameChanged(KeyEvent event) {
        System.out.println("Changed Reservations family name");

        List<CarReservation> reservations = service.getAllReservationsByFamilyName(ReservationsMenu1FamilyText.getText());
        ObservableList<CarReservation> reservationsObsList = FXCollections.observableList(reservations);
        ReservationsList.setItems(reservationsObsList);

        OutputLog.appendText("Filtering family name by: " + ReservationsMenu1FamilyText.getText() + "\n");
    }

    @FXML
    void reservationsfFirstNameChanged(KeyEvent event) {
        System.out.println("Changed Reservations first name");

        List<CarReservation> reservations = service.getAllReservationsByFirstName(ReservationsMenu2FirstText.getText());
        ObservableList<CarReservation> reservationsObsList = FXCollections.observableList(reservations);
        ReservationsList.setItems(reservationsObsList);

        OutputLog.appendText("Filtering first name by: " + ReservationsMenu2FirstText.getText() + "\n");
    }

    @FXML
    void reservationsfIsReservedOnAction(ActionEvent event) {
        System.out.println("Actioned Car Checkbox");
        if (CarsMenu1Checkbox.isSelected()) {
            List<Car> cars = StreamSupport
                    .stream(service.getAllAvailableCars().getAll().spliterator(), false)
                    .toList();

            ObservableList<Car> carObsList = FXCollections.observableList(cars);
            CarsList.setItems(carObsList);

            OutputLog.appendText("Showing only available cars\n");
        }
        else {
            refreshCarsList();

            OutputLog.appendText("Showing all cars\n");
        }
    }

    @FXML
    void clearButtonReleased(MouseEvent event) {
        service.removeAll();
        refreshReservationsList();
        refreshCarsList();

        OutputLog.appendText("Cleared all data\n");
    }
    @FXML
    void dataButtonReleased(MouseEvent event) {
        AddExamples.addExamplesIntoRepos(service);
        refreshReservationsList();
        refreshCarsList();

        OutputLog.appendText("Added dummy examples\n");
    }

    @FXML
    void RefreshButtonReleased(MouseEvent event) {
        refreshLists();
    }
    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert CarsAdd != null : "fx:id=\"CarsAdd\" was not injected: check your FXML file 'GUI.fxml'.";
        assert CarsList != null : "fx:id=\"CarsList\" was not injected: check your FXML file 'GUI.fxml'.";
        assert CarsMenu != null : "fx:id=\"CarsMenu\" was not injected: check your FXML file 'GUI.fxml'.";
        assert CarsMenu1Checkbox != null : "fx:id=\"CarsMenu1Checkbox\" was not injected: check your FXML file 'GUI.fxml'.";
        assert CarsMenu2BrandText != null : "fx:id=\"CarsMenu2BrandText\" was not injected: check your FXML file 'GUI.fxml'.";
        assert CarsMenu3Price != null : "fx:id=\"CarsMenu3Price\" was not injected: check your FXML file 'GUI.fxml'.";
        assert CarsMenuItem1 != null : "fx:id=\"CarsMenuItem1\" was not injected: check your FXML file 'GUI.fxml'.";
        assert CarsMenuItem2 != null : "fx:id=\"CarsMenuItem2\" was not injected: check your FXML file 'GUI.fxml'.";
        assert CarsMenuItem3 != null : "fx:id=\"CarsMenuItem3\" was not injected: check your FXML file 'GUI.fxml'.";
        assert CarsRemove != null : "fx:id=\"CarsRemove\" was not injected: check your FXML file 'GUI.fxml'.";
        assert CarsUpdate != null : "fx:id=\"CarsUpdate\" was not injected: check your FXML file 'GUI.fxml'.";
        assert Clear != null : "fx:id=\"Clear\" was not injected: check your FXML file 'GUI.fxml'.";
        assert OutputLog != null : "fx:id=\"OutputLog\" was not injected: check your FXML file 'GUI.fxml'.";
        assert ReservationMenu != null : "fx:id=\"ReservationMenu\" was not injected: check your FXML file 'GUI.fxml'.";
        assert ReservationsAdd != null : "fx:id=\"ReservationsAdd\" was not injected: check your FXML file 'GUI.fxml'.";
        assert ReservationsList != null : "fx:id=\"ReservationsList\" was not injected: check your FXML file 'GUI.fxml'.";
        assert ReservationsMenItem1 != null : "fx:id=\"ReservationsMenItem1\" was not injected: check your FXML file 'GUI.fxml'.";
        assert ReservationsMenu1FamilyText != null : "fx:id=\"ReservationsMenu1FamilyText\" was not injected: check your FXML file 'GUI.fxml'.";
        assert ReservationsMenu2FirstText != null : "fx:id=\"ReservationsMenu2FirstText\" was not injected: check your FXML file 'GUI.fxml'.";
        assert ReservationsMenuItem2 != null : "fx:id=\"ReservationsMenuItem2\" was not injected: check your FXML file 'GUI.fxml'.";
        assert ReservationsRemove != null : "fx:id=\"ReservationsRemove\" was not injected: check your FXML file 'GUI.fxml'.";
        assert ReservationsUpdate != null : "fx:id=\"ReservationsUpdate\" was not injected: check your FXML file 'GUI.fxml'.";
        refreshCarsList();
        refreshReservationsList();
    }


}
