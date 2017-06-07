package byenkov.com.dices.diceroller;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import characters.PlayerCharacter;

public class CharacterMenuFragment extends Fragment implements View.OnClickListener{
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_character_menu, container, false);
        Button upButton = (Button) view.findViewById(R.id.add_character_button);
        upButton.setOnClickListener(this);

        File path = getPlayerCharacterStorageDir(getContext(), "characters");

        List<File> fileList = getListFiles(path);

        if (!fileList.isEmpty()) setCharacters(fileList);

        return view;
    }

    public void setCharacters(List<File> characterFiles){


        ScrollView sv = (ScrollView) view.findViewById(R.id.character_scroll_view);

        for(final File element : characterFiles) {
            if (isExternalStorageReadable()) {

                RelativeLayout relativeLayout = new RelativeLayout(getContext());
                LinearLayout.LayoutParams rlp = new LinearLayout.LayoutParams(new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 300));
                rlp.setMargins(0,50,0,0);
                relativeLayout.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorMedium));
                relativeLayout.setLayoutParams(rlp);

                Button btn1 = new Button(getContext());
                btn1.setText("Delete");
                btn1.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));

                Button btn2 = new Button(getContext());
                btn2.setText("Edit");
                btn2.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));

                final TextView rowTextView = new TextView(getContext());

                Serializer serializer = new Persister();
                File source = element;

                PlayerCharacter character = null;

                try {
                    character = serializer.read(PlayerCharacter.class, source);
                }
                catch (Exception e){
                    e.printStackTrace();
                }

                ImageView img = new ImageView(getContext());

                InputStream imageStream = null;

                Bitmap selectedImage = stringToBitmap(character.getPortrait());

                img.setImageBitmap(selectedImage);

                RelativeLayout.LayoutParams lpImg = new RelativeLayout.LayoutParams(300,400);
                lpImg.addRule(RelativeLayout.CENTER_HORIZONTAL);
                lpImg.addRule(RelativeLayout.CENTER_VERTICAL);

                img.setLayoutParams(lpImg);

                img.setScaleType(ImageView.ScaleType.FIT_XY);

                LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.fragment_character_menu);

                rowTextView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                RelativeLayout.LayoutParams lpTv = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
                lpTv.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                lpTv.addRule(RelativeLayout.CENTER_VERTICAL);



                RelativeLayout.LayoutParams lpBtn1 = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
                lpBtn1.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                lpBtn1.addRule(RelativeLayout.ALIGN_PARENT_TOP);

                RelativeLayout.LayoutParams lpBtn2 = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
                lpBtn2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                lpBtn2.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                lpBtn2.setMargins(0, 2, 0, 0);

                rowTextView.setLayoutParams(lpTv);

                rowTextView.setText("Name: "+character.getName()+"\nLevel: "+character.getLevel()+"\nStrength: "+character.getStrength().toString()+"\nAgility: "+character.getAgility().toString()+"\nWisdom: "+character.getWisdom().toString()+"");
                // add the textview to the linearlayout
                relativeLayout.addView(rowTextView);

                btn1.setLayoutParams(lpBtn1);

                btn2.setLayoutParams(lpBtn2);

                relativeLayout.addView(btn1);

                relativeLayout.addView(btn2);

                relativeLayout.addView(img);

                linearLayout.addView(relativeLayout);

                btn1.setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                element.delete();
                                CharacterMenuFragment newFragment = new CharacterMenuFragment();
                                Bundle args = new Bundle();
                                newFragment.setArguments(args);

                                FragmentTransaction transaction = getFragmentManager().beginTransaction();

                                // Replace whatever is in the fragment_container view with this fragment,
                                // and add the transaction to the back stack so the user can navigate back
                                transaction.replace(R.id.fragment_container, newFragment);
                                transaction.addToBackStack(null);

                                // Commit the transaction
                                transaction.commit();
                            }
                        });

                final PlayerCharacter ch = character;
                btn2.setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                final FragmentTransaction ft = getFragmentManager().beginTransaction();
                                ft.replace(R.id.character_relative, new CharacterFormFragment());
                                ft.commit();
                            }
                        });

                sv.invalidate();
                sv.requestLayout();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_character_button:
                final FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.character_relative, new CharacterFormFragment());
                ft.commit();
                break;
        }
    }

    private List<File> getListFiles(File parentDir) {
        ArrayList<File> inFiles = new ArrayList<File>();
        File[] files = parentDir.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                inFiles.addAll(getListFiles(file));
            } else {
                if(file.getName().endsWith(".xml")){
                    inFiles.add(file);
                }
            }
        }
        return inFiles;
    }

    private File getPlayerCharacterStorageDir(Context context, String catalogName) {
        // Get the directory for the user's public pictures directory.
        File file = new File(context.getExternalFilesDir(
                Environment.DIRECTORY_DOCUMENTS), catalogName);
        return file;
    }

    /* Checks if external storage is available to at least read */
    private boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    public final static Bitmap stringToBitmap(String in){
        byte[] bytes = Base64.decode(in, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }
}