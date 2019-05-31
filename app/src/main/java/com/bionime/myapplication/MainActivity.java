package com.bionime.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private RecyclerView recyclerView;
    private MyAdapter myAdapter;
    private ArrayList<String> arrayList;
    private LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        arrayList = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            arrayList.add(String.valueOf(i + 1));
        }
        myAdapter = new MyAdapter(arrayList);
        recyclerView = findViewById(R.id.recyclerView);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(myAdapter);
        recyclerView.post(() -> {
            linearLayoutManager.scrollToPosition(myAdapter.getItemCount() - 1);
            recyclerView.addOnScrollListener(new MyScrollListener());
        });
    }

    class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
        public final static int TOP = 0;
        public final static int BOTTOM = 1;
        private ArrayList<String> arrayList;

        MyAdapter(ArrayList<String> arrayList) {
            this.arrayList = arrayList;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            holder.textItem.setText(arrayList.get(position));
        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }

        void insertItem(int i, ArrayList<String> arrayList) {
            if (i == TOP) {
                this.arrayList.addAll(0, arrayList);
            } else if (i == BOTTOM) {
                this.arrayList.addAll(getItemCount(), arrayList);
            }
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            private TextView textItem;

            MyViewHolder(@NonNull View itemView) {
                super(itemView);
                textItem = itemView.findViewById(R.id.textItem);
            }
        }
    }

    class MyScrollListener extends RecyclerView.OnScrollListener {

        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            Log.d(TAG, "onScrolled: ");
            int limitCount = 10;
            int total = myAdapter.getItemCount();
            int firstIndex = linearLayoutManager.findFirstCompletelyVisibleItemPosition();
            int lastIndex = linearLayoutManager.findLastCompletelyVisibleItemPosition();
            if (dy < 0 && firstIndex <= limitCount) {
                ArrayList<String> arrayList = new ArrayList<>();
                for (int i = 0; i < 50; i++) {
                    arrayList.add(String.valueOf(i + 1));
                }
                myAdapter.insertItem(MyAdapter.TOP, arrayList);
                recyclerView.post(() -> myAdapter.notifyItemRangeInserted(0, arrayList.size()));
            } else if (dy > 0 && lastIndex > (total - limitCount)) {
                ArrayList<String> arrayList = new ArrayList<>();
                for (int i = 0; i < 50; i++) {
                    arrayList.add(String.valueOf(i + 1));
                }
                myAdapter.insertItem(MyAdapter.BOTTOM, arrayList);
                recyclerView.post(() -> myAdapter.notifyItemRangeInserted(total, arrayList.size()));
            }
        }
    }
}
