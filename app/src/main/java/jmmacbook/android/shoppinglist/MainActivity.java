package jmmacbook.android.shoppinglist;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import butterknife.Bind;
import butterknife.ButterKnife;
import jmmacbook.android.shoppinglist.adapter.SListAdapter;
import jmmacbook.android.shoppinglist.data.Item;

public class MainActivity extends AppCompatActivity
{


    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private SListAdapter sListAdapter;
    public static final int NEW_ITEM_REQUEST = 1;
    public static final int EDIT_ITEM_REQUEST = 2;

    private Item lastEdited;
    private Item lastDeleted;

    private boolean edited;

    ImageView ivItemIcon;
    AlertDialog alertDialog;
    AlertDialog.Builder aDBuilder;

    @Bind(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        ivItemIcon = (ImageView) findViewById(R.id.ivItemIcon);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sListAdapter = new SListAdapter(this);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        final LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(sListAdapter);

        LayoutInflater inflater = LayoutInflater.from(this);
        View promptDelete = inflater.inflate(R.layout.prompt, null);

        aDBuilder = new AlertDialog.Builder(this);
        aDBuilder.setMessage(R.string.areYouSurePrompt);
        aDBuilder.setTitle("Remove All?");
        aDBuilder.setPositiveButton(R.string.deleteALL, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                sListAdapter.removeAllItems();
            }
        });
        aDBuilder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.cancel();
            }
        });
        aDBuilder.setIcon(android.R.drawable.ic_dialog_alert);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch(item.getItemId())
        {
            case R.id.addItem:
                Intent newItemIntent = new Intent(MainActivity.this, NewItemActivity.class);
                startActivityForResult(newItemIntent, NEW_ITEM_REQUEST);
                break;
            case R.id.deleteAll:
                if(alertDialog == null)
                {
                    alertDialog = aDBuilder.create();
                }
                alertDialog.show();
                break;
            case R.id.seeEstimatedCost:
                showPrice();
        }
        return true;
    }

    @Override
    public void onBackPressed()
    {
        //Do nothing
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(requestCode == NEW_ITEM_REQUEST)
        {
            if(resultCode == RESULT_OK)
            {
                Item newItem = (Item) data.getSerializableExtra(NewItemActivity.NEW_ITEM_KEY);
                sListAdapter.addItem(newItem);
            }
        }
        else if(requestCode == EDIT_ITEM_REQUEST)
        {
            if(resultCode == RESULT_OK)
            {
                edited = true;
                lastEdited = (Item) data.getSerializableExtra(NewItemActivity.EDIT_ITEM_KEY);
                Item oldItem = (Item) data.getSerializableExtra(NewItemActivity.OLD_ITEM_KEY);
                int oldItemPos = sListAdapter.getItemPosition(oldItem);
                sListAdapter.editItem(oldItemPos, lastEdited);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void undoDelete()
    {
        Snackbar.make(coordinatorLayout, "List Item Deleted", Snackbar.LENGTH_LONG)
                .setAction("UNDO", new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        sListAdapter.addItem(lastDeleted);
                        Snackbar.make(coordinatorLayout, "Item Restored", Snackbar.LENGTH_LONG).show();
                    }
                }).show();
    }

    public void setLastDeleted(Item lastDeleted)
    {
        this.lastDeleted = lastDeleted;
    }

    public void showPrice()
    {
        Snackbar.make(coordinatorLayout, "Total cost: " + "$" + sListAdapter.getTotalCost(), Snackbar.LENGTH_LONG).show();
    }
}
