package jmmacbook.android.shoppinglist;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jmmacbook.android.shoppinglist.data.Item;


// Defines the Activity that is used to create a new item on the shopping list
public class NewItemActivity extends AppCompatActivity
{

    public static final String NEW_ITEM_KEY = "NEW_ITEM";
    public static final String EDIT_ITEM_KEY = "EDIT_ITEM";
    public static final String OLD_ITEM_KEY = "OLD_ITEM";
    private Item itemToEdit;

    @Bind(R.id.spnNewItemType)
    Spinner spnNewItemType;

    @Bind(R.id.etNewItemName)
    EditText etNewItemName;

    @Bind(R.id.etNewItemEstPrice)
    EditText etNewItemPrice;

    @Bind(R.id.etNewItemDescription)
    EditText etNewItemDescription;

    @Bind(R.id.cbNewItemPurchased)
    CheckBox cbNewItemPurchased;

    @Bind(R.id.btnAddNewItem)
    Button btnAddNewItem;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_item);

        ButterKnife.bind(this);

        ArrayAdapter<CharSequence> arrayAdapter =
                ArrayAdapter.createFromResource(this, R.array.spinnerCategories, R.layout.textview);

        arrayAdapter.setDropDownViewResource(R.layout.textview);
        spnNewItemType.setAdapter(arrayAdapter);

        Intent editItemIntent = getIntent();
        itemToEdit = (Item)editItemIntent.getSerializableExtra("ITEM_TO_EDIT");
        if(itemToEdit != null)
        {
            btnAddNewItem.setText(R.string.editItem);
            setCurrentItem(itemToEdit);
        }

    }

    @OnClick(R.id.btnAddNewItem)
    public void onAddNewItemClicked(Button btn)
    {
        if(itemToEdit == null)
        {
            addNewItem();
        }
        else
        {
            editItem();
        }
    }

    private void addNewItem()
    {
        Item newItem = new Item();
        if(!etNewItemName.getText().toString().isEmpty() &&
                !etNewItemPrice.getText().toString().isEmpty())
        {
            newItem.setName(etNewItemName.getText().toString());
            newItem.setPrice(etNewItemPrice.getText().toString());
            if(!etNewItemDescription.getText().toString().isEmpty())
            {
                newItem.setDescription(etNewItemDescription.getText().toString());
            }
            else
            {
                newItem.setDescription("Dust, Nothing to see here");
            }
            newItem.setPurchased(cbNewItemPurchased.isChecked());
            newItem.setPicIcon(spnNewItemType);

            Intent resultIntent = new Intent();
            resultIntent.putExtra(NEW_ITEM_KEY, newItem);
            setResult(Activity.RESULT_OK, resultIntent);
            finish();
        }
        else
        {
            Toast.makeText(this, "You must include a Name and Price.", Toast.LENGTH_LONG).show();
        }

    }

    private void editItem()
    {
        Item temp = new Item(itemToEdit);
        if(!etNewItemName.getText().toString().isEmpty() &&
                !etNewItemPrice.getText().toString().isEmpty())
        {
            itemToEdit.setName(etNewItemName.getText().toString());
            itemToEdit.setPrice(etNewItemPrice.getText().toString());
            if(!etNewItemDescription.getText().toString().isEmpty())
            {
                itemToEdit.setDescription(etNewItemDescription.getText().toString());
            }
            itemToEdit.setPurchased(cbNewItemPurchased.isChecked());
            itemToEdit.setPicIcon(spnNewItemType);

            Intent editedResultIntent = new Intent();
            editedResultIntent.putExtra(OLD_ITEM_KEY, temp);
            editedResultIntent.putExtra(EDIT_ITEM_KEY, itemToEdit);
            setResult(Activity.RESULT_OK, editedResultIntent);
            finish();
        }
        else
        {
            Toast.makeText(this, "You must include a Name and Price.", Toast.LENGTH_LONG).show();
        }
    }

    public void setCurrentItem(Item item)
    {
        etNewItemName.setText(item.getName());
        etNewItemPrice.setText(item.getPrice());
        etNewItemDescription.setText(item.getDescription());
        cbNewItemPurchased.setChecked(item.isPurchased());
        int spinnerSelected = item.picIconToSpinnerNumber(item.getPicIcon());
        spnNewItemType.setSelection(spinnerSelected);

    }

    @Override
    public void onBackPressed()
    {
        if (!etNewItemName.getText().toString().equals("") ||
                !etNewItemPrice.getText().toString().equals(""))
        {

            new AlertDialog.Builder(this)
                    .setTitle("Lose Progress")
                    .setMessage("Are you sure you want to go back to the list? " +
                            "Progress on the current item will be lost.")
                    .setPositiveButton("Continue", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            finish();
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            dialog.cancel();
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
        else
        {
            super.onBackPressed();
        }
    }
}