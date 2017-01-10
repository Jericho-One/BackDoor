package com.hackathon.backdoor;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

/**
 * Created by Aaron Dishman on 1/9/17.
 */
public class BackDoorFragment extends Fragment {
    public static final String TAG = BackDoorFragment.class.getCanonicalName();

    private static final String URL = "http://10.102.89.220:5000/open";

    private boolean success = false;
    private View backDoorView;
    private AnimationDrawable animation;
    private RequestQueue queue;

    public static Fragment newInstance() {
        Bundle args = new Bundle();
        BackDoorFragment fragment = new BackDoorFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);
        queue = Volley.newRequestQueue(getActivity());
        backDoorView = inflater.inflate(R.layout.back_door, container);
        final ImageView imageView = (ImageView) backDoorView.findViewById(R.id.fingerprint);
        animation = (AnimationDrawable) imageView.getBackground();
        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    animation.start();
                } else if (event.getAction() == MotionEvent.ACTION_UP && !success) {
                    imageView.setBackgroundDrawable(null);
                    imageView.setBackgroundResource(R.drawable.fingerprint_animation);
                    animation = (AnimationDrawable) imageView.getBackground();
                }
                return false;
            }
        });
        backDoorView.findViewById(R.id.fingerprint).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ((Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE)).vibrate(50);
                success = true;
                makeApiCall();
                return true;
            }
        });
        return v;
    }

    private void makeApiCall() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Snackbar.make(backDoorView, "UNLOCKED", BaseTransientBottomBar.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Snackbar.make(backDoorView, "YO SHIT FAILED!", BaseTransientBottomBar.LENGTH_SHORT).show();
            }
        });
        stringRequest.setTag(TAG);
        queue.add(stringRequest);
    }

    @Override
    public void onStop() {
        super.onStop();
        queue.cancelAll(TAG);
    }
}
