package com.example.retrofit2.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.retrofit2.R;
import com.example.retrofit2.model.Note;
import com.example.retrofit2.retrofit.RetrofitInstance;
import com.example.retrofit2.retrofit.ServiceApi;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SecondFragment extends Fragment {
    TextInputEditText title_edit_text, body_edit_text;
    Note note;
    int idExtra = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_second, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        title_edit_text = view.findViewById(R.id.title_edit_text);
        body_edit_text = view.findViewById(R.id.body_edit_text);
        Button createData = view.findViewById(R.id.bt_post);

        createData.setOnClickListener(view1 -> {

            Note note = new Note(
                    title_edit_text.getText().toString(),
                    body_edit_text.getText().toString()
            );
            if (idExtra == 0) {
                insertNote(note);
            } else {
                note.setId(idExtra);
                updateNote(idExtra, note);
            }
        });

        if (getActivity().getIntent().hasExtra("IdExtra")) {
            idExtra = getActivity().getIntent().getExtras().getInt("IdExtra");
            createData.setText("Update");
            getPostApi(idExtra);
        }
    }

    private void updateNote(int idExtra, Note note) {
        ServiceApi serviceApi = RetrofitInstance.getRetrofit().create(ServiceApi.class);
        Call<Note> call = serviceApi.updatePost(idExtra, note);
        call.enqueue(new Callback<Note>() {
            @Override
            public void onResponse(Call<Note> call, Response<Note> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getActivity(), "Successful Update", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getActivity(), FirstFragment.class));
                }
            }

            @Override
            public void onFailure(Call<Note> call, Throwable t) {
                Toast.makeText(getActivity(), "failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getPostApi(int idExtra) {
        ServiceApi serviceApi = RetrofitInstance.getRetrofit().create(ServiceApi.class);
        Call<Note> call = serviceApi.getNote(idExtra);
        call.enqueue(new Callback<Note>() {
            @Override
            public void onResponse(Call<Note> call, Response<Note> response) {
                if (response.isSuccessful()) {
                    note = response.body();
                    if (note != null) {
                        title_edit_text.setText(note.getTitle());
                        body_edit_text.setText(note.getBody());
                    }
                }
            }

            @Override
            public void onFailure(Call<Note> call, Throwable t) {
                Toast.makeText(getActivity(), "failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void insertNote(Note note) {
        ServiceApi serviceApi = RetrofitInstance.getRetrofit().create(ServiceApi.class);
        Call<Note> call = serviceApi.createPost(note);
        call.enqueue(new Callback<Note>() {
            @Override
            public void onResponse(Call<Note> call, Response<Note> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getActivity(), "Successful Create", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(getActivity(),FirstFragment.class);
                    getActivity().startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<Note> call, Throwable t) {
                Toast.makeText(getActivity(), "failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}