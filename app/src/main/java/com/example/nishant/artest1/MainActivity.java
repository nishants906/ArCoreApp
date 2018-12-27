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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.sceneform_fragment);
        initializeGallery();


        fragment.setOnTapArPlaneListener(new BaseArFragment.OnTapArPlaneListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onTapPlane(HitResult hitResult, Plane plane, MotionEvent motionEvent) {
                if (plane.getType()!=Plane.Type.HORIZONTAL_DOWNWARD_FACING){
                    return;
                }
                Anchor anchor = hitResult.createAnchor();
                placeObject(fragment,anchor,selectedobject);

            }
        });

    }

    public void initializeGallery(){
        selection = findViewById(R.id.selection);

        ImageView bed = new ImageView(this);
        bed.setImageResource(R.drawable.ic_launcher_background);
        bed.setContentDescription("Bed");
        bed.setOnClickListener(view ->{selectedobject = Uri.parse("bed.sfb");});
        selection.addView(bed);

        ImageView lamp = new ImageView(this);
        bed.setImageResource(R.drawable.lamp);
        bed.setContentDescription("lamp");
        bed.setOnClickListener(view ->{selectedobject = Uri.parse("lamp.sfb");});
        selection.addView(lamp);

        ImageView model = new ImageView(this);
        bed.setImageResource(R.drawable.ic_launcher_background);
        bed.setContentDescription("model");
        bed.setOnClickListener(view ->{selectedobject = Uri.parse("modela.sfb");});
        selection.addView(bed);

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void placeObject(ArFragment arFragment, Anchor anchor, Uri model){
        ModelRenderable.builder()
                .setSource(fragment.getContext(),model)
                .build()
                .thenAccept(renderable -> addnodetoscene(fragment,anchor,renderable))
                .exceptionally(throwable -> {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage(throwable.getMessage()).setTitle("Error");
                    AlertDialog dialog = builder.create();
                    dialog.show();
                    return null;

                });


    }

    public void addnodetoscene(ArFragment arFragment, Anchor anchor,Renderable renderable){

        AnchorNode anchorNode = new AnchorNode(anchor);
        TransformableNode node = new TransformableNode(fragment.getTransformationSystem());
        node.setRenderable(renderable);
        node.setParent(anchorNode);
        fragment.getArSceneView().getScene().addChild(node);
        node.select();
    }
}
