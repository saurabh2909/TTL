package com.example.saubhagyam.thetalklist.TutorGuideFiles;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.saubhagyam.thetalklist.R;
import com.unnamed.b.atv.model.TreeNode;

/**
 * Created by Bogdan Melnychuk on 2/13/15.
 */
public class HeaderHolder extends TreeNode.BaseNodeViewHolder<IconTreeItemHolder.IconTreeItem> {

//    private PrintView arrowView;

    public HeaderHolder(Context context) {
        super(context);
    }

    @Override
    public View createNodeView(TreeNode node, IconTreeItemHolder.IconTreeItem value) {
        final LayoutInflater inflater = LayoutInflater.from(context);
        final View view = inflater.inflate(R.layout.row_first, null, false);
        TextView tvValue = (TextView) view.findViewById(R.id.eventsListEventRowText);
        tvValue.setText(value.text);


        return view;
    }

    @Override
    public void toggle(boolean active) {
//        arrowView.setIconText(context.getResources().getString(active ? R.string.ic_keyboard_arrow_down : R.string.ic_keyboard_arrow_right));
    }


}
