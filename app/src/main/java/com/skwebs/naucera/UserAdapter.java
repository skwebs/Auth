package com.skwebs.naucera;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    Context context;
    final ArrayList<UserModel> userList;

    public UserAdapter(Context context, ArrayList<UserModel> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public UserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_row_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.ViewHolder holder, int position) {
        holder.tvUserName.setText(userList.get(position).getName());
        holder.tvUserEmail.setText(userList.get(position).getEmail());
        holder.tvSerialNum.setText(String.valueOf(position + 1));
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private static final String TAG = "UserActivity:";
        final TextView tvUserName;
        final TextView tvUserEmail;
        final TextView tvSerialNum;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUserName = itemView.findViewById(R.id.tv_user_name);
            tvUserEmail = itemView.findViewById(R.id.tv_user_email);
            tvSerialNum = itemView.findViewById(R.id.tv_serial_num);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = this.getAdapterPosition();
            UserModel userModel = userList.get(position);

            String name = userModel.getName();
            String email = userModel.getEmail();

            Log.d(TAG, "onClick: "+position);

            Intent intent = new Intent(context, UserDetailsActivity.class);
            intent.putExtra("name", name);
            intent.putExtra("email",email);

            context.startActivity(intent);

        }
    }
}
