package com.android.quantumassignment;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.client.methods.CloseableHttpResponse;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.client.methods.HttpGet;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.impl.client.CloseableHttpClient;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.impl.client.HttpClientBuilder;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.util.EntityUtils;

import org.json.JSONArray;
import org.json.JSONObject;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView ivLogOut;
    FirebaseAuth firebaseAuth;
    RecyclerView rvHome;
    JSONArray arr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        init();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        ivLogOut.setOnClickListener(this);
        HomeAdapter ha = new HomeAdapter();
        rvHome.setLayoutManager(new LinearLayoutManager(this));
        rvHome.setAdapter(ha);
        ha.notifyDataSetChanged();
        try {
            CloseableHttpClient client = HttpClientBuilder.create().build();
            HttpGet httpGet = new HttpGet("https://newsapi.org/v2/top-headlines?country=us&category=business&apiKey=d6db21d9f04e453995b8c43b85e95eb0");
            CloseableHttpResponse response = client.execute(httpGet);
            String responseJSON = EntityUtils.toString(response.getEntity());
            JSONObject jobj = new JSONObject(responseJSON);
            arr = jobj.getJSONArray("articles");
            //Toast.makeText(this, arr.toString(), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public void init() {
        ivLogOut = findViewById(R.id.ivLogOut);
        firebaseAuth = FirebaseAuth.getInstance();
        rvHome = findViewById(R.id.rvHome);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivLogOut:
                firebaseAuth.signOut();
                startActivity(new Intent(HomeActivity.this, MainActivity.class));
                finish();
                break;
        }
    }

    public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {
        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            try {
                JSONObject jo = arr.getJSONObject(position);
                holder.tvSource.setText(jo.getJSONObject("source").getString("name"));
                holder.tvTitle.setText(jo.getString("title"));
                holder.tvContent.setText(jo.getString("description"));
                Glide.with(getApplicationContext()).load(jo.getString("urlToImage")).into(holder.ivThumb);
            } catch (Exception e) {
                Toast.makeText(HomeActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public int getItemCount() {
            return arr.length();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvSource, tvTitle, tvContent;
            ImageView ivThumb;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                tvSource = itemView.findViewById(R.id.tvSource);
                tvTitle = itemView.findViewById(R.id.tvTitle);
                tvContent = itemView.findViewById(R.id.tvContent);
                ivThumb = itemView.findViewById(R.id.ivThumb);
            }
        }
    }
}