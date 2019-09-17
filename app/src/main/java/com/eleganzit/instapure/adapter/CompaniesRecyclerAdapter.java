package com.eleganzit.instapure.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import com.eleganzit.instapure.R;
import com.eleganzit.instapure.model.CompanyListData;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class CompaniesRecyclerAdapter extends RecyclerView.Adapter<CompaniesRecyclerAdapter.MyViewHolder>
        implements Filterable {
    private Context context;
    private List<CompanyListData> contactList;
    private List<CompanyListData> contactListFiltered;
    private ContactsAdapterListener listener;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name;

        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.txt_company);


        }
    }

    public CompaniesRecyclerAdapter(Context context, List<CompanyListData> contactList, ContactsAdapterListener listener) {
        this.context = context;
        this.listener = listener;
        this.contactList = contactList;
        this.contactListFiltered = contactList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_company_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        final CompanyListData companyListData = contactListFiltered.get(position);
        holder.name.setText(companyListData.getTitle());

        Log.d("fhsdkfdfsfvc",">>>>  "+companyListData.getTitle()+"");

        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // send selected contact in callback
                listener.onContactSelected(contactListFiltered.get(position));
                //Toast.makeText(context, ""+contactListFiltered.get(position).getTitle(), Toast.LENGTH_SHORT).show();
            }
        });
        //holder.phone.setText(pagesData.getPhone());
    }

    @Override
    public int getItemCount() {
        return contactListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();

                if (charString.isEmpty()) {
                    contactListFiltered = contactList;
                    Log.d("zzzzzzzzzz","is empty");
                } else {
                    Log.d("zzzzzzzzzz","not empty  "+charString);
                    List<CompanyListData> filteredList = new ArrayList<>();
                    for (CompanyListData row : contactList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        Log.d("contactListFiltered",row.getTitle().toLowerCase()+"    "+charSequence);
                        if (row.getTitle().toLowerCase().contains(charString.toLowerCase()) || row.getTitle().contains(charString)) {
                            filteredList.add(row);

                            Log.d("addddeddd","added "+row.getTitle());
                        }
                    }

                    contactListFiltered = filteredList;
                    for(int i=0;i<contactListFiltered.size();i++){
                        Log.d("fhsdkfdfsfvc",contactListFiltered.get(i).getTitle()+"");
                    }

                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = contactListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                contactListFiltered = (ArrayList<CompanyListData>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface ContactsAdapterListener {
        void onContactSelected(CompanyListData pagesData);
    }
}
