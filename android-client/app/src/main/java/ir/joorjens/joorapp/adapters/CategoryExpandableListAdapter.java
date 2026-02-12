package ir.joorjens.joorapp.adapters;

/**
 * Created by meysammoh on 14.11.17.
 */

import java.util.HashMap;
import java.util.List;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import ir.joorjens.joorapp.R;
import ir.joorjens.joorapp.customViews.ImageViewPlus;
import ir.joorjens.joorapp.models.KeyValueChildItem;
import ir.joorjens.joorapp.models.PairResultItem;

public class CategoryExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<KeyValueChildItem> expandableListTitle;

    public CategoryExpandableListAdapter(Context context, List<KeyValueChildItem> expandableListTitle) {
        this.context = context;
        this.expandableListTitle = expandableListTitle;
    }

    @Override
    public Object getChild(int listPosition, int expandedListPosition) {
        return this.expandableListTitle.get(listPosition).getChild().get(expandedListPosition);
    }

    @Override
    public long getChildId(int listPosition, int expandedListPosition) {
        return expandedListPosition;
    }

    @Override
    public View getChildView(int listPosition, final int expandedListPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        final String expandedListText = ((KeyValueChildItem) getChild(listPosition, expandedListPosition)).getName();
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_category_item, null);
        }
        TextView expandedListTextView = (TextView) convertView.findViewById(R.id.expandedListItem);
        expandedListTextView.setText(expandedListText);
        return convertView;
    }

    @Override
    public int getChildrenCount(int listPosition) {
        return this.expandableListTitle.get(listPosition).getChild().size();
    }

    @Override
    public Object getGroup(int listPosition) {
        return this.expandableListTitle.get(listPosition);
    }

    @Override
    public int getGroupCount() {
        return this.expandableListTitle.size();
    }

    @Override
    public long getGroupId(int listPosition) {
        return this.expandableListTitle.get(listPosition).getId();
    }

    @Override
    public View getGroupView(int listPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String listTitle = ((KeyValueChildItem) getGroup(listPosition)).getName();
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_category_group, null);
        }

        RelativeLayout border = (RelativeLayout) convertView.findViewById(R.id.list_border);
//        ViewGroup.LayoutParams lp = border.getLayoutParams();
//        lp.height = 60;
//        border.setLayoutParams(lp);

        TextView listTitleTextView = (TextView) convertView.findViewById(R.id.listTitle);
        listTitleTextView.setText(listTitle);
        ImageViewPlus indicator = (ImageViewPlus) convertView.findViewById(R.id.list_group_indicator);
        View divider = convertView.findViewById(R.id.list_group_divider);

        if (isExpanded) {
            indicator.setImageResource(R.drawable.v_expand_arrow_up);
            border.setBackground(ContextCompat.getDrawable( context, R.drawable.rounded_discount_color));
            listTitleTextView.setTextColor(Color.WHITE);
            divider.setVisibility(View.GONE);
        } else {
            indicator.setImageResource(R.drawable.v_expand_arrow_down);
            border.setBackground(ContextCompat.getDrawable( context, R.drawable.expand_border));
            listTitleTextView.setTextColor(ContextCompat.getColor(context, R.color.expand_text));
            divider.setVisibility(View.VISIBLE);
        }
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int listPosition, int expandedListPosition) {
        return true;
    }
}

