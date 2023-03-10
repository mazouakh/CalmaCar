package com.example.calmacar.common;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.calmacar.R;

import java.util.ArrayList;

public class PaymentsAdapter extends BaseAdapter {

    Context context;
    LayoutInflater inflater;
    ArrayList<Payment> payments;

    public PaymentsAdapter(Context context, ArrayList<Payment> payments) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.payments = payments;
    }

    @Override
    public int getCount() {
        return payments.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.listview_payments, null);

        // Hooks
        TextView tv_paymentID = view.findViewById(R.id.tv_paymentID);
        TextView tv_paymentAmount = view.findViewById(R.id.tv_paymentAmount);

        // Set Values
        tv_paymentID.setText(payments.get(i).getId());
        tv_paymentAmount.setText(payments.get(i).getAmount() + "€");

        return view;
    }
}
