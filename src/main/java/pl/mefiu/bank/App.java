package pl.mefiu.bank;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

public final class App extends Application {

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        String tooltipText = createAdminUser();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/LoginView.fxml"));
        Parent root = fxmlLoader.load();
        LoginController loginController = fxmlLoader.getController();
        loginController.setStage(primaryStage);
        loginController.setTooltipText(tooltipText);
        Scene scene = new Scene(root, 1400, 800);
        primaryStage.setTitle("Banking System Example App by m3fiu & piterson");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private static String createAdminUser() {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.YEAR, 2);
        Date newDate = calendar.getTime();
        String ret = "";
        Employee employee;
        employee = EntitiesFactory.getInstance().createAdminEmployee("admin", newDate, HibernateUtil.getWbkSession());
        ret += "For WBK: " + employee.getAccessCode();
        employee = EntitiesFactory.getInstance().createAdminEmployee("admin", newDate, HibernateUtil.getPkoSession());
        ret += ", for PKO: " + employee.getAccessCode() + ", user: admin";
        return ret;
    }

}
