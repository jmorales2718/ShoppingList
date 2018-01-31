package jmmacbook.android.shoppinglist.data;

import android.widget.Spinner;

import com.orm.SugarRecord;

import java.io.Serializable;

import jmmacbook.android.shoppinglist.R;

/**
 * Created by jmmacbook on 4/5/16.
 *
 * Defines the Item data that will be stored in the SugarORM database for persistent storage
 */
public class Item
        extends SugarRecord
        implements Serializable
{
    private String name;
    private String price;
    private String description;
    private boolean purchased;
    private int picIcon;

    public Item() {}

    public Item(Item item)
    {
        this.name = item.getName();
        this.price = item.getPrice();
        this.purchased = item.isPurchased();
        this.description = item.getDescription();
        this.picIcon = item.getPicIcon();
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getPrice()

    {
        return price;
    }

    public void setPrice(String price)
    {

        this.price = prependDollarSign(price);
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public boolean isPurchased()
    {
        return purchased;
    }

    public void setPurchased(boolean purchased)
    {
        this.purchased = purchased;
    }

    public int getPicIcon()
    {
        return picIcon;
    }

    // Uses spinner to set the icon for an item based on category
    public void setPicIcon(Spinner spinner)
    {
        String spinnerContent = spinner.getSelectedItem().toString();

        if(spinnerContent.equals("Groceries"))
        {
            picIcon = R.drawable.groceries_icon;
        }
        else if(spinnerContent.equals("Books"))
        {
            picIcon = R.drawable.books_icon;
        }
        else if(spinnerContent.equals("Technology"))
        {
            picIcon = R.drawable.electronics_icon;
        }
        else if(spinnerContent.equals("Travel"))
        {
            picIcon = R.drawable.travel_icon;
        }
        else if(spinnerContent.equals("Clothes"))
        {
            picIcon = R.drawable.clothes_icon;
        }
    }

    public void setPicIconFromID(int picIconID){
        picIcon = picIcon;
    }

    // Converts a given pic icon id into the corresponding selected spinner item
    public int picIconToSpinnerNumber(int resID)
    {
        if(resID == R.drawable.groceries_icon)
        {
            return 0;
        }
        else if(resID == R.drawable.books_icon)
        {
            return 1;
        }
        else if(resID == R.drawable.electronics_icon)
        {
            return 2;
        }
        else if(resID == R.drawable.travel_icon)
        {
            return 3;
        }
        else if(resID == R.drawable.clothes_icon)
        {
            return 4;
        }
        return -1;
    }

    private String prependDollarSign(String price)
    {
        if(price.charAt(0) != '$')
        {
            return "$" + price;
        }
        return price;
    }

    public String deleteDollarSign(String price)
    {
        if(price.charAt(0) == '$')
        {
            return price.substring(1);
        }
        return price;
    }

}
