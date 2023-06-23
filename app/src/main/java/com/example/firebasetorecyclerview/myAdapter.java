package com.example.firebasetorecyclerview;

import android.app.ActionBar;
import android.content.DialogInterface;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;


public class myAdapter extends FirebaseRecyclerAdapter<myModel , myAdapter.myViewholder> {

    public myAdapter(@NonNull FirebaseRecyclerOptions<myModel> options) {
        super(options);
    }

    //  in case of firebase recycler view we have to pass two thgin
    //


// Convert the position and all others in to final class becouse when we use inner class there should be reqoiued to be final
    @Override
    protected void onBindViewHolder(@NonNull final  myViewholder holder,final int position , @NonNull final  myModel model) {
        holder.nametext.setText(model.getName());
        holder.coursetext.setText(model.getCourse());
        holder.emailtext.setText(model.getEmail());
        Glide.with(holder.img.getContext()).load(model.getPurl()).into(holder.img);
               // direct excees the edit or update text
               holder.update.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                      final DialogPlus dialogPlus = DialogPlus.newDialog(holder.img.getContext())
                              .setContentHolder(new ViewHolder(R.layout.dailog))
                              .setExpanded(true ,1100 )
                              .create();
                     // dialogPlus.show();
                       // now create a run time view
                       // without change in xml taking the refence from the dialog plus
                       View myview = dialogPlus.getHolderView();
                       EditText purl = myview.findViewById(R.id.purledit);
                       EditText name = myview.findViewById(R.id.nameedit);
                       EditText course = myview.findViewById(R.id.coursededit);
                       EditText email = myview.findViewById(R.id.emailedit);
                       Button submit = myview.findViewById(R.id.updatebtn);
                       // now taking all the view with the help of model object
                       purl.setText(model.getPurl());
                       name.setText(model.getName());
                       course.setText(model.getCourse());
                       email.setText(model.getEmail());
                       dialogPlus.show();

                       // now we have to work over submit on click on submit all the data get changed
                       submit.setOnClickListener(new View.OnClickListener() {
                           @Override
                           public void onClick(View v) {
                               // now we have to set the text new text using the hash map
                               HashMap<String ,Object> map = new HashMap<>();
                               map.put("purl" , purl.getText().toString());
                               map.put("name" , name.getText().toString());
                               map.put("course" , course.getText().toString());
                               map.put("email" , email.getText().toString());
                               FirebaseDatabase.getInstance().getReference().child("students")
                                       // through getref direct get s1 , s2 students on click ion cardview or particular object
                                       .child(getRef(holder.getAdapterPosition()).getKey()).updateChildren(map)
                                       .addOnSuccessListener(new OnSuccessListener<Void>() {
                                           @Override
                                           public void onSuccess(Void unused) {
                                            dialogPlus.dismiss();
                                           }
                                       })
                                       .addOnFailureListener(new OnFailureListener() {
                                           @Override
                                           public void onFailure(@NonNull Exception e) {
                                           dialogPlus.dismiss();
                                           }
                                       });

                           }
                       });
                   }
               });
            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // just creating a in built alert dialog box
                    AlertDialog.Builder alertdialog = new AlertDialog.Builder(holder.img.getContext());
                    alertdialog.setTitle("Delete your Profile");
                    alertdialog.setMessage("Your information will be get deleted ");
                    // on over click on yes text some action  will be perform
                    alertdialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            FirebaseDatabase.getInstance().getReference().child("students")
                                    // through getref direct get s1 , s2 students on click ion cardview or particular object
                                    .child(getRef(holder.getAdapterPosition()).getKey()).removeValue();
                        }
                    });
                    alertdialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // nothing to do
                        }
                    });

                    alertdialog.show();
                }
            });
    }

    @NonNull
    @Override
    public myViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_row  , parent , false);
        return new myViewholder(view);
    }

    public static class  myViewholder extends RecyclerView.ViewHolder {
        CircleImageView img;
        ImageView update , delete;
         TextView nametext , coursetext , emailtext;
        public myViewholder(@NonNull View itemView) {
            super(itemView);
            update =itemView.findViewById(R.id.updateimg);
            delete = itemView.findViewById(R.id.deleteimng);
            img = itemView.findViewById(R.id.profile_image);
            nametext = itemView.findViewById(R.id.nametext);
            coursetext = itemView.findViewById(R.id.coursetext) ;
            emailtext= itemView.findViewById(R.id.emailtext);
        }
    }

}
