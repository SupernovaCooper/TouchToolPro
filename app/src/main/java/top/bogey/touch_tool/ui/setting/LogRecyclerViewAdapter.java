package top.bogey.touch_tool.ui.setting;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import top.bogey.touch_tool.data.LogInfo;
import top.bogey.touch_tool.databinding.FloatLogItemBinding;

public class LogRecyclerViewAdapter extends RecyclerView.Adapter<LogRecyclerViewAdapter.ViewHolder> {
    private final ArrayList<LogInfo> showLogs = new ArrayList<>();
    private String taskId;
    private RecyclerView recyclerView;

    public LogRecyclerViewAdapter() {

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(FloatLogItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        holder.refreshItem(showLogs.get(position));
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
        recyclerView.scrollToPosition(getItemCount() - 1);
    }

    @Override
    public int getItemCount() {
        return showLogs.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void addLogs(String taskId, ArrayList<LogInfo> logs) {
        this.taskId = taskId;
        showLogs.clear();
        showLogs.addAll(logs);
        notifyDataSetChanged();
        if (recyclerView != null) recyclerView.scrollToPosition(showLogs.size() - 1);
    }

    public void addLog(LogInfo log) {
        if (!log.getTaskId().equals(taskId)) return;
        showLogs.add(log);
        notifyItemInserted(showLogs.size());
        if (recyclerView != null) recyclerView.scrollToPosition(showLogs.size() - 1);
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder {
        public final FloatLogItemBinding binding;
        private final Context context;

        public ViewHolder(@NonNull FloatLogItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            context = binding.getRoot().getContext();
        }

        @SuppressLint("DefaultLocale")
        public void refreshItem(LogInfo log) {
            binding.titleText.setText(String.format("%s\n%d. %s", log.getTime(context), log.getIndex(), log.getLog()));
        }
    }
}