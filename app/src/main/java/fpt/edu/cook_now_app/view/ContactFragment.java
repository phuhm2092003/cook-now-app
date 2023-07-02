package fpt.edu.cook_now_app.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import fpt.edu.cook_now_app.R;
import fpt.edu.cook_now_app.model.Contact;

public class ContactFragment extends Fragment {

    private EditText fullnameEditText, phoneNumberEditText, emailEditText, contentEditText;
    private ConstraintLayout sendIdeaButton;
    public static final String REGEX_FULLNAME = ".*\\d.*";
    public static final String REGEX_EMAIL = "[a-zA-Z\\d._%+-]+@[a-zA-Z\\d.-]+\\.[a-zA-Z]{2,}";
    public static final String INPUT_DATA_EMPTY_MESSAGE = "Vui lòng nhập đầy đủ thông tin";
    public static final String FULLNAME_INVALID_MESSAGE = "Họ và tên vui lòng không dùng ký tự số";
    public static final String PHONE_NUMBER_INVALID_MESSAGE = "Nhập số điện thoại sai";
    public static final String EMAIL_INVALID_MESSAGE = "Nhập email sai định dạng";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_contact, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    private void initView(@NonNull View view) {
        fullnameEditText = view.findViewById(R.id.fullnameEditText);
        phoneNumberEditText = view.findViewById(R.id.phoneNumberEditText);
        emailEditText = view.findViewById(R.id.emailEditText);
        contentEditText = view.findViewById(R.id.contentEditText);
        sendIdeaButton = view.findViewById(R.id.sendIdeaButton);
        sendIdeaButton.setOnClickListener(view1 -> handleSendIdea());
    }

    private void handleSendIdea() {
        String fullname = fullnameEditText.getText().toString().trim();
        String phoneNumber = phoneNumberEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String content = contentEditText.getText().toString().trim();

        if (isDataInputEmpty(fullname, phoneNumber, email, content)) {
            showMessageToast(INPUT_DATA_EMPTY_MESSAGE);
            return;
        }

        if (isFullNameInvalid(fullname)) {
            showMessageToast(FULLNAME_INVALID_MESSAGE);
            return;
        }

        if (isPhoneNumberInvalid(phoneNumber)) {
            showMessageToast(PHONE_NUMBER_INVALID_MESSAGE);
            return;
        }

        if (isEmailInvalid(email)) {
            showMessageToast(EMAIL_INVALID_MESSAGE);
            return;
        }

        performSendIdea(fullname, phoneNumber, email, content);

    }

    private boolean isDataInputEmpty(String fullname, String phoneNumber, String email, String content) {
        return fullname.isEmpty() || phoneNumber.isEmpty() || email.isEmpty() || content.isEmpty();
    }

    private boolean isFullNameInvalid(String fullname) {
        return fullname.matches(REGEX_FULLNAME);
    }

    private boolean isPhoneNumberInvalid(String phoneNumber) {
        return phoneNumber.length() < 10;
    }

    private boolean isEmailInvalid(String email) {
        return !email.matches(REGEX_EMAIL);
    }

    private void performSendIdea(String fullname, String phoneNumber, String email, String content) {
        DatabaseReference contactRef = FirebaseDatabase
                .getInstance()
                .getReference("contacts")
                .push();

        // Add data to FireBase database
        Contact contact = new Contact(contactRef.getKey(), fullname, phoneNumber, email, content);
        contactRef.setValue(contact)
                .addOnSuccessListener(unused -> {
                    showMessageToast("Gửi phản hồi thành công");
                    clearText();
                })
                .addOnFailureListener(e -> showMessageToast("Gửi phản hồi thất bại"));
    }

    private void clearText() {
        fullnameEditText.setText("");
        phoneNumberEditText.setText("");
        emailEditText.setText("");
        contentEditText.setText("");
    }

    private void showMessageToast(String mess) {
        Toast.makeText(getActivity(), mess, Toast.LENGTH_SHORT).show();
    }
}