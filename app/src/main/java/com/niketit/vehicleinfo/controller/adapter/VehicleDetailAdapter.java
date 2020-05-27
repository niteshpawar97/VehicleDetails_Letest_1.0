package com.niketit.vehicleinfo.controller.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.ads.NativeExpressAdView;
import com.niketit.vehicleinfo.R;
import com.niketit.vehicleinfo.model.VehicleInfo;
import com.niketit.vehicleinfo.utils.AdIDSingleton;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

public class VehicleDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int MENU_ITEM_VIEW_TYPE = 0;
    private static final int NATIVE_EXPRESS_AD_VIEW_TYPE = 1;
    private static final int VIEW_TYPE_FOOTER = 2;
    private Context context;
    private ArrayList<Object> vehicleInfos;

    public VehicleDetailAdapter(Context context, ArrayList<Object> list) {
        this.vehicleInfos = list;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case MENU_ITEM_VIEW_TYPE:
                View view1 = LayoutInflater.from(parent.getContext()).inflate(R.layout.number_details_item, parent, false);
                return new VehicleDetailAdapter.DetailViewHolder(view1);
            case VIEW_TYPE_FOOTER:
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.detail_contact_us_ly, parent, false);
                return new VehicleDetailAdapter.ErrorContactLy(view);
            case NATIVE_EXPRESS_AD_VIEW_TYPE:
            default:
                View nativeExpressLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.native_express_ad_container, parent, false);
                return new NativeExpressAdViewHolder(nativeExpressLayoutView);
        }
    }

    public class NativeExpressAdViewHolder extends VehicleDetailAdapter.DetailViewHolder {
        NativeExpressAdViewHolder(View view) {
            super(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == vehicleInfos.size() - 1) {
            return VIEW_TYPE_FOOTER;
        } else {
            if (position == ((vehicleInfos.size() - 1) / 2)) {
                return NATIVE_EXPRESS_AD_VIEW_TYPE;
            } else {
                return MENU_ITEM_VIEW_TYPE;
            }
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        int viewType = getItemViewType(position);
        switch (viewType) {
            case MENU_ITEM_VIEW_TYPE:
                try {
                    VehicleDetailAdapter.DetailViewHolder historyViewHolder = (VehicleDetailAdapter.DetailViewHolder) viewHolder;
                    VehicleInfo vehicleInfo = (VehicleInfo) vehicleInfos.get(position);
                    Typeface typefaceB = Typeface.createFromAsset(context.getAssets(), "fonts/bahnschrift.ttf");
                    historyViewHolder.header.setTypeface(typefaceB);
                    historyViewHolder.value.setTypeface(typefaceB);
                    historyViewHolder.header.setText(vehicleInfo.getName());
                    historyViewHolder.value.setText(vehicleInfo.getValue());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case VIEW_TYPE_FOOTER:
                VehicleDetailAdapter.ErrorContactLy errorContactLy = (VehicleDetailAdapter.ErrorContactLy) viewHolder;
                VehicleInfo vehicleInfo = (VehicleInfo) vehicleInfos.get(position);
                Typeface typefaceB = Typeface.createFromAsset(context.getAssets(), "fonts/bahnschrift.ttf");
                errorContactLy.notShowingCorrectInfo.setTypeface(typefaceB);
                errorContactLy.header.setTypeface(typefaceB);
                errorContactLy.value.setTypeface(typefaceB);
                errorContactLy.header.setText(vehicleInfo.getName());
                errorContactLy.value.setText(vehicleInfo.getValue());
                errorContactLy.contactusInfo.setOnClickListener(view -> openGmail());
                break;
            case NATIVE_EXPRESS_AD_VIEW_TYPE:
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

    private void openGmail() {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("text/plain");
        emailIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"niketitservices@yhaoo.com"});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Regarding incorrect details of my Vehicle");
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Hello Sir,"+ "\n\n");
        stringBuilder.append("My Vehicle number is"+ ":" + AdIDSingleton.INSTANCE.getVehicleNumber()+ "\n\n");
        stringBuilder.append("The RC detail of my Vehicle are not showing Correct information."+ "\n\n");
        stringBuilder.append("Please check and update the information."+ "\n\n");
        stringBuilder.append("Thank You.");
        emailIntent.putExtra(Intent.EXTRA_TEXT, stringBuilder.toString());
        context.startActivity(Intent.createChooser(emailIntent, "Send mail..."));
    }

    @Override
    public int getItemCount() {
        return vehicleInfos.size();
    }

    class DetailViewHolder extends RecyclerView.ViewHolder {
        private AppCompatTextView header;
        private AppCompatTextView value;

        DetailViewHolder(View view) {
            super(view);
            header = view.findViewById(R.id.header_tv);
            value = view.findViewById(R.id.value_tv);
        }
    }

    public class ErrorContactLy extends RecyclerView.ViewHolder {

        private AppCompatTextView notShowingCorrectInfo;
        private AppCompatButton contactusInfo;
        private AppCompatTextView header;
        private AppCompatTextView value;

        ErrorContactLy(View view) {
            super(view);
            header = view.findViewById(R.id.header_tv);
            value = view.findViewById(R.id.value_tv);
            notShowingCorrectInfo = view.findViewById(R.id.not_showing_correct_info_tv);
            contactusInfo = view.findViewById(R.id.contact_us_info_bt);
        }
    }
}