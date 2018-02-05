package cy.agorise.crystalwallet.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.paging.PagedList;

import cy.agorise.crystalwallet.dao.CrystalDatabase;
import cy.agorise.crystalwallet.models.Contact;

/**
 * Created by Henry Varona on 2/4/2018.
 */

public class ContactViewModel extends AndroidViewModel {

    private CrystalDatabase db;

    public ContactViewModel(Application application) {
        super(application);
        this.db = CrystalDatabase.getAppDatabase(application.getApplicationContext());
    }

    public boolean addContact(Contact contact){
        return this.db.contactDao().add(contact)[0] >= 0;
    }
}
