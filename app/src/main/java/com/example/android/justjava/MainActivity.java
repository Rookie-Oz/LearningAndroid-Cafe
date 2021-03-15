/**
 * IMPORTANT: Make sure you are using the correct package name. 
 * This example uses the package name:
 * package com.example.android.JustJava
 * If you get an error when copying this code into Android studio, update it to match teh package name found
 * in the project's AndroidManifest.xml file.
 **/

package com.example.android.justjava;



import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.NumberFormat;

/**
 * This app displays an order form to order coffee.
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    int quantity=1;
    float totalPrice = 0;
    String priceMessage="", name;

    public void incrementOrder(View view){
        if(quantity>=100){
            Toast.makeText(this,"You can't order more than 100 Coffees",Toast.LENGTH_SHORT).show();
            return;
        }
        quantity += 1;
        displayQuantity(quantity);
    }
    public void decrementOrder(View view){
        if(quantity<=1){
            Toast.makeText(this,"You can't order less than 1 Coffees",Toast.LENGTH_SHORT).show();
            return;
        }
        quantity -= 1;
        displayQuantity(quantity);
    }



    //On clicking the order button
    public void submitOrder(View view) {
        //taking name input
        EditText nText = (EditText) findViewById(R.id.name_text_field);
        name = nText.getText().toString();

        //taking checkbox input for toppings
        CheckBox whipC = (CheckBox)findViewById(R.id.whip_check_box);
        CheckBox chocoC = (CheckBox)findViewById(R.id.choco_check_box);
        boolean hasWhippedCream = whipC.isChecked();
        boolean hasChocolate = chocoC.isChecked();

        //calculation for price and stuff
        float totalPrice = calculatePrice(hasWhippedCream,hasChocolate);
        String priceMessage = createOrderSummary(totalPrice,name,hasWhippedCream,hasChocolate);
        displayMessage(priceMessage);

        /*
        for resetting order summary after order is done
        resetOrderSummary();    //not built yet
        */
    }//end of SubmitOrder



    public float calculatePrice(boolean hasWhippedCream, boolean hasChocolate){
        //price of one cup of coffee
        int basePrice = 10;

        //if customer wants whipped cream
        if(hasWhippedCream){;    //for updating topping in order summary
            basePrice += 2;     //toppings price
        }
        //if customer wants chocolate
        if(hasChocolate){;    //same
            basePrice +=3; //same
        }
        totalPrice = quantity*basePrice;
        return totalPrice;
    }

    public String createOrderSummary(float price, String name, boolean hasWhippedCream, boolean hasChocolate){
        priceMessage = getString(R.string.order_summary_name,name);
        priceMessage +="\n"+getString(R.string.quantity)+": "+quantity;
        priceMessage +="\n"+getString(R.string.add_whipped_cream_question)+" "+ hasWhippedCream;
        priceMessage +="\n"+getString(R.string.add_chocolate_question)+" "+ hasChocolate;
        priceMessage +="\n"+getString(R.string.order_summary_total, NumberFormat.getCurrencyInstance().format(totalPrice));
        priceMessage +="\n"+getString(R.string.thank_you);
        return priceMessage;
    }
//    public void resetOrderSummary(){
//        priceMessage = "";
//    }


    /**
     * This method displays the given quantity value on the screen.
     */
    private void displayQuantity(int number) {
        TextView quantityTextView = (TextView) findViewById(R.id.quantity_text_view);
        quantityTextView.setText(""+number);
    }

    /**
     * This method displays the given price on the screen.
     */
    private void displayMessage(String msg) {
        TextView priceTextView = (TextView) findViewById(R.id.order_summary_text_view);
        priceTextView.setText(msg);
    }

    public void composeEmail(View view){
        Intent email = new Intent(Intent.ACTION_SENDTO);
        email.setData(Uri.parse("mailto:")); // only email apps should handle this
        email.putExtra(Intent.EXTRA_SUBJECT, "Just Java Order for "+name);
        email.putExtra(Intent.EXTRA_TEXT, priceMessage);
        if(email.resolveActivity(getPackageManager())!=null){   //checking if theres an app for handling intent
            startActivity(email);
        }
    }
}