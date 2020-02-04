package com.iceka.whatsappclone.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.iceka.whatsappclone.InputPhoneNumberActivity;
import com.iceka.whatsappclone.R;
import com.iceka.whatsappclone.models.CountryCallingCode;

import java.util.List;

public class AdapterCountryCallingCode extends RecyclerView.Adapter<AdapterCountryCallingCode.MyViewHolder> {

    private List<CountryCallingCode> countryCallingCodeList;
    private Context mContext;

    public AdapterCountryCallingCode(Context context, List<CountryCallingCode> countryCallingCodes) {
        this.mContext = context;
        this.countryCallingCodeList = countryCallingCodes;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_country_calling_code, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        final CountryCallingCode countryCallingCode = countryCallingCodeList.get(position);

        int flagOffset = 0x1F1E6;
        int asciiOffset = 0x41;

        String codeName = countryCallingCode.getCode();
        int firstChar = Character.codePointAt(codeName, 0) - asciiOffset + flagOffset;
        int secondChar = Character.codePointAt(codeName, 1) - asciiOffset + flagOffset;
        String flag = new String(Character.toChars(firstChar)) + new String(Character.toChars(secondChar));

        holder.countryFlag.setText(flag);
        holder.countryName.setText(countryCallingCode.getName());
        holder.dialCode.setText(countryCallingCode.getDial_code());
        if (countryCallingCode.getLocal() != null) {
            holder.countryLocalName.setVisibility(View.VISIBLE);
            holder.countryLocalName.setText(countryCallingCode.getLocal());
        } else {
            holder.countryLocalName.setVisibility(View.GONE);
        }

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.selectedCountry.setVisibility(View.VISIBLE);
                Intent intent = new Intent(mContext, InputPhoneNumberActivity.class);
                intent.putExtra("countryName", countryCallingCode.getName());
                intent.putExtra("countryDialCode", countryCallingCode.getDial_code());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                ((Activity) view.getContext()).setResult(Activity.RESULT_OK, intent);
//                mContext.startActivity(intent);
                ((Activity) view.getContext()).finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return countryCallingCodeList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView countryFlag;
        private TextView countryName;
        private TextView countryLocalName;
        private TextView dialCode;
        private ImageView selectedCountry;
        private LinearLayout layout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            countryFlag = itemView.findViewById(R.id.country_flag);
            countryName = itemView.findViewById(R.id.tv_country_name);
            countryLocalName = itemView.findViewById(R.id.tv_country_local_name);
            dialCode = itemView.findViewById(R.id.tv_dial_code);
            selectedCountry = itemView.findViewById(R.id.img_country_code_selected);
            layout = itemView.findViewById(R.id.country_calling_code_layout);
        }
    }


}
