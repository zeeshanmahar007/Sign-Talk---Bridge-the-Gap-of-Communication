package com.example.signtalk.ModeSelectionAndNavDrawer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.signtalk.R;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    List<CategoryClass> DataArrayList;
    Context context;

    public MyAdapter(List<CategoryClass> DataArrayList,Context activity) {
        this.DataArrayList = DataArrayList;
        this.context = activity;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater =LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.categoriescardview,parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final CategoryClass categoryClass = DataArrayList.get(position);
        holder.name.setText(DataArrayList.get(position).getName());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, ShowCategorySignActivity.class);
                SharedPreferences sharedPreferences;
                sharedPreferences = context.getSharedPreferences("MyFile", 0);
                SharedPreferences.Editor editor= sharedPreferences.edit();
                editor.putString("Name",categoryClass.getName());
                editor.putString("VideoId",categoryClass.getVideoid());
                editor.commit();
                context.startActivity(i);

            }
        });

    }

    @Override
    public int getItemCount() {
        return DataArrayList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        public CardView cardView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.categoryname);
            cardView = itemView.findViewById(R.id.cardviewcategory);

        }
    }
}