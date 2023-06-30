package fpt.edu.cook_now_app.adpter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import fpt.edu.cook_now_app.R;

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.StepViewHolder>{
    private List<String> list;

    public StepAdapter(List<String> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public StepViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_step, parent, false);
        return new StepViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StepViewHolder holder, int position) {
        String step = list.get(position);

        if(step == null){
            return;
        }

        holder.stepNumber.setText(position + 1 + "");
        holder.stepText.setText(step);

    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public static class StepViewHolder extends RecyclerView.ViewHolder {
        TextView stepNumber, stepText;
        public StepViewHolder(@NonNull View itemView) {
            super(itemView);
            stepNumber = itemView.findViewById(R.id.stepNumber);
            stepText = itemView.findViewById(R.id.stepText);
        }
    }
}
