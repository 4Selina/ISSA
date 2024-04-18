package com.changshi.issa;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.changshi.issa.Adapter.CategoryAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CategoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        RecyclerView recyclerView = findViewById(R.id.category_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<Category> categories = new ArrayList<>();
        categories.add(new Category("Learning Support", Arrays.asList("Eggs", "Pancakes", "Toast")));
        categories.add(new Category("Social Activities", Arrays.asList("Salad", "Sandwich", "Soup")));
        categories.add(new Category("Accommodation", Arrays.asList("Chicken", "Beef", "Fish")));
        categories.add(new Category("Transports", Arrays.asList("Cake", "Ice Cream", "Cookies")));
        categories.add(new Category("Job Support", Arrays.asList("Part-Time Job", "Internship Information", "Graduate Job")));

        CategoryAdapter adapter = new CategoryAdapter(categories);
        recyclerView.setAdapter(adapter);
    }
}
