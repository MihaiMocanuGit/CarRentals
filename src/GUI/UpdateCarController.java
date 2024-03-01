package GUI; /**
 * Sample Skeleton for 'AddCar.fxml' Controller Class
 */

import java.net.URL;
import java.util.ResourceBundle;

import Domain.Car;
import Service.Service;
import Utils.Primitives;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class UpdateCarController {
    Service service;
    Car car;
    public UpdateCarController(Service service, Car car)
    {
        this.service = service;
        this.car = car;
    }

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="AddCarConfirmButton"
    private Button AddCarConfirmButton; // Value injected by FXMLLoader

    @FXML // fx:id="CarAddBrandText"
    private TextField CarAddBrandText; // Value injected by FXMLLoader

    @FXML // fx:id="CarAddCostText"
    private TextField CarAddCostText; // Value injected by FXMLLoader

    @FXML // fx:id="CarAddMileageText"
    private TextField CarAddMileageText; // Value injected by FXMLLoader

    @FXML // fx:id="CarAddModelText"
    private TextField CarAddModelText; // Value injected by FXMLLoader

    @FXML // fx:id="CarAddYearText"
    private TextField CarAddYearText; // Value injected by FXMLLoader

    @FXML
    private TextField CarAddStatus;

    @FXML
    void AddCarConfirmButtonReleased(MouseEvent event) {
        String brand = CarAddBrandText.getText();
        String model = CarAddModelText.getText();
        String yearStr = CarAddYearText.getText();
        String mileageStr = CarAddMileageText.getText();
        String costStr = CarAddCostText.getText();

        if (brand.isEmpty() || model.isEmpty() || yearStr.isEmpty() || mileageStr.isEmpty() || costStr.isEmpty())
            CarAddStatus.setText("Fill all fields");
        else {
            int year;
            try{
                year = Integer.parseInt(yearStr);
            }catch (Exception e)
            {
                CarAddStatus.setText("Cannot convert year into integer");
                return;
            }

            float mileage;
            try{
                mileage = Float.parseFloat(mileageStr);
            }catch (Exception e)
            {
                CarAddStatus.setText("Cannot convert mileage into float");
                return;
            }

            float cost;
            try{
                cost = Float.parseFloat(costStr);
            }catch (Exception e)
            {
                CarAddStatus.setText("Cannot convert cost into float");
                return;
            }

            service.updateCar(car.getId(), new Primitives.PrimCar(car.getIsReserved(), brand, model, year, mileage, cost));
            Stage stage = (Stage) AddCarConfirmButton.getScene().getWindow();

            stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
            stage.close();

        }
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert AddCarConfirmButton != null : "fx:id=\"AddCarConfirmButton\" was not injected: check your FXML file 'AddCar.fxml'.";
        assert CarAddBrandText != null : "fx:id=\"CarAddBrandText\" was not injected: check your FXML file 'AddCar.fxml'.";
        assert CarAddCostText != null : "fx:id=\"CarAddCostText\" was not injected: check your FXML file 'AddCar.fxml'.";
        assert CarAddMileageText != null : "fx:id=\"CarAddMileageText\" was not injected: check your FXML file 'AddCar.fxml'.";
        assert CarAddModelText != null : "fx:id=\"CarAddModelText\" was not injected: check your FXML file 'AddCar.fxml'.";
        assert CarAddYearText != null : "fx:id=\"CarAddYearText\" was not injected: check your FXML file 'AddCar.fxml'.";
        assert CarAddStatus != null : "fx:id=\"CarAddStatus\" was not injected: check your FXML file 'AddCar.fxml'.";
        System.out.println("NEW WINDOW");
    }

}
