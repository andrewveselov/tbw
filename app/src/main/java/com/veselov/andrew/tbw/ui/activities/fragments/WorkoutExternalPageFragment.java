package com.veselov.andrew.tbw.ui.activities.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.veselov.andrew.tbw.R;
import com.veselov.andrew.tbw.utils.RequestMaker;


public class WorkoutExternalPageFragment extends Fragment {


    public WorkoutExternalPageFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_workout_external_page, container, false);
        final WebView webView = root.findViewById(R.id.browse);
        final TextView status = root.findViewById(R.id.status);
        final ProgressBar progressBar = root.findViewById(R.id.progressBar);
        // Создаем объект класса делателя запросов и на лету делаем анонимный класс слушателя
        final RequestMaker requestMaker = new RequestMaker(new RequestMaker.OnRequestListener() {
            // Обновим прогресс
            @Override
            public void onStatusProgress(String updateProgress) {
                status.setText(updateProgress);
            }
            // По окончании загрузки страницы вызовем этот метод, который и вставит текст в WebView
            @Override
            public void onComplete(String result) {
                webView.loadData(result, "text/html; charset=utf-8", "utf-8");
                progressBar.setIndeterminate(false);
                progressBar.setProgress(100);
            }
        });
        requestMaker.make("https://www.lenta.ru/");

        return root;
    }


}
