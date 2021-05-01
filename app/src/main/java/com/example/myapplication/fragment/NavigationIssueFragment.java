package com.example.myapplication.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.myapplication.Adapter;
import com.example.myapplication.ApiClient;
import com.example.myapplication.Model.Articles;
import com.example.myapplication.Model.Headlines;
import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NavigationIssueFragment extends Fragment {
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    final String API_KEY ="948efc67d5924d89a8619bf6f840cf13";
    String country;
    Adapter adapter;
    List<Articles> articles = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_bottom_main_issue, container, false);
        swipeRefreshLayout = view.findViewById(R.id.swipe);
        recyclerView = view.findViewById(R.id.recyclerView);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                retrieveJson(country,API_KEY);
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        country = getCountry();
        adapter = new Adapter(getActivity(), articles);
        recyclerView.setAdapter(adapter);

        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        retrieveJson(country,API_KEY);
    }

    public void retrieveJson(String country, String apiKey){
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        swipeRefreshLayout.setRefreshing(true);
        Call<Headlines> call = ApiClient.getInstance().getApi().getHeadlines(country,apiKey);
        call.enqueue(new Callback<Headlines>() {
            @Override
            public void onResponse(Call<Headlines> call, Response<Headlines> response) {
                Log.d("respose값",response+"");
                if(response.isSuccessful() && response.body().getArticles()!=null) {
                    swipeRefreshLayout.setRefreshing(false);
                    articles.clear();
                    articles = response.body().getArticles();
                    adapter = new Adapter(getActivity(), articles);
                    recyclerView.setAdapter(adapter);

                }

            }

            @Override
            public void onFailure(Call<Headlines> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(getActivity(),t.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    public String getCountry(){
        Locale locale = Locale.getDefault();
        String country = "kr";
        return country.toLowerCase();
    }
}
