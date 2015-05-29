package pl.mefiu.bank;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.hibernate.Session;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public final class LoginController implements Initializable, IChangeableView {

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        viewsControllers.put("CUSTOMER", "/CustomerView.fxml");
        viewsControllers.put(String.valueOf(Employee.EmployeeRole.ADMIN), "/AdminView.fxml");
        viewsControllers.put(String.valueOf(Employee.EmployeeRole.ORDINARY), "/OrdinaryEmployeeView.fxml");
        final List<String> banks = new ArrayList<>();
        iBankMediator = new ElixirBankMediator();
        IBank iBankWbk = new Wbk(iBankMediator);
        IBank iBankPko = new Pko(iBankMediator);
        ((ElixirBankMediator) iBankMediator).getBanks().forEach((s, iBank) -> banks.add(s));
        ObservableList<String> observableBanks = FXCollections.observableList(banks);
        bankComboBox.getItems().clear();
        bankComboBox.setItems(observableBanks);
        bankComboBox.setValue(observableBanks.get(0));
        final List<String> roles = new ArrayList<>();
        viewsControllers.forEach((role, view) -> roles.add(role));
        ObservableList<String> observableRoles = FXCollections.observableList(roles);
        roleComboBox.getItems().clear();
        roleComboBox.setItems(observableRoles);
        roleComboBox.setValue(observableRoles.get(0));
    }

    @FXML
    public void handleSubmitButtonAction(ActionEvent actionEvent) throws IOException {
        if(getStage() != null) {
            String bankName = (String) bankComboBox.getValue();
            IBank iBank = ((ElixirBankMediator) iBankMediator).getBanks().get(bankName);
            String role = roleComboBox.getSelectionModel().getSelectedItem().toString();
            String pinName = pinNameField.getText();
            String password = passwordField.getText();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(viewsControllers.get(role)));
            Parent root = fxmlLoader.load();
            Initializable controller = fxmlLoader.getController();
            ((IChangeableView) controller).setStage(stage);
            if(controller instanceof CustomerController) {
                Customer customer = iBank.customerByPin(pinName);
                if(customer != null) {
                    if(customer.getPassword().equals(password)) {
                        ((CustomerController) controller).setCustomer(customer);
                    } else {
                        showPasswordError();
                        return;
                    }
                } else {
                    showUserError();
                    return;
                }
            } else if(controller instanceof  AdminController) {
                Employee employee = iBank.employeeByName(pinName);
                if((employee != null) && (employee.getEmployeeRole() == Employee.EmployeeRole.ADMIN)) {
                    if(employee.getAccessCode().equals(password)) {
                        ((AdminController) controller).setEmployee(employee);
                    } else {
                        showPasswordError();
                        return;
                    }
                } else {
                    showUserError();
                    return;
                }
            } else {
                Employee employee = iBank.employeeByName(pinName);
                if((employee != null) && (employee.getEmployeeRole() == Employee.EmployeeRole.ORDINARY)) {
                    if(employee.getAccessCode().equals(password)) {
                        ((OrdinaryEmployeeController) controller).setEmployee(employee);
                    } else {
                        showPasswordError();
                        return;
                    }
                } else {
                    showUserError();
                    return;
                }
            }
            if(controller instanceof CustomerController) {
                ((CustomerController) controller).setActiveIBank(iBank);
                ((CustomerController) controller).setiBankMediator(iBankMediator);
            } else if(controller instanceof AdminController) {
                ((AdminController) controller).setIbank(iBank);
            } else {
                ((OrdinaryEmployeeController) controller).setIbank(iBank);
            }
            Scene scene = new Scene(root, 1400, 800);
            stage.setScene(scene);
            stage.show();
        } else {
            throw new IllegalArgumentException("stage cannot be null!");
        }
    }

    public void handleExit(ActionEvent actionEvent) {
        Platform.exit();
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        if(stage != null) {
            this.stage = stage;
        } else {
            throw new IllegalArgumentException("stage cannot be null!");
        }
    }

    public String getTooltipText() {
        return tooltipText;
    }

    public void setTooltipText(String tooltipText) {
        if((tooltipText != null) && (!tooltipText.isEmpty())) {
            this.tooltipText = tooltipText;
        } else {
            throw new IllegalArgumentException("tooltipText cannot be empty/null!");
        }
    }

    private void showUserError() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error!");
        alert.setHeaderText("There is no such user!");
        alert.setContentText("Please try again!");
        alert.showAndWait();
    }

    private void showPasswordError() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error!");
        alert.setHeaderText("Invalid password!");
        alert.setContentText("Please try again!");
        alert.showAndWait();
    }

    private Session getPkoSession() {
        return HibernateUtil.getPkoSession();
    }

    private Session getWbkSession() {
        return HibernateUtil.getWbkSession();
    }

    private Stage stage;

    final Map<String, String> viewsControllers = new Hashtable<>();

    private IBankMediator iBankMediator;

    @FXML
    private TextField pinNameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private ComboBox bankComboBox;

    @FXML
    private ComboBox roleComboBox;

    private String tooltipText;

}
