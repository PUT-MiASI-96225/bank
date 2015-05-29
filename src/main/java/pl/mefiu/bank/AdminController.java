package pl.mefiu.bank;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

public final class AdminController implements Initializable, IChangeableView {

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        isAdmin = false;
        emps = FXCollections.observableArrayList();
        tableView.setItems(emps);
        datePicker.setValue(LocalDate.now());
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

    public IBank getIbank() {
        return ibank;
    }

    public void setIbank(IBank ibank) {
        if(ibank != null) {
            this.ibank = ibank;
        } else {
            throw new IllegalArgumentException("ibank cannot be null!");
        }
        getEmployees();
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        if(employee == null) {
            throw new IllegalArgumentException("employee cannot be null!");
        } else {
            this.employee = employee;
        }
    }

    public void handleOrdinaryEmployee(ActionEvent actionEvent) {
        this.isAdmin = false;
    }

    public void handleAdmin(ActionEvent actionEvent) {
        this.isAdmin = true;
    }

    public void handleAddEmployee(ActionEvent actionEvent) throws ParseException {
        LocalDate localDate = datePicker.getValue();
        Instant instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()));
        Date date = Date.from(instant);
        if(isAdmin) {
            ibank.createAdminEmployee(textField.getText(), date);
        } else {
            ibank.createOrdinaryEmployee(textField.getText(), date);
        }
        getEmployees();
    }

    public void handleUpdateEmployee(ActionEvent actionEvent) {
        Emp emp = (Emp) tableView.getSelectionModel().getSelectedItem();
        if(emp != null) {
            Dialog<Map<String, Object>> dialog = new Dialog<>();
            dialog.setTitle("Update");
            dialog.setHeaderText("Update Employee");
            dialog.setGraphic(new ImageView(this.getClass().getResource("/update.png").toString()));
            ButtonType updateButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(updateButtonType, ButtonType.CANCEL);
            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(20, 150, 10, 10));
            TextField username = new TextField();
            username.setPromptText("Name");
            DatePicker expiresAt = new DatePicker();
            expiresAt.setPromptText("yyyy/MM/dd");
            expiresAt.setValue(LocalDate.now());
            grid.add(new Label("Employee name:"), 0, 0);
            grid.add(username, 1, 0);
            grid.add(new Label("Expires at:"), 0, 1);
            grid.add(expiresAt, 1, 1);
            dialog.getDialogPane().setContent(grid);
            Platform.runLater(() -> username.requestFocus());
            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == updateButtonType) {
                    LocalDate localDate = datePicker.getValue();
                    Map<String, Object> map = new HashMap<>();
                    map.put("username", username.getText());
                    map.put("expiresAt", localDate);
                    return map;
                }
                return null;
            });
            Optional<Map<String, Object>> result = dialog.showAndWait();
            result.ifPresent(employee -> {
                String name = (String) employee.get("username");
                LocalDate localDate = (LocalDate) employee.get("expiresAt");
                Instant instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()));
                Date date = Date.from(instant);
                Employee e = emp.getEmployee();
                e.setName(name);
                e.setExpiresAt(date);
                ibank.updateEmployee(e);
                getEmployees();
            });
        }
    }

    public void handleRemoveEmployee(ActionEvent actionEvent) {
        Emp emp = (Emp) tableView.getSelectionModel().getSelectedItem();
        if(emp != null) {
            ibank.deleteEmployee(emp.getEmployee());
            getEmployees();
        }
    }

    public void handleLogout(ActionEvent actionEvent) throws IOException {
        if(getStage() != null) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/LoginView.fxml"));
            Parent root = fxmlLoader.load();
            Initializable controller = fxmlLoader.getController();
            ((IChangeableView) controller).setStage(stage);
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

    private void getEmployees() {
        emps.clear();
        getIbank().fetchAllEmployees().stream().forEach((employee) -> {
            emps.add(new Emp(employee));
        });
        tableView.setItems(emps);
    }

    @FXML
    private DatePicker datePicker;

    @FXML
    private TextField textField;

    @FXML
    private TableView tableView;

    private boolean isAdmin;

    private Employee employee;

    private Stage stage;

    private IBank ibank;

    private ObservableList<Emp> emps;

    public final class Emp {

        public Employee getEmployee() {
            return employee;
        }

        public void setEmployee(Employee employee) {
            if(employee != null) {
                this.employee = employee;
            } else {
                throw new IllegalArgumentException("employee cannot be null!");
            }
            DateFormat dateFormat;
            String datestring;
            dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            datestring = dateFormat.format(employee.getCreatedAt());
            setCreatedAt(datestring);
            datestring = dateFormat.format(employee.getExpiresAt());
            setExpiresAt(datestring);
            setAccessCode(employee.getAccessCode());
            setRole(String.valueOf(employee.getEmployeeRole()));
            setName(employee.getName());
        }

        public String getName() {
            return name.get();
        }

        public SimpleStringProperty nameProperty() {
            return name;
        }

        public void setName(String name) {
            this.name.set(name);
        }

        public String getAccessCode() {
            return accessCode.get();
        }

        public SimpleStringProperty accessCodeProperty() {
            return accessCode;
        }

        public void setAccessCode(String accessCode) {
            this.accessCode.set(accessCode);
        }

        public String getRole() {
            return role.get();
        }

        public SimpleStringProperty roleProperty() {
            return role;
        }

        public void setRole(String role) {
            this.role.set(role);
        }

        public String getCreatedAt() {
            return createdAt.get();
        }

        public SimpleStringProperty createdAtProperty() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt.set(createdAt);
        }

        public String getExpiresAt() {
            return expiresAt.get();
        }

        public SimpleStringProperty expiresAtProperty() {
            return expiresAt;
        }

        public void setExpiresAt(String expiresAt) {
            this.expiresAt.set(expiresAt);
        }

        public Emp() {

        }

        public Emp(Employee employee) {
            setEmployee(employee);
        }

        private Employee employee;

        private final SimpleStringProperty name = new SimpleStringProperty();

        private final SimpleStringProperty accessCode = new SimpleStringProperty();

        private final SimpleStringProperty role = new SimpleStringProperty();

        private final SimpleStringProperty createdAt = new SimpleStringProperty();

        private final SimpleStringProperty expiresAt = new SimpleStringProperty();

    }

}