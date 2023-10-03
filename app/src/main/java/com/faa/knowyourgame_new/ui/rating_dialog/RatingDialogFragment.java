package com.faa.knowyourgame_new.ui.rating_dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.faa.knowyourgame_new.R;
import com.faa.knowyourgame_new.dto.UserDto;
import com.faa.knowyourgame_new.retrofit.utils.DbUtils;

import static com.faa.knowyourgame_new.MainActivity.hasConnection;

public class RatingDialogFragment extends DialogFragment implements DialogInterface.OnClickListener {

    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());

        View view = getLayoutInflater().inflate(R.layout.rating_dialog, null);
        dialog.setView(view);

        TextView rating = view.findViewById(R.id.rating_info);
        Button close_rating = view.findViewById(R.id.btn_close_rating);

        Dialog ratingDialog = dialog.create();

        StringBuilder ratingText = new StringBuilder();

        if(hasConnection(getContext())) {
            DbUtils.getRating(getRatingResponse -> {
                for(UserDto elem: getRatingResponse.getUsers()){
                    ratingText.append(elem.getLogin())
                            .append(" ------- ")
                            .append(elem.getScore())
                            .append("\n");
                }
                rating.setText(ratingText);
            });
        }
        else{
            rating.setText("No connection");
        }

        close_rating.setOnClickListener(v -> {
            ratingDialog.dismiss();
        });

        return ratingDialog;
    }

    @Override
    public void onClick(DialogInterface dialog, int which) { }
}
