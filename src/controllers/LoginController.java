package controllers;

import constants.AdminEmailPassword;
import java.net.URL;
import java.util.ResourceBundle;

import controllers.student.StudentHomeFXMLController;
import exceptions.LoginException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.Student;
import org.controlsfx.control.Notifications;

public class LoginController implements Initializable {
    @FXML
    private TextField adminEmail;
    @FXML
    private PasswordField adminPassword;
    @FXML
    private Button adminLoginButton;
    @FXML
    private TextField studentEmail;
    @FXML
    private PasswordField studentPassword;
    @FXML
    private Button studentLoginButton;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Pre-fill some credentials for testing (remove for production)
        this.studentEmail.setText("dee@gmail.com");
        this.studentPassword.setText("123456789");
        this.adminEmail.setText("admin@gmail.com");
        this.adminPassword.setText("123456");
    }

    @FXML
    private void loginAdmin(ActionEvent event) {
        String email = adminEmail.getText().trim();
        String password = adminPassword.getText().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Notifications.create()
                    .title("Input Error")
                    .text("Please fill in both email and password.")
                    .showWarning();
            return;
        }

        if (email.equalsIgnoreCase(AdminEmailPassword.email) && password.equals(AdminEmailPassword.password)) {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/fxml/AdminHomeScreenFXML.fxml"));
                Stage stage = (Stage) adminLoginButton.getScene().getWindow(); // Use the admin login button
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.setMaximized(true);
                System.out.println("Admin Login Success");
            } catch (Exception e) {
                Notifications.create()
                        .title("Error")
                        .text("Failed to load admin home screen.")
                        .showError();
                e.printStackTrace();
            }
        } else {
            Notifications.create()
                    .title("Login Failed")
                    .text("Incorrect admin email or password.")
                    .showError();
            System.out.println("Admin Login Failed.");
        }
    }

    @FXML
    private void loginStudent(ActionEvent event) {
        System.out.println("controllers.AdminLoginController.loginStudent()");
        String email = studentEmail.getText().trim();
        String password = studentPassword.getText().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Notifications.create()
                    .title("Input Error")
                    .text("Please fill in both email and password.")
                    .showWarning();
            return;
        }

        Student s = new Student(email, password);
        try {
            s.login(); // Attempt to login
            System.out.println(s);

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/student/StudentHomeFXML.fxml"));
            Parent root = fxmlLoader.load();
            StudentHomeFXMLController controller = fxmlLoader.getController();
            controller.setStudent(s);
            Stage stage = (Stage) studentLoginButton.getScene().getWindow(); // Use the student login button
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setMaximized(true);
        } catch (LoginException ex) {
            Notifications.create()
                    .title("Login Failed")
                    .text("Email or password incorrect")
                    .showError();
        } catch (Exception ex) {
            Notifications.create()
                    .title("Error")
                    .text("An unexpected error occurred.")
                    .showError();
            ex.printStackTrace(); // Log unexpected errors
        }
    }
}
