package GUI; /**
 * Sample Skeleton for 'AddReservation.fxml' Controller Class
 */

import java.net.URL;
import java.util.IllegalFormatCodePointException;
import java.util.ResourceBundle;

import Domain.Car;
import Domain.CarReservation;
import Domain.Person;
import Service.Service;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class AddReservationController {

    Service service;
    Car car;
    public AddReservationController(Service service, Car car)
    {
        this.service = service;
        this.car = car;
    }
    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;


    @FXML // fx:id="AddReservationConfirmBtn"
    private Button AddReservationConfirmBtn; // Value injected by FXMLLoader

    @FXML // fx:id="AddReservationFamilyText"
    private TextField AddReservationFamilyText; // Value injected by FXMLLoader

    @FXML // fx:id="AddReservationFirstText"
    private TextField AddReservationFirstText; // Value injected by FXMLLoader

    @FXML // fx:id="AddReservationIdText"
    private TextField AddReservationIdText; // Value injected by FXMLLoader

    @FXML // fx:id="AddReservationSelectedCar"
    private TextField AddReservationSelectedCar; // Value injected by FXMLLoader

    @FXML // fx:id="AddReservationStatus"
    private TextField AddReservationStatus; // Value injected by FXMLLoader

    @FXML
    void AddReservationConfirmBtnReleased(MouseEvent event) {

        String familyName = AddReservationFamilyText.getText();
        String firstName = AddReservationFirstText.getText();
        String id = AddReservationIdText.getText();

        if (familyName.isEmpty() || firstName.isEmpty() || id.isEmpty())
            AddReservationStatus.setText("Fill all fields!");
        else {
            service.makeReservation(car.getId(), new Person(familyName, firstName, id));


            Stage stage = (Stage) AddReservationConfirmBtn.getScene().getWindow();
            stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
            stage.close();
        }


    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert AddReservationConfirmBtn != null : "fx:id=\"AddReservationConfirmBtn\" was not injected: check your FXML file 'AddReservation.fxml'.";
        assert AddReservationFamilyText != null : "fx:id=\"AddReservationFamilyText\" was not injected: check your FXML file 'AddReservation.fxml'.";
        assert AddReservationFirstText != null : "fx:id=\"AddReservationFirstText\" was not injected: check your FXML file 'AddReservation.fxml'.";
        assert AddReservationIdText != null : "fx:id=\"AddReservationIdText\" was not injected: check your FXML file 'AddReservation.fxml'.";
        assert AddReservationSelectedCar != null : "fx:id=\"AddReservationSelectedCar\" was not injected: check your FXML file 'AddReservation.fxml'.";
        assert AddReservationStatus != null : "fx:id=\"AddReservationStatus\" was not injected: check your FXML file 'AddReservation.fxml'.";

        AddReservationSelectedCar.setText(car.toString());
    }

}
