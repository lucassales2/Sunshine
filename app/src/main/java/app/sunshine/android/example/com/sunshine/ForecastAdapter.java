package app.sunshine.android.example.com.sunshine;

import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.DrawableRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import app.sunshine.android.example.com.sunshine.ui.CursorRecyclerViewAdapter;
import app.sunshine.android.example.com.sunshine.viewmodels.ForecastListItemViewModel;

public class ForecastAdapter extends CursorRecyclerViewAdapter<ForecastAdapter.ViewHolder> {

    private static final int VIEW_TYPE_TODAY = 0;
    private static final int VIEW_TYPE_FUTURE = 1;
    private final Listener listener;
    private boolean twoPane;

    public ForecastAdapter(Cursor cursor, Listener listener) {
        super(cursor);
        this.listener = listener;
    }

    public void setTwoPane(boolean twoPane) {
        this.twoPane = twoPane;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), viewType == VIEW_TYPE_TODAY ? R.layout.list_item_forecast_today : R.layout.list_item_forecast, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final Cursor cursor) {
        final int position = cursor.getPosition();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(cursor, position);
            }
        });
        if (holder.viewModel != null) {
            holder.viewModel.update(cursor, holder.itemView.getContext());
        } else {
            holder.viewModel = new ForecastListItemViewModel(cursor, holder.itemView.getContext());
        }

        holder.viewDataBinding.setVariable(BR.forecast, holder.viewModel);
        @DrawableRes int resId;
        if (getItemViewType(position) == VIEW_TYPE_TODAY) {
            resId = Utility.getArtResourceForWeatherCondition(holder.viewModel.getWeatherCondition());
        } else {
            resId = Utility.getIconResourceForWeatherCondition(holder.viewModel.getWeatherCondition());
        }
        holder.imageView.setContentDescription(String.format(holder.itemView.getContext().getString(R.string.weather_condition), holder.viewModel.getDesc()));
        holder.imageView.setImageResource(resId);
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 && !twoPane ? VIEW_TYPE_TODAY : VIEW_TYPE_FUTURE;
    }

    interface Listener {
        void onItemClick(Cursor cursor, int position);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        final ImageView imageView;
        ForecastListItemViewModel viewModel;
        private ViewDataBinding viewDataBinding;

        ViewHolder(ViewDataBinding viewDataBinding) {
            super(viewDataBinding.getRoot());
            this.viewDataBinding = viewDataBinding;
            imageView = (ImageView) itemView.findViewById(R.id.list_item_icon);
        }
    }
}
