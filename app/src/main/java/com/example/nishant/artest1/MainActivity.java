package com.example.nishant.artest1;

import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.ar.core.Anchor;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.Renderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.BaseArFragment;
import com.google.ar.sceneform.ux.TransformableNode;

public class MainActivity extends AppCompatActivity {

    private ArFragment fragment;
    LinearLayout selection;
    public Uri selectedobject;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.sceneform_fragment);
        initializeGallery();


        fragment.setOnTapArPlaneListener((HitResult hitResult, Plane plane, MotionEvent motionEvent)->{
            Log.d("enter1","touch");
            if (plane.getType()!=Plane.Type.HORIZONTAL_UPWARD_FACING){
                Log.d("enter1","touch123");
                return;
            }
            Anchor anchor = hitResult.createAnchor();
            placeObject(fragment,anchor,selectedobject);
            Log.d("enter1","touch1");

        });




    }

    public void initializeGallery(){
        selection = findViewById(R.id.selection);

        ImageView bed = new ImageView(this);
        bed.setImageResource(R.drawable.lamppo);
        bed.setContentDescription("lamp");
        bed.setOnClickListener(view ->{selectedobject = Uri.parse("LampPost.sfb");
            Log.d("enter1","enter12");});
        selection.addView(bed);


    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void placeObject(ArFragment fragment, Anchor anchor, Uri model){
        Log.d("enter1","done23");

        ModelRenderable.builder()
                .setSource(fragment.getContext(),model)
                .build()
                .thenAccept(renderable -> addnodetoscene(fragment, anchor, renderable))
                .exceptionally(throwable -> {
                    Log.d("enter1",throwable.getMessage());
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage(throwable.getMessage()).setTitle("Error");
                    AlertDialog dialog = builder.create();
                    dialog.show();
                    return null;
                });


    }

    public void addnodetoscene(ArFragment fragment, Anchor anchor,Renderable renderable){

        Log.d("enter1","done");
        AnchorNode anchorNode = new AnchorNode(anchor);
        anchorNode.setParent(fragment.getArSceneView().getScene());

        TransformableNode node = new TransformableNode(fragment.getTransformationSystem());
        node.setParent(anchorNode);
        node.setRenderable(renderable);
        node.select();
        Log.d("enter1","done3456");
        Log.d("enter1", String.valueOf(node.getParent()));
        Log.d("enter1", String.valueOf(anchorNode.getAnchor()));
        Log.d("enter1", String.valueOf(anchor));


    }
}
