package com.example.pr06_version2.ui.list;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.pr06_version2.R;
import com.example.pr06_version2.base.BaseRecyclerViewAdapter;
import com.example.pr06_version2.base.OnUserClickListener;
import com.example.pr06_version2.data.model.User;
import com.example.pr06_version2.databinding.ItemBinding;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

public class ListFragmentAdapter extends BaseRecyclerViewAdapter<User, ListFragmentAdapter.ViewHolder> {

    private OnUserClickListener onUserClickListener;

    ListFragmentAdapter(OnUserClickListener onUserClickListener) {
        super(new DiffUtil.ItemCallback<User>() {
            @Override
            public boolean areItemsTheSame(@NonNull User oldItem, @NonNull User newItem) {
                return oldItem.getId() == newItem.getId();
            }

            @Override
            public boolean areContentsTheSame(@NonNull User oldItem, @NonNull User newItem) {
                return TextUtils.equals(oldItem.getName(), newItem.getName()) &&
                        TextUtils.equals(oldItem.getWeb(), newItem.getWeb()) &&
                        TextUtils.equals(oldItem.getAddress(), newItem.getAddress()) &&
                        TextUtils.equals(oldItem.getEmail(), newItem.getEmail()) &&
                        oldItem.getImg_Avatar().getId() == newItem.getImg_Avatar().getId() &&
                        oldItem.getTlf() == newItem.getTlf();
            }
        });
        this.onUserClickListener = onUserClickListener;
        setHasStableIds(true);
    }

    @NonNull
    @Override
    public ListFragmentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ItemBinding b;

        ViewHolder(@NonNull ItemBinding itemView) {
            super(itemView.getRoot());
            b = itemView;
        }

        void bind(User item) {
            b.lblUsermail.setText(item.getEmail());
            b.imageUser.setImageResource(item.getImg_Avatar().getImageResId());
            b.lblUsername.setText(item.getName());
            b.lblUsertlf.setText(String.valueOf(item.getTlf()));

            b.btnEdit.setOnClickListener(v -> onUserClickListener.onEditUser(item));
            b.btnDelete.setOnClickListener(v -> onUserClickListener.onDeleteUser(item));
        }
    }
}
