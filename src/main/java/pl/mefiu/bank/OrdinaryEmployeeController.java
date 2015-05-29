package pl.mefiu.bank;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.ResourceBundle;

public final class OrdinaryEmployeeController implements Initializable, IChangeableView {

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        custList = FXCollections.observableArrayList();
        tableView.setItems(custList);
        datePicker.setValue(LocalDate.now());
    }

    public void handleAddCustomer(ActionEvent actionEvent) {
        LocalDate localDate = datePicker.getValue();
        Instant instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()));
        Date date = Date.from(instant);
        getIbank().createCustomer(firstNameTextField.getText(), secondNameTextField.getText(), date,
                signatureTextField.getText(), pinTextField.getText(), cityTextField.getText(),
                postalCodeTextField.getText(), streetTextField.getText(), apartmentNumberTextField.getText(),
                stateTextField.getText(), emailTextField.getText(), phoneNumberTextField.getText());
        getCustomers();
    }

    public void handleRemoveCustomer(ActionEvent actionEvent) {
        Cust cust = (Cust) tableView.getSelectionModel().getSelectedItem();
        if(cust != null) {
            ibank.deleteCustomer(cust.getCustomer());
            getCustomers();
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
        getCustomers();
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

    private void getCustomers() {
        custList.clear();
        getIbank().fetchAllCustomers().stream().forEach((customer) -> {
            custList.add(new Cust(customer));
        });
        tableView.setItems(custList);
    }

    @FXML
    private TableView tableView;

    @FXML
    private TextField firstNameTextField;

    @FXML
    private TextField secondNameTextField;

    @FXML
    private TextField signatureTextField;

    @FXML
    private TextField pinTextField;

    @FXML
    private TextField cityTextField;

    @FXML
    private TextField postalCodeTextField;

    @FXML
    private TextField streetTextField;

    @FXML
    private TextField apartmentNumberTextField;

    @FXML
    private TextField stateTextField;

    @FXML
    private TextField emailTextField;

    @FXML
    private TextField phoneNumberTextField;

    @FXML
    private DatePicker datePicker;

    private IBank ibank;

    private Employee employee;

    private Stage stage;

    private ObservableList<Cust> custList;

    public final class Cust {

        private SimpleStringProperty fName = new SimpleStringProperty();

        private SimpleStringProperty sName = new SimpleStringProperty();

        private SimpleStringProperty date = new SimpleStringProperty();

        private SimpleStringProperty sig = new SimpleStringProperty();

        private SimpleStringProperty pin = new SimpleStringProperty();

        private SimpleStringProperty pass = new SimpleStringProperty();

        private SimpleStringProperty email = new SimpleStringProperty();

        private SimpleStringProperty phone = new SimpleStringProperty();

        private SimpleStringProperty city = new SimpleStringProperty();

        private SimpleStringProperty zip = new SimpleStringProperty();

        private SimpleStringProperty street = new SimpleStringProperty();

        private SimpleStringProperty apartment = new SimpleStringProperty();

        private SimpleStringProperty state = new SimpleStringProperty();

        private Customer customer;

        public String getfName() {
            return fName.get();
        }

        public SimpleStringProperty fNameProperty() {
            return fName;
        }

        public void setfName(String fName) {
            this.fName.set(fName);
            getCustomer().setFirstName(fName);
        }

        public String getsName() {
            return sName.get();
        }

        public SimpleStringProperty sNameProperty() {
            return sName;
        }

        public void setsName(String sName) {
            this.sName.set(sName);
            getCustomer().setSecondName(sName);
        }

        public String getDate() {
            return date.get();
        }

        public SimpleStringProperty dateProperty() {
            return date;
        }

        public void setDate(String date) {
            this.date.set(date);
        }

        public String getSig() {
            return sig.get();
        }

        public SimpleStringProperty sigProperty() {
            return sig;
        }

        public void setSig(String sig) {
            this.sig.set(sig);
            getCustomer().setSignature(sig);
        }

        public String getPin() {
            return pin.get();
        }

        public SimpleStringProperty pinProperty() {
            return pin;
        }

        public void setPin(String pin) {
            this.pin.set(pin);
            getCustomer().setPin(pin);
        }

        public String getPass() {
            return pass.get();
        }

        public SimpleStringProperty passProperty() {
            return pass;
        }

        public void setPass(String pass) {
            this.pass.set(pass);
            getCustomer().setPassword(pass);
        }

        public String getEmail() {
            return email.get();
        }

        public SimpleStringProperty emailProperty() {
            return email;
        }

        public void setEmail(String email) {
            this.email.set(email);
            Contact c = getCustomer().getContact();
            c.setEmail(email);
            getCustomer().setContact(c);
        }

        public String getPhone() {
            return phone.get();
        }

        public SimpleStringProperty phoneProperty() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone.set(phone);
            Contact c = getCustomer().getContact();
            c.setPhoneNumber(phone);
            getCustomer().setContact(c);
        }

        public String getCity() {
            return city.get();
        }

        public SimpleStringProperty cityProperty() {
            return city;
        }

        public void setCity(String city) {
            this.city.set(city);
            Address a = getCustomer().getAddress();
            a.setCity(city);
            getCustomer().setAddress(a);
        }

        public String getZip() {
            return zip.get();
        }

        public SimpleStringProperty zipProperty() {
            return zip;
        }

        public void setZip(String zip) {
            this.zip.set(zip);
            Address a = getCustomer().getAddress();
            a.setPostalCode(zip);
            getCustomer().setAddress(a);
        }

        public String getStreet() {
            return street.get();
        }

        public SimpleStringProperty streetProperty() {
            return street;
        }

        public void setStreet(String street) {
            this.street.set(street);
            Address a = getCustomer().getAddress();
            a.setStreet(street);
            getCustomer().setAddress(a);
        }

        public String getApartment() {
            return apartment.get();
        }

        public SimpleStringProperty apartmentProperty() {
            return apartment;
        }

        public void setApartment(String apartment) {
            this.apartment.set(apartment);
            Address a = getCustomer().getAddress();
            a.setApartmentNumber(apartment);
            getCustomer().setAddress(a);
        }

        public String getState() {
            return state.get();
        }

        public SimpleStringProperty stateProperty() {
            return state;
        }

        public void setState(String state) {
            this.state.set(state);
            Address a = getCustomer().getAddress();
            a.setState(state);
            getCustomer().setAddress(a);
        }

        public Customer getCustomer() {
            return customer;
        }

        public void setCustomer(Customer customer) {
            if(customer != null) {
                this.customer = customer;
            } else {
                throw new IllegalArgumentException("customer cannot be null!");
            }
            DateFormat dateFormat;
            String datestring;
            dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            datestring = dateFormat.format(getCustomer().getDateOfBirth());
            setDate(datestring);
            setApartment(customer.getAddress().getApartmentNumber());
            setState(customer.getAddress().getState());
            setStreet(customer.getAddress().getStreet());
            setCity(customer.getAddress().getCity());
            setZip(customer.getAddress().getPostalCode());
            setEmail(customer.getContact().getEmail());
            setPhone(customer.getContact().getPhoneNumber());
            setfName(customer.getFirstName());
            setsName(customer.getSecondName());
            setPass(customer.getPassword());
            setPin(customer.getPin());
            setSig(customer.getSignature());
        }

        public Cust() {

        }

        public Cust(Customer customer) {
            setCustomer(customer);
        }

    }

}