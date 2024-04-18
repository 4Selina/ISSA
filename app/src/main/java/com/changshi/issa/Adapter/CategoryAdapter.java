package com.changshi.issa.Adapter;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.changshi.issa.Category;
import com.changshi.issa.ClickableTextView;
import com.changshi.issa.HomeActivity;
import com.changshi.issa.R;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    private List<Category> categories;
    public CategoryAdapter(List<Category> categories) {
        this.categories = categories;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_category_item, parent, false);
        return new ViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Category category = categories.get(position);
        holder.bind(category);
    }
    @Override
    public int getItemCount() {
        return categories.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView parentCategoryTitle;
        private ClickableTextView subCategoryTitle;

        public ViewHolder(View itemView) {
            super(itemView);
            parentCategoryTitle = itemView.findViewById(R.id.parent_category_title);
            subCategoryTitle = itemView.findViewById(R.id.subcategory_title);
        }

        public void bind(Category category) {
            parentCategoryTitle.setText(category.getParentCategory());
            subCategoryTitle.setTextWithClickableSubCategories(category.getSubCategories());
            subCategoryTitle.setOnSubCategoryClickListener(new ClickableTextView.OnSubCategoryClickListener() {
                @Override
                public void onSubCategoryClick(String subCategory) {
                    // log subcategory, for debugging
                    Log.d("subcategory", "onSubCategoryClick: " + subCategory + " clicked");
                    Intent intent = new Intent(itemView.getContext(), HomeActivity.class);
                    intent.putExtra("category", category.getParentCategory());
                    intent.putExtra("subcategory", subCategory);
                    itemView.getContext().startActivity(intent);
                }
                @Override
                public void onSubCategoryClick(String subCategory, int position) {
                    String clickedSubCategory = category.getSubCategories().get(position);
                    Log.d("subcategory", "onSubCategoryClick: " + clickedSubCategory + " clicked");
                    Intent intent = new Intent(itemView.getContext(), HomeActivity.class);
                    intent.putExtra("category", category.getParentCategory());
                    intent.putExtra("subcategory", clickedSubCategory);
                    itemView.getContext().startActivity(intent);
                }
            });
        }

    }
}
