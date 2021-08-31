package com.bikash.filetransferapp.view;

import com.bikash.filetransferapp.fragment.EditableListFragment;
import com.bikash.filetransferapp.widget.EditableListAdapter;

public interface EditableListFragmentModelImpl<V extends EditableListAdapter.EditableViewHolder>
{
    void setLayoutClickListener(EditableListFragment.LayoutClickListener<V> clickListener);
}
