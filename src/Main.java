import com.business.logic.modules.CurrencyConverter;
import com.business.logic.modules.Menu;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        Menu myMenu = new Menu();
        Scanner theUserBoard = new Scanner(System.in);

        myMenu.mainMenu();
        String firstCurrency = theUserBoard.nextLine().toUpperCase();

        myMenu.currencyToConvert(firstCurrency);
        String secondCurrency = theUserBoard.nextLine().toUpperCase();

        System.out.println(myMenu.quantityMenu(secondCurrency));
        double amount = theUserBoard.nextInt();

        CurrencyConverter myConverter = new CurrencyConverter(
                firstCurrency,
                secondCurrency,
                amount
        );

        double conversionResult = myConverter.convertCurrency();

        System.out.println("El resultado de convertir " + amount + " " + firstCurrency + " a " + secondCurrency + " es: " + conversionResult);
    }
}
