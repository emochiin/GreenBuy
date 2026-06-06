import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Demo {
    DemoService demoService = new DemoService();

    public void start() {
        while (true) {
            IO.println("Welcome to GreenBuy");
            IO.println("1. Produkte im Shop anzeigen");
            IO.println("2. Benutzereinstellungen");
            IO.println("3. Katalog-Einstellungen");
            IO.println("4. Export-Service");
            IO.println("5. Beenden");
            int eingabe = demoService.validateInteger(1, 5);
            if (eingabe == 5) {
                IO.println("Anwendung beenden...");
                break;
            }
            demoService.menuNavigate(eingabe);
        }
    }
}