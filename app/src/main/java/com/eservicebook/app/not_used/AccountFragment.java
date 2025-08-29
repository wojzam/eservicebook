//package com.eservicebook.app.not_used;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.TextView;
//
//import androidx.fragment.app.Fragment;
//
//import com.eservicebook.app.R;
//import com.eservicebook.app.activities.LoginActivity;
//import com.eservicebook.app.data.DataManager;
//
//
//public class AccountFragment extends Fragment {
//
//    private final DataManager dataManager = DataManager.getInstance();
//    private Button logoutButton;
//    private TextView email;
//
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_account, container, false);
//
//        logoutButton = root.findViewById(R.id.button_logout);
//        email = root.findViewById(R.id.email);
//
//        //email.setText(dataManager.read(requireContext()));
//        email.setText("dupastanisława@post.pl");
//
//        logoutButton.setOnClickListener(v -> {
//            Intent intent = new Intent(getContext(), LoginActivity.class);
//            startActivity(intent);
//            requireActivity().finish();
//        });
//
//        return root;
//    }
//}