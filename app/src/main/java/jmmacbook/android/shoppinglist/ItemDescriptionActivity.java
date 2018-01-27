package jmmacbook.android.shoppinglist;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import jmmacbook.android.shoppinglist.data.Item;


// Displays Description of the Item
public class ItemDescriptionActivity extends AppCompatActivity
{
    @Bind(R.id.tvItemDescription)
    TextView tvItemDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_description);

        ButterKnife.bind(this);

        Intent intent = getIntent();
        Item descriptionItem = (Item) intent.getSerializableExtra("DESCRIPTION_ITEM");
        setDescription(descriptionItem);
    }

    public void setDescription(Item item)
    {
        tvItemDescription.setText(item.getDescription());
    }
}
