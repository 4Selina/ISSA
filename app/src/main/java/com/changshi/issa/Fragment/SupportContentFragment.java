package com.changshi.issa.Fragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.changshi.issa.Adapter.SupportContentAdapter;
import com.changshi.issa.AddActivity;
import com.changshi.issa.HomeActivity;
import com.changshi.issa.R;
import com.changshi.issa.SupportContent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SupportContentFragment extends Fragment {

    private RecyclerView recyclerView;
    private SupportContentAdapter adapter;
    private List<SupportContent> supportContentList;

    private Button buttonEdit;
    private RecyclerView.ViewHolder holder;

    public SupportContentFragment(ArrayList<SupportContent> NewSupportContentList) {
        supportContentList = NewSupportContentList;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_support_content, container, false);

        recyclerView = root.findViewById(R.id.recycler_view_sections);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        buttonEdit = root.findViewById(R.id.button_edit);

        SharedPreferences Pref = getActivity().getSharedPreferences("login_pref", MODE_PRIVATE);
        boolean IsLoggedIn = Pref.getBoolean("is_logged_in", false);

        if (!IsLoggedIn) {
            buttonEdit.setVisibility(View.GONE);
        }
        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddActivity.class);
                intent.putExtra("IsEditMode", true);
                startActivity(intent);
            }
        });

        // 初始化空的支持内容列表
        if (supportContentList == null)
            supportContentList = new ArrayList<>();

        adapter = new SupportContentAdapter(supportContentList);
        recyclerView.setAdapter(adapter);

        // 从Firebase实时数据库中获取支持内容数据
        fetchSupportContentData();

        return root;
    }

        private void fetchSupportContentData() {
            DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("support_contents");
            databaseRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    // clean dats
                    supportContentList.clear();

                    // 遍历数据库中的支持内容数据，并添加到supportContentList中
                    for (DataSnapshot snapshot : dataSnapshot.getChildren())
                    {
                        SupportContent supportContent = snapshot.getValue(SupportContent.class);
                        supportContentList.add(supportContent);
                    }

                    adapter.notifyDataSetChanged();
                }

                //testing if database is passed
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("SupportContentFragment", "Failed to fetch support content data: " + databaseError.getMessage());
                    // 处理获取数据失败的情况
                    // 例如，显示一个错误消息或者重试按钮供用户点击重新获取数据
                }
            });
        }

    public void setHolder(RecyclerView.ViewHolder holder) {
        this.holder = holder;
    }
}
