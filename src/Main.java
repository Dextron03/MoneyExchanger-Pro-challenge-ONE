import com.dev.modules.CurrencyConverter;
import com.dev.modules.Menu;

import java.io.IOException;


public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        Menu myMenu = new Menu();
        myMenu.mainMenu();
        CurrencyConverter myConvert = new CurrencyConverter(
                "DOP",
                "USD",
                40);


        System.out.println(myConvert.convertCurrency());

    }
}
