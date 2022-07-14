package com.example.retrofit2.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.retrofit2.R;
import com.example.retrofit2.adapter.RetrofitAdapter;
import com.example.retrofit2.model.Note;
import com.example.retrofit2.retrofit.RetrofitInstance;
import com.example.retrofit2.retrofit.ServiceApi;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FirstFragment extends Fragment {

    private RecyclerView recyclerView;
    private RetrofitAdapter adapter;
    private List<Note> note = new ArrayList<>();
    private ProgressBar progressBar;
    private NavController navController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);

        FloatingActionButton fab = view.findViewById(R.id.fab);

        fab.setOnClickListener(view1 ->
                navController.navigate(R.id.action_firstFragment_to_secondFragment));

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(view.getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        progressBar = view.findViewById(R.id.progress_bar);

        ServiceApi serviceApi = RetrofitInstance.getRetrofit().create(ServiceApi.class);
        Call<List<Note>> call = serviceApi.getNotes();

        call.enqueue(new Callback<List<Note>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(Call<List<Note>> call, Response<List<Note>> response) {
                progressBar.setVisibility(View.VISIBLE);
                if (response.isSuccessful()) {
                    note = response.body();
                    adapter = new RetrofitAdapter(view.getContext(), note, FirstFragment.this);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<List<Note>> call, Throwable t) {
                Log.d("TAG", "onFailure" + t.getLocalizedMessage());
            }
        });
    }

    public void dialogPoster(Note note) {
        new AlertDialog.Builder(getContext())
                .setTitle("Delete Poster")
                .setMessage("Are you sure you want to delete this poster?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ServiceApi serviceApi = RetrofitInstance.getRetrofit().create(ServiceApi.class);
                        Call<Note> call = serviceApi.deletePost(note.getId());
                        call.enqueue(new Callback<Note>() {
                            @SuppressLint({"NotifyDataSetChanged", "NewApi"})

                            @Override
                            public void onResponse(Call<Note> call, Response<Note> response) {
                                if (response.isSuccessful()) {
                                    Toast.makeText(getContext(), "Successful Delete", Toast.LENGTH_SHORT).show();
                                    adapter.notifyDataSetChanged();
                                }
                            }

                            @Override
                            public void onFailure(Call<Note> call, Throwable t) {
                                Toast.makeText(getContext(), "failed", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}