package com.changshi.issa.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.changshi.issa.DatabaseHandler.WebpageItem;
import com.changshi.issa.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class WebpageAdapter extends RecyclerView.Adapter<WebpageAdapter.ViewHolder> {

    private List<WebpageItem> mItems;
    private FirebaseFirestore mFirestore;

    public WebpageAdapter(List<WebpageItem> items, FirebaseFirestore firestore) {
        mItems = items;
        mFirestore = firestore;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_web_inform, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(mItems.get(position));
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public void submitUpdates() {
        notifyDataSetChanged();
    }

    public void saveChangesToFirestore(List<WebpageItem> updatedItems) {
    }

    public List<WebpageItem> setItems(List<WebpageItem> updatedItems) {
        return mItems;
    }

    public List<WebpageItem> getItems() {
        return mItems;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView mDepartmentTextView;
        private TextView mEmailTextView;
        private TextView mContactTextView;
        private TextView mAddressTextView;
        private Button mAddButton;
        private Button mRemoveButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mDepartmentTextView = itemView.findViewById(R.id.dialog_department);
            mEmailTextView = itemView.findViewById(R.id.dialog_email);
            mContactTextView = itemView.findViewById(R.id.dialog_contact);
            mAddressTextView = itemView.findViewById(R.id.dialog_address);
            mAddButton = itemView.findViewById(R.id.dialog_add);
            mRemoveButton = itemView.findViewById(R.id.dialog_remove);
        }

        public void bind(WebpageItem item) {
            mDepartmentTextView.setText(item.getDepartment());
            mEmailTextView.setText(item.getEmail());
            mContactTextView.setText(item.getContact());
            mAddressTextView.setText(item.getAddress());

            mAddButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addNewDepartment();
                }
            });

            mRemoveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeDepartment();
                }
            });
        }

        private void addNewDepartment() {
            // 创建一个新的 WebpageItem 对象
            WebpageItem newItem = new WebpageItem();
            // 设置新部门的属性，您可能需要从用户输入中获取这些值
            newItem.setDepartment("New Department");
            newItem.setEmail("Email");
            newItem.setContact("Contact");
            newItem.setAddress("Address");
            // 将新部门添加到列表中
            mItems.add(newItem);
            // 通知适配器数据已更改
            notifyDataSetChanged();
            // 在这里您还需要将新部门数据保存到数据库中
            // 可以调用一个方法来执行这个操作
            saveToDatabase(newItem);
        }

        private void removeDepartment() {
            // 获取当前部门在列表中的位置
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                // 移除列表中的部门
                WebpageItem removedItem = mItems.remove(position);
                // 通知适配器数据已更改
                notifyDataSetChanged();
                // 在这里您还需要从数据库中删除当前部门的数据
                // 可以调用一个方法来执行这个操作
                deleteFromDatabase(removedItem);
            }
        }

        private void deleteFromDatabase(WebpageItem removedItem) {
            mFirestore.collection("webpageItems")
                    .document(removedItem.getId())
                    .delete()
                    .addOnSuccessListener(aVoid -> {
                        // 成功删除数据
                        // 可以添加一些逻辑来处理成功删除数据的情况
                    })
                    .addOnFailureListener(e -> {
                        // 发生错误，无法删除数据
                        // 可以添加一些逻辑来处理删除失败的情况
                    });
        }

        private void saveToDatabase(WebpageItem newItem) {
            mFirestore.collection("webpageItems")
                    .add(newItem)
                    .addOnSuccessListener(documentReference -> {
                        // 成功保存数据到数据库
                        // 可以添加一些逻辑来处理成功保存数据的情况
                    })
                    .addOnFailureListener(e -> {
                        // 发生错误，无法保存数据
                        // 可以添加一些逻辑来处理保存失败的情况
                    });
        }
    }
}
