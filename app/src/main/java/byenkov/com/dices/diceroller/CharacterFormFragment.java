package byenkov.com.dices.diceroller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.File;

import characters.PlayerCharacter;
import characters.SkillLevel;

public class CharacterFormFragment extends Fragment implements View.OnClickListener{
    View view;

    OnCharacterAddedListener characterAddedListener;

    public interface OnCharacterAddedListener {
        public void onCharacterAdded();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_character_form, container, false);
        Button submitButton = (Button) view.findViewById(R.id.submit_button);
        submitButton.setOnClickListener(this);
        Button cancelButton = (Button) view.findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(this);
        Button imageButton = (Button) view.findViewById(R.id.add_image_button);
        imageButton.setOnClickListener(this);
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            characterAddedListener = (OnCharacterAddedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement TextClicked");
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit_button:
                if (isExternalStorageWritable()) {

                    final EditText nameField = (EditText) view.findViewById(R.id.edit_name);
                    String name = nameField.getText().toString();

                    final EditText levelField = (EditText) view.findViewById(R.id.edit_level);
                    String level = levelField.getText().toString();

                    final Spinner strengthSpinner = (Spinner) view.findViewById(R.id.strength_list_pick);
                    String strength = strengthSpinner.getSelectedItem().toString();

                    final Spinner agilitySpinner = (Spinner) view.findViewById(R.id.agility_list_pick);
                    String agility = agilitySpinner.getSelectedItem().toString();

                    final Spinner wisdomSpinner = (Spinner) view.findViewById(R.id.wisdom_list_pick);
                    String wisdom = wisdomSpinner.getSelectedItem().toString();

                    String uri = ((CharacterActivity) getActivity()).getUriString();

                    if( !name.isEmpty() && !level.isEmpty() && uri != null) {
                        PlayerCharacter newCharacter = new PlayerCharacter();

                        newCharacter.setName(name);
                        newCharacter.setLevel(level);
                        newCharacter.setStrength(SkillLevel.valueOf(strength));
                        newCharacter.setAgility(SkillLevel.valueOf(agility));
                        newCharacter.setWisdom(SkillLevel.valueOf(wisdom));
                        newCharacter.setPortrait(uri);

                        Serializer serializer = new Persister();

                        File path = getPlayerCharacterStorageDir(getContext(), "characters");

                        File result = new File(path, name + ".xml");

                        try {
                            serializer.write(newCharacter, result);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                characterAddedListener.onCharacterAdded();

                getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
                break;
            case R.id.cancel_button:
                getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
                break;
            case R.id.add_image_button:
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
                break;
        }
    }

    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    public File getPlayerCharacterStorageDir(Context context, String catalogName) {
        // Get the directory for the user's public pictures directory.
        File file = new File(context.getExternalFilesDir(
                Environment.DIRECTORY_DOCUMENTS), catalogName);
        if (!file.mkdirs()) {
            System.out.println("Cant create dir");
        }
        return file;
    }


}