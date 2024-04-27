import com.business.logic.modules.CurrencyConverter;
import com.business.logic.modules.Menu;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        Menu myMenu = new Menu();
        Scanner userInput = new Scanner(System.in);

        while (true) {
            try {
                myMenu.showCurrencyMenu(); // Mostrar el menú de monedas
                String firstCurrency = userInput.nextLine().toUpperCase();

                if (firstCurrency.equals("SALIR")) {
                    System.out.println("Gracias por usar nuestros servicios.");
                    break;
                }

                myMenu.showConversionOptions(firstCurrency); // Mostrar opciones de conversión
                String secondCurrency = userInput.nextLine().toUpperCase();

                System.out.println(myMenu.askForAmount(secondCurrency));
                double amount;
                try {
                    amount = userInput.nextDouble();
                    userInput.nextLine(); // Limpiar el buffer
                } catch (InputMismatchException e) {
                    System.out.println("Entrada no válida. Por favor, introduce un número.");
                    userInput.nextLine(); // Limpiar el buffer y seguir el bucle
                    continue;
                }

                if (amount <= 0) {
                    System.out.println("La cantidad debe ser mayor que cero.");
                    continue;
                }

                CurrencyConverter myConverter = new CurrencyConverter(
                        firstCurrency,
                        secondCurrency,
                        amount
                );

                double conversionResult = myConverter.convertCurrency();

                System.out.println("El resultado de convertir " + amount + " " + firstCurrency + " a " + secondCurrency + " es: " + conversionResult);

            } catch (IOException | InterruptedException e) {
                System.out.println("Se produjo un error durante la conversión: " + e.getMessage());
                break; // Salir si ocurre un error grave
            }
        }

        userInput.close(); // Cerrar el scanner al final
    }
}
