package it.gmaglio.sportsmanage.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import it.gmaglio.sportsmanage.R;

public class SignInDialog extends DialogFragment {

   private Toolbar toolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme_FullScreenDialog);
    }

    public static SignInDialog showSignInDialog(FragmentManager fragmentManager){

        SignInDialog signInDialog = new SignInDialog();
        signInDialog.show(fragmentManager, signInDialog.getClass().getCanonicalName());
        return signInDialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if(dialog != null){
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
            dialog.getWindow().setWindowAnimations(R.style.AppTheme_Slide);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.sign_in_with_mail_dialog, container, false);

        toolbar = view.findViewById(R.id.toolbar);


        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
       // toolbar.setNavigationOnClickListener(v -> dismiss());
        toolbar.setTitle("Some Title");
   //     toolbar.inflateMenu(R.menu.example_dialog);
       // toolbar.setOnMenuItemClickListener(item -> {
         //   dismiss();
           // return true;
        //});
    }
}
