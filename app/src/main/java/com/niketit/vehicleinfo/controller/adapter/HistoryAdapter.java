package com.niketit.vehicleinfo.controller.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.gms.ads.NativeExpressAdView;
import com.niketit.vehicleinfo.R;
import com.niketit.vehicleinfo.controller.interfaces.OnClickHistoryItem;
import com.niketit.vehicleinfo.utils.database.EventEntity;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class HistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int MENU_ITEM_VIEW_TYPE = 0;
    private static final int NATIVE_EXPRESS_AD_VIEW_TYPE = 1;
    private Context context;
    private List<Object> vehicleInfos;
    private OnClickHistoryItem onClickHistoryItem;

    public HistoryAdapter(OnClickHistoryItem onClickHistoryItem, Context context, ArrayList<Object> list) {
        this.onClickHistoryItem = onClickHistoryItem;
        this.vehicleInfos = list;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case MENU_ITEM_VIEW_TYPE:
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_row_items, parent, false);
                return new HistoryAdapter.HistoryViewHolder(view);
            case NATIVE_EXPRESS_AD_VIEW_TYPE:
            default:
                View nativeExpressLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.native_express_ad_container, parent, false);
                return new HistoryAdapter.NativeExpressAdViewHolder(nativeExpressLayoutView);
        }
    }

    public class NativeExpressAdViewHolder extends HistoryAdapter.HistoryViewHolder {
        NativeExpressAdViewHolder(View view) {
            super(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == ((vehicleInfos.size()-1) / 2)) {
            return NATIVE_EXPRESS_AD_VIEW_TYPE;
        } else {
            return MENU_ITEM_VIEW_TYPE;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        int viewType = getItemViewType(position);
        switch (viewType) {
            case MENU_ITEM_VIEW_TYPE:
                try {
                    HistoryViewHolder historyViewHolder = (HistoryViewHolder) viewHolder;
                    EventEntity vehicleInfo = (EventEntity) vehicleInfos.get(position);
                    Typeface typefaceB = Typeface.createFromAsset(context.getAssets(), "fonts/bahnschrift.ttf");
                    Typeface typefaceVehicle = Typeface.createFromAsset(context.getAssets(), "fonts/LicensePlate.ttf");
                    historyViewHolder.vehicleNumber.setTypeface(typefaceVehicle);
                    historyViewHolder.owner.setTypeface(typefaceB);
                    historyViewHolder.vehicleNumber.setText(vehicleInfo.getVehicleNumber());
                    historyViewHolder.owner.setText(vehicleInfo.getOwnerName());
                    historyViewHolder.history.setOnClickListener(view -> onClickHistoryItem.historyItem(vehicleInfo));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case NATIVE_EXPRESS_AD_VIEW_TYPE:
            default:
                try {
                    NativeExpressAdViewHolder nativeExpressHolder =
                            (NativeExpressAdViewHolder) viewHolder;
                    NativeExpressAdView adView = (NativeExpressAdView) vehicleInfos.get(position);
                    ViewGroup adCardView = (ViewGroup) nativeExpressHolder.itemView;
                    if (adCardView.getChildCount() > 0) {
                        adCardView.removeAllViews();
                    }
                    if (adView.getParent() != null) {
                        ((ViewGroup) adView.getParent()).removeView(adView);
                    }
                    // Add the Native Express ad to the native express ad view.
                    adCardView.addView(adView);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    public int getItemCount() {
        return vehicleInfos.size();
    }

    private class HistoryViewHolder extends RecyclerView.ViewHolder {
        private AppCompatTextView vehicleNumber;
        private AppCompatTextView owner;
        private CardView history;

        HistoryViewHolder(View view) {
            super(view);
            vehicleNumber = view.findViewById(R.id.vehicle_number_tv);
            owner = view.findViewById(R.id.owner_tv);
            history = view.findViewById(R.id.history_item_ly);
        }
    }
}