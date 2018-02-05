package cy.agorise.crystalwallet.activities;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import cy.agorise.crystalwallet.R;
import cy.agorise.crystalwallet.cryptonetinforequests.CryptoNetInfoRequestListener;
import cy.agorise.crystalwallet.cryptonetinforequests.CryptoNetInfoRequests;
import cy.agorise.crystalwallet.cryptonetinforequests.ValidateCreateBitsharesAccountRequest;
import cy.agorise.crystalwallet.models.Contact;
import cy.agorise.crystalwallet.models.GrapheneAccount;
import cy.agorise.crystalwallet.viewmodels.ContactListViewModel;
import cy.agorise.crystalwallet.viewmodels.ContactViewModel;
import cy.agorise.crystalwallet.viewmodels.validators.CreateContactValidator;
import cy.agorise.crystalwallet.viewmodels.validators.CreateSeedValidator;
import cy.agorise.crystalwallet.viewmodels.validators.UIValidatorListener;
import cy.agorise.crystalwallet.viewmodels.validators.validationfields.ValidationField;

public class CreateContactActivity extends AppCompatActivity implements UIValidatorListener {

    @BindView(R.id.etName)
    EditText etName;
    @BindView(R.id.tvNameError)
    TextView tvNameError;
    @BindView(R.id.btnCancel)
    Button btnCancel;
    @BindView(R.id.btnCreate)
    Button btnCreate;

    CreateContactValidator createContactValidator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_contact);
        ButterKnife.bind(this);

        btnCreate.setEnabled(false);
        createContactValidator = new CreateContactValidator(this.getApplicationContext(),etName);
        createContactValidator.setListener(this);
    }

    @OnTextChanged(value = R.id.etName,
            callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    void afterContactNameChanged(Editable editable) {
        this.createContactValidator.validate();
    }


    @OnClick(R.id.btnCancel)
    public void cancel(){
        this.finish();
    }

    @OnClick(R.id.btnCreate)
    public void createContact(){
        if (this.createContactValidator.isValid()) {
            Contact newContact = new Contact();
            newContact.setName(etName.getText().toString());
            ContactViewModel contactViewModel = ViewModelProviders.of(this).get(ContactViewModel.class);
            if (contactViewModel.addContact(newContact)){
                this.finish();
            } else {
                createContactValidator.validate();
            }
        }
    }

    @Override
    public void onValidationSucceeded(final ValidationField field) {
        final CreateContactActivity activity = this;

        activity.runOnUiThread(new Runnable() {
            public void run() {

                if (field.getView() == etName) {
                    tvNameError.setText("");
                }

                if (activity.createContactValidator.isValid()){
                    btnCreate.setEnabled(true);
                } else {
                    btnCreate.setEnabled(false);
                }
            }
        });
    }

    @Override
    public void onValidationFailed(final ValidationField field) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if (field.getView() == etName) {
                    tvNameError.setText(field.getMessage());
                }
            }
        });
    }
}
