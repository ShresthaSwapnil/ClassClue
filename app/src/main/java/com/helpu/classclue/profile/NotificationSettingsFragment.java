package com.helpu.classclue.profile;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.helpu.classclue.R;
import com.helpu.classclue.utils.SharedPrefsHelper;

import java.io.IOException;

public class NotificationSettingsFragment extends Fragment {
    private static final int PICK_SOUND_REQUEST = 123;
    private SharedPrefsHelper prefs;
    private TextView tvSelectedSound;
    private MediaPlayer mediaPlayer;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification_settings, container, false);
        prefs = SharedPrefsHelper.getInstance(requireContext());

        tvSelectedSound = view.findViewById(R.id.tvSelectedSound);
        MaterialButton btnChooseSound = view.findViewById(R.id.btnChooseSound);
        MaterialButton btnPlaySound = view.findViewById(R.id.btnPlaySound);
        MaterialButton btnStopSound = view.findViewById(R.id.btnStopSound);

        updateSelectedSound();

        btnChooseSound.setOnClickListener(v -> checkPermissionsAndPickSound());
        btnPlaySound.setOnClickListener(v -> playSelectedSound());
        btnStopSound.setOnClickListener(v -> stopSound());

        return view;
    }

    private void checkPermissionsAndPickSound() {
        if (ContextCompat.checkSelfPermission(requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    PICK_SOUND_REQUEST);
        } else {
            openSoundPicker();
        }
    }

    private void openSoundPicker() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("audio/*");
        startActivityForResult(intent, PICK_SOUND_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_SOUND_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            Uri soundUri = data.getData();
            if (soundUri != null) {
                // Take persistable URI permission
                requireContext().getContentResolver().takePersistableUriPermission(
                        soundUri,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION
                );

                prefs.saveAlarmSound(soundUri.toString());
                updateSelectedSound();
            }
        }
    }

    private void updateSelectedSound() {
        String uriString = prefs.getAlarmSound();
        if (uriString != null && !uriString.isEmpty()) {
            Uri soundUri = Uri.parse(uriString);
            String displayName = getFileName(soundUri);
            tvSelectedSound.setText(displayName);
        } else {
            tvSelectedSound.setText("No sound selected");
        }
    }

    private String getFileName(Uri uri) {
        String name = "";
        try (Cursor cursor = requireContext().getContentResolver().query(uri,
                null, null, null, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                if (columnIndex != -1) {
                    name = cursor.getString(columnIndex);
                } else {
                    // Handle the case where DISPLAY_NAME is not available
                    name = "Unknown file name"; // Or some other appropriate default
                    // You might also want to log this:
                    // Log.w("NotificationSettings", "Column " + OpenableColumns.DISPLAY_NAME + " not found for URI: " + uri);
                }
            }
        }
        return name;
    }

    private void playSelectedSound() {
        stopSound();

        String uriString = prefs.getAlarmSound();
        if (uriString != null && !uriString.isEmpty()) {
            try {
                Uri soundUri = Uri.parse(uriString);
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setDataSource(requireContext(), soundUri);
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
                mediaPlayer.prepare();
                mediaPlayer.start();
            } catch (IOException e) {
                Toast.makeText(requireContext(), "Error playing sound", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void stopSound() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        stopSound();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PICK_SOUND_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openSoundPicker();
            } else {
                Toast.makeText(requireContext(), "Permission required to select sounds", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
