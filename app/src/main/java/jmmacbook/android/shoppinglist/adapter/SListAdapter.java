package jmmacbook.android.shoppinglist.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import jmmacbook.android.shoppinglist.ItemDescriptionActivity;
import jmmacbook.android.shoppinglist.MainActivity;
import jmmacbook.android.shoppinglist.NewItemActivity;
import jmmacbook.android.shoppinglist.R;
import jmmacbook.android.shoppinglist.data.Item;

/**
 * Created by jmmacbook on 4/5/16.
 */
public class SListAdapter
        extends RecyclerView.Adapter<SListAdapter.ViewHolder>
{
    private Context context;
    private List<Item> items = new ArrayList<Item>();
    private boolean removeAll;

    public SListAdapter(Context callerContext)
    {
        this.context = callerContext;
        items = Item.listAll(Item.class);
        removeAll = false;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View rowView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.shopping_list_row, parent, false);
        return new ViewHolder(rowView);
    }

    @Override
    public void onBindViewHolder(final SListAdapter.ViewHolder holder, final int position)
    {
        holder.tvItemName.setText(items.get(position).getName());
        holder.tvItemPrice.setText(items.get(position).getPrice());

        holder.cbItemPurchased.setChecked(items.get(position).isPurchased());
        holder.cbItemPurchased.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (items.get(position).isPurchased())
                {
                    items.get(position).setPurchased(false);
                    items.get(position).save();
                }
                else
                {
                    items.get(position).setPurchased(true);
                    items.get(position).save();
                }
            }
        });

        holder.ivItemIcon.setImageResource(items.get(position).getPicIcon());

        holder.btnEdit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent editIntent = new Intent(context, NewItemActivity.class);
                editIntent.putExtra("ITEM_TO_EDIT", items.get(position));
                ((MainActivity)context).startActivityForResult(editIntent,
                        MainActivity.EDIT_ITEM_REQUEST);
            }
        });

        holder.btnDelete.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                int pos = holder.getAdapterPosition();
                removeItem(pos);
            }
        });

        holder.btnDescription.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                int pos = holder.getAdapterPosition();
                Intent descriptionIntent = new Intent(context, ItemDescriptionActivity.class);
                descriptionIntent.putExtra("DESCRIPTION_ITEM", items.get(pos));
                context.startActivity(descriptionIntent);
            }
        });
    }

    public int getItemPosition(Item item)
    {
        int len = items.size();
        for (int i = 0; i < len; i++)
        {
            if(items.get(i).getName().equals(item.getName()) &&
                    items.get(i).getDescription().equals(item.getDescription()) &&
                    items.get(i).getPrice().equals(item.getPrice())&&
                    items.get(i).isPurchased() == item.isPurchased() &&
                    items.get(i).getPicIcon() == item.getPicIcon())
            {
                return i;
            }
        }
        return -1;
    }

    public void setPurchased(int position){

    }

    @Override
    public int getItemCount()
    {
        return items.size();
    }

    public int getTotalCost()
    {
        int cost = 0;
        for (Item item: items)
        {
            if(!item.isPurchased())
            {
                cost += Integer.valueOf(item.deleteDollarSign(item.getPrice()));
            }
        }
        return cost;
    }

    public void addItem(Item item)
    {
        int len = items.size();
        items.add(len, item);
        item.save();
        notifyItemInserted(len);

    }

    // must pass in spinner of newItem
    public void editItem(int positionToEdit, Item newItem){
        Item oldItem = items.get(positionToEdit);
        items.set(positionToEdit, newItem);
        oldItem.setName(newItem.getName());
        oldItem.setDescription(newItem.getDescription());
        oldItem.setPrice(newItem.getPrice());
        oldItem.setPicIconFromID(newItem.getPicIcon());
        oldItem.setPurchased(newItem.isPurchased());
        oldItem.save();
        notifyItemChanged(positionToEdit);
    }

    public void removeWithoutUndo(int position)
    {
        items.get(position).delete();
        items.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, getItemCount());
    }

    public void removeItem(int position)
    {
        ((MainActivity) context).setLastDeleted(items.get(position));
        items.get(position).delete();
        items.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, getItemCount());
        ((MainActivity) context).undoDelete();
    }

    public void removeAllItems()
    {
        removeAll = true;
        int size = items.size();

        while(size > 0)
        {
            removeWithoutUndo(0);
            size--;
        }
        removeAll = false;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        private TextView tvItemName;
        private TextView tvItemPrice;
        private CheckBox cbItemPurchased;
        private ImageView ivItemIcon;
        private Button btnDelete;
        private Button btnDescription;
        private Button btnEdit;

        public ViewHolder(View itemView)
        {
            super(itemView);
            tvItemName = (TextView) itemView.findViewById(R.id.tvItemName);
            tvItemPrice = (TextView) itemView.findViewById(R.id.tvItemPrice);
            cbItemPurchased = (CheckBox) itemView.findViewById(R.id.cbPurchasedState);
            ivItemIcon = (ImageView) itemView.findViewById(R.id.ivItemIcon);
            btnEdit = (Button) itemView.findViewById(R.id.btnEdit);
            btnDelete = (Button) itemView.findViewById(R.id.btnDelete);
            btnDescription = (Button) itemView.findViewById(R.id.btnShowItemDets);
        }

    }
}
